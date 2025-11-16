package ru.gavrilovegor519.tasks.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.gavrilovegor519.tasks.entity.Comments;

public interface CommentsService {
    Comments create(Comments comment, Long taskId, String email);

    Comments edit(Long id, Comments changes, String email);
    void delete(Long id, String email);
    Comments getComment(Long id);
    Page<Comments> getMultipleCommentsForUser(String email, Pageable pageable);
    Page<Comments> getMultipleCommentsForTask(Long taskId, Pageable pageable);
}
