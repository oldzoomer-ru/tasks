package ru.gavrilovegor519.tasks.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gavrilovegor519.tasks.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}