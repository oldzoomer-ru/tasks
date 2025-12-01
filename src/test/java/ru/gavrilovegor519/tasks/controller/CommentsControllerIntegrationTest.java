package ru.gavrilovegor519.tasks.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.gavrilovegor519.tasks.constant.TaskPriority;
import ru.gavrilovegor519.tasks.constant.TaskStatus;
import ru.gavrilovegor519.tasks.dto.input.comments.CreateCommentDto;
import ru.gavrilovegor519.tasks.dto.input.comments.EditCommentDto;
import ru.gavrilovegor519.tasks.entity.Comments;
import ru.gavrilovegor519.tasks.entity.Task;
import ru.gavrilovegor519.tasks.repo.CommentsRepository;
import ru.gavrilovegor519.tasks.repo.TaskRepository;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommentsControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @AfterEach
    void tearDown() {
        commentsRepository.deleteAll();
        taskRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "author@email.com")
    void createComment_shouldCreateComment() throws Exception {
        Task task = createTask();

        CreateCommentDto createCommentDto = new CreateCommentDto();
        createCommentDto.setTaskId(task.getId());
        createCommentDto.setText("Test comment");

        mockMvc.perform(post("/api/1.0/comments/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCommentDto)))
                .andExpect(status().isCreated());

        Comments comment = commentsRepository.findAll().getFirst();
        assertNotNull(comment);
        assertEquals("Test comment", comment.getText());
        assertEquals("author@email.com", comment.getAuthorEmail());
        assertEquals(task.getId(), comment.getTask().getId());
    }

    @Test
    @WithMockUser(username = "author@email.com")
    void getAllCommentsForUser_shouldReturnComments() throws Exception {
        Task task = createTask();
        Comments comment = createComment("Test comment", task);
        mockMvc.perform(get("/api/1.0/comments/get/user")
                        .param("start", "0")
                        .param("end", "10")
                        .param("email", "author@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(comment.getId()))
                .andExpect(jsonPath("$.data.content[0].text").value("Test comment"));
    }

    @Test
    @WithMockUser(username = "author@email.com")
    void getAllCommentsForTask_shouldReturnComments() throws Exception {
        Task task = createTask();
        Comments comment = createComment("Test comment", task);
        mockMvc.perform(get("/api/1.0/comments/get/task")
                        .param("start", "0")
                        .param("end", "10")
                        .param("taskId", task.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(comment.getId()))
                .andExpect(jsonPath("$.data.content[0].text").value("Test comment"));
    }

    @Test
    @WithMockUser(username = "author@email.com")
    void getComment_shouldReturnComment() throws Exception {
        Task task = createTask();
        Comments comment = createComment("Test comment", task);
        mockMvc.perform(get("/api/1.0/comments/get/" + comment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(comment.getId()))
                .andExpect(jsonPath("$.data.text").value("Test comment"));
    }

    @Test
    @WithMockUser(username = "author@email.com")
    void editComment_shouldEditComment() throws Exception {
        Task task = createTask();
        Comments comment = createComment("Original comment", task);

        EditCommentDto editCommentDto = createEditCommentDto(comment.getId());
        mockMvc.perform(put("/api/1.0/comments/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editCommentDto)))
                .andExpect(status().isOk());

        Comments updatedComment = commentsRepository.findById(comment.getId()).orElseThrow();
        assertNotNull(updatedComment);
        assertEquals("Updated comment", updatedComment.getText());
    }

    private Task createTask() {
        Task task = new Task();
        task.setName("Test Task");
        task.setDescription("Test Description");
        task.setAuthorEmail("author@email.com");
        task.setPriority(TaskPriority.LOW);
        task.setStatus(TaskStatus.FINISHED);
        task.setAssignedEmail("assigned@email.com");
        return taskRepository.save(task);
    }

    private Comments createComment(String text, Task task) {
        Comments comment = new Comments();
        comment.setText(text);
        comment.setAuthorEmail("author@email.com");
        comment.setTask(task);
        return commentsRepository.save(comment);
    }

    private EditCommentDto createEditCommentDto(Long commentId) {
        EditCommentDto editCommentDto = new EditCommentDto();
        editCommentDto.setCommentId(commentId);
        editCommentDto.setText("Updated comment");
        return editCommentDto;
    }
}
