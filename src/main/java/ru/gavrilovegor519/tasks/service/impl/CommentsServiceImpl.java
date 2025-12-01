package ru.gavrilovegor519.tasks.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gavrilovegor519.tasks.entity.Comments;
import ru.gavrilovegor519.tasks.entity.Task;
import ru.gavrilovegor519.tasks.exception.CommentNotFoundException;
import ru.gavrilovegor519.tasks.exception.ForbiddenChangesException;
import ru.gavrilovegor519.tasks.exception.TaskNotFoundException;
import ru.gavrilovegor519.tasks.repo.CommentsRepository;
import ru.gavrilovegor519.tasks.repo.TaskRepository;
import ru.gavrilovegor519.tasks.service.CommentsService;

@Service
@AllArgsConstructor
public class CommentsServiceImpl implements CommentsService {

    private static final String COMMENT_NOT_FOUND = "Comment not found.";
    private final CommentsRepository commentsRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Comments create(Comments comment, Long taskId, String email) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found!"));

        comment.setAuthorEmail(email);
        comment.setTask(task);

        return commentsRepository.save(comment);
    }

    @Override
    @Transactional
    public Comments edit(Long id, Comments changes, String email) {
        Comments comment = commentsRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(COMMENT_NOT_FOUND));

        if (!comment.getAuthorEmail().equals(email)) {
            throw new ForbiddenChangesException("Changes of data must do only his author!");
        } else {
            comment.setText(changes.getText());
            return commentsRepository.save(comment);
        }
    }

    @Override
    @Transactional
    public void delete(Long id, String email) {
        Comments comments = commentsRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(COMMENT_NOT_FOUND));

        if (!comments.getAuthorEmail().equals(email)) {
            throw new ForbiddenChangesException("Changes of data must do only his author!");
        } else {
            Task task = comments.getTask();
            task.getComments().remove(comments);
            commentsRepository.delete(comments);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Comments getComment(Long id) {
        return commentsRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(COMMENT_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Comments> getMultipleCommentsForUser(String email, Pageable pageable) {
        return commentsRepository.findAllByAuthorEmail(email, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Comments> getMultipleCommentsForTask(Long taskId, Pageable pageable) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found."));

        return commentsRepository.findAllByTask(task, pageable);
    }
}
