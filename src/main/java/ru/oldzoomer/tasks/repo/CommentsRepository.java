package ru.oldzoomer.tasks.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.oldzoomer.tasks.entity.Comments;
import ru.oldzoomer.tasks.entity.Task;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
    Page<Comments> findAllByAuthorEmail(String authorEmail, Pageable pageable);

    Page<Comments> findAllByTask(Task task, Pageable pageable);
}