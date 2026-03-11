package ru.gavrilovegor519.tasks.service;

import ru.gavrilovegor519.tasks.constant.TaskPriority;
import ru.gavrilovegor519.tasks.constant.TaskStatus;
import ru.gavrilovegor519.tasks.entity.Task;

import java.util.List;

public interface TaskService {
    Task create(Task task, String email, String assignedEmail);
    void delete(Long id, String email);

    Task editStatus(Long id, TaskStatus status, String email);

    Task editPriority(Long id, TaskPriority priority, String email);

    Task editNameAndDescription(Long id, Task task, String email);

    Task editAssignedUser(Long id, String assignedEmail, String email);
    Task getTask(Long id);

    List<Task> getMultipleTasksForUser(String email);
}
