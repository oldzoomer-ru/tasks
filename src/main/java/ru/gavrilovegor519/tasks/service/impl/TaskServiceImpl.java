package ru.gavrilovegor519.tasks.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gavrilovegor519.tasks.constant.TaskPriority;
import ru.gavrilovegor519.tasks.constant.TaskStatus;
import ru.gavrilovegor519.tasks.entity.Task;
import ru.gavrilovegor519.tasks.exception.ForbiddenChangesException;
import ru.gavrilovegor519.tasks.exception.TaskNotFoundException;
import ru.gavrilovegor519.tasks.repo.TaskRepository;
import ru.gavrilovegor519.tasks.service.TaskService;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final String TASK_NOT_FOUND = "Task not found.";

    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Task create(Task task, String email, String assignedEmail) {
        task.setAuthorEmail(email);
        task.setAssignedEmail(assignedEmail);
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public void delete(Long id, String email) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND));
        if (!task.getAuthorEmail().equals(email)) {
            throw new ForbiddenChangesException("Changes of data must do only his author!");
        }
        taskRepository.delete(task);
    }

    @Override
    @Transactional
    public Task editStatus(Long id, TaskStatus status, String email) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND));
        if (!task.getAuthorEmail().equals(email) && !email.equals(task.getAssignedEmail())) {
            throw new ForbiddenChangesException("Changes of data must do only his author, or assigned user!");
        }
        task.setStatus(status);
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task editPriority(Long id, TaskPriority priority, String email) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND));
        if (!task.getAuthorEmail().equals(email)) {
            throw new ForbiddenChangesException("Changes of data must do only his author!");
        }
        task.setPriority(priority);
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task editNameAndDescription(Long id, Task task, String email) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND));
        if (!existing.getAuthorEmail().equals(email)) {
            throw new ForbiddenChangesException("Changes of data must do only his author!");
        }
        existing.setName(task.getName());
        existing.setDescription(task.getDescription());
        return taskRepository.save(existing);
    }

    @Override
    @Transactional
    public Task editAssignedUser(Long id, String assignedEmail, String email) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND));
        if (!task.getAuthorEmail().equals(email)) {
            throw new ForbiddenChangesException("Changes of data must do only his author!");
        }
        task.setAssignedEmail(assignedEmail);
        return taskRepository.save(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Task> getMultipleTasksForUser(String email, Pageable pageable) {
        return taskRepository.findAllByAuthorEmail(email, pageable);
    }
}
