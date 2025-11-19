package ru.gavrilovegor519.tasks.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gavrilovegor519.tasks.entity.Comments;
import ru.gavrilovegor519.tasks.entity.Task;
import ru.gavrilovegor519.tasks.entity.User;
import ru.gavrilovegor519.tasks.exception.CommentNotFoundException;
import ru.gavrilovegor519.tasks.exception.ForbiddenChangesException;
import ru.gavrilovegor519.tasks.exception.TaskNotFoundException;
import ru.gavrilovegor519.tasks.exception.UserNotFoundException;
import ru.gavrilovegor519.tasks.repo.CommentsRepository;
import ru.gavrilovegor519.tasks.repo.TaskRepository;
import ru.gavrilovegor519.tasks.repo.UserRepository;
import ru.gavrilovegor519.tasks.service.CommentsService;

@Service
@AllArgsConstructor
public class CommentsServiceImpl implements CommentsService {

    private static final String COMMENT_NOT_FOUND = "Comment not found.";
    private static final String USER_NOT_FOUND = "User not found!";
    private final UserRepository userRepository;
    private final CommentsRepository commentsRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Comments create(Comments comment, Long taskId, String email) {
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Author not found."));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found!"));

        comment.setAuthor(author);
        comment.setTask(task);

        return commentsRepository.save(comment);
    }

    @Override
    @Transactional
    public Comments edit(Long id, Comments changes, String email) {
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Comments comment = commentsRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(COMMENT_NOT_FOUND));

        if (!comment.getAuthor().getEmail().equals(author.getEmail())) {
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

        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        if (!comments.getAuthor().getEmail().equals(author.getEmail())) {
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
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        return commentsRepository.findAllByAuthor(author, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Comments> getMultipleCommentsForTask(Long taskId, Pageable pageable) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found."));

        return commentsRepository.findAllByTask(task, pageable);
    }
}
