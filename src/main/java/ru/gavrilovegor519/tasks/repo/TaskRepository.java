package ru.gavrilovegor519.tasks.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.gavrilovegor519.tasks.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @EntityGraph(attributePaths = "comments")
    Page<Task> findAllByAuthorEmail(String authorEmail, Pageable pageable);
}