package ru.oldzoomer.tasks.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.oldzoomer.tasks.constant.TaskPriority;
import ru.oldzoomer.tasks.constant.TaskStatus;
import ru.oldzoomer.tasks.entity.Task;

public interface TaskService {
    Task create(Task task, String email, String assignedEmail);
    void delete(Long id, String email);

    Task editStatus(Long id, TaskStatus status, String email);

    Task editPriority(Long id, TaskPriority priority, String email);

    Task editNameAndDescription(Long id, Task task, String email);

    Task editAssignedUser(Long id, String assignedEmail, String email);
    Task getTask(Long id);
    Page<Task> getMultipleTasksForUser(String email, Pageable pageable);
}
