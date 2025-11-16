package ru.gavrilovegor519.tasks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.gavrilovegor519.tasks.config.TestContainersConfig;
import ru.gavrilovegor519.tasks.constant.TaskPriority;
import ru.gavrilovegor519.tasks.constant.TaskStatus;
import ru.gavrilovegor519.tasks.dto.input.comments.CreateCommentDto;
import ru.gavrilovegor519.tasks.dto.input.comments.EditCommentDto;
import ru.gavrilovegor519.tasks.entity.Comments;
import ru.gavrilovegor519.tasks.entity.Task;
import ru.gavrilovegor519.tasks.entity.User;
import ru.gavrilovegor519.tasks.repo.CommentsRepository;
import ru.gavrilovegor519.tasks.repo.TaskRepository;
import ru.gavrilovegor519.tasks.repo.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentsControllerIntegrationTest extends TestContainersConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @AfterEach
    void tearDown() {
        commentsRepository.deleteAll();
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void createComment_shouldCreateComment() throws Exception {
        User user = createUser("test@example.com", "password");
        User user2 = createUser("test2@example.com", "password");
        Task task = createTask("Test Task", "Test Description", user, TaskPriority.LOW, TaskStatus.FINISHED, user2);

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
        assertEquals("test@example.com", comment.getAuthor().getEmail());
        assertEquals(task.getId(), comment.getTask().getId());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void getAllCommentsForUser_shouldReturnComments() throws Exception {
        User user = createUser("test@example.com", "password");
        User user2 = createUser("test2@example.com", "password");
        Task task = createTask("Test Task", "Test Description", user, TaskPriority.LOW, TaskStatus.FINISHED, user2);
        Comments comment = createComment("Test comment", user, task);
        mockMvc.perform(get("/api/1.0/comments/get/user")
                        .param("start", "0")
                        .param("end", "10")
                        .param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(comment.getId()))
                .andExpect(jsonPath("$.data.content[0].text").value("Test comment"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void getAllCommentsForTask_shouldReturnComments() throws Exception {
        User user = createUser("test@example.com", "password");
        User user2 = createUser("test2@example.com", "password");
        Task task = createTask("Test Task", "Test Description", user, TaskPriority.LOW, TaskStatus.FINISHED, user2);
        Comments comment = createComment("Test comment", user, task);
        mockMvc.perform(get("/api/1.0/comments/get/task")
                        .param("start", "0")
                        .param("end", "10")
                        .param("taskId", task.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(comment.getId()))
                .andExpect(jsonPath("$.data.content[0].text").value("Test comment"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void getComment_shouldReturnComment() throws Exception {
        User user = createUser("test@example.com", "password");
        User user2 = createUser("test2@example.com", "password");
        Task task = createTask("Test Task", "Test Description", user, TaskPriority.LOW, TaskStatus.FINISHED, user2);
        Comments comment = createComment("Test comment", user, task);
        mockMvc.perform(get("/api/1.0/comments/get/" + comment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(comment.getId()))
                .andExpect(jsonPath("$.data.text").value("Test comment"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void editComment_shouldEditComment() throws Exception {
        User user = createUser("test@example.com", "password");
        User user2 = createUser("test2@example.com", "password");
        Task task = createTask("Test Task", "Test Description", user, TaskPriority.LOW, TaskStatus.FINISHED, user2);
        Comments comment = createComment("Original comment", user, task);

        EditCommentDto editCommentDto = createEditCommentDto(comment.getId(), "Updated comment");
        mockMvc.perform(put("/api/1.0/comments/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editCommentDto)))
                .andExpect(status().isOk());

        Comments updatedComment = commentsRepository.findById(comment.getId()).orElseThrow();
        assertNotNull(updatedComment);
        assertEquals("Updated comment", updatedComment.getText());
    }

    private User createUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        return userRepository.save(user);
    }

    private Task createTask(String name, String description, User author, TaskPriority priority, TaskStatus status, User assigned) {
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setAuthor(author);
        task.setPriority(priority);
        task.setStatus(status);
        task.setAssigned(assigned);
        return taskRepository.save(task);
    }

    private Comments createComment(String text, User author, Task task) {
        Comments comment = new Comments();
        comment.setText(text);
        comment.setAuthor(author);
        comment.setTask(task);
        return commentsRepository.save(comment);
    }

    private EditCommentDto createEditCommentDto(Long commentId, String text) {
        EditCommentDto editCommentDto = new EditCommentDto();
        editCommentDto.setCommentId(commentId);
        editCommentDto.setText(text);
        return editCommentDto;
    }
}
