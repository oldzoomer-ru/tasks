package ru.gavrilovegor519.tasks.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gavrilovegor519.tasks.constant.TaskPriority;
import ru.gavrilovegor519.tasks.constant.TaskStatus;
import ru.gavrilovegor519.tasks.entity.Task;
import ru.gavrilovegor519.tasks.entity.User;
import ru.gavrilovegor519.tasks.exception.ForbiddenChangesException;
import ru.gavrilovegor519.tasks.exception.TaskNotFoundException;
import ru.gavrilovegor519.tasks.exception.UserNotFoundException;
import ru.gavrilovegor519.tasks.repo.TaskRepository;
import ru.gavrilovegor519.tasks.repo.UserRepository;
import ru.gavrilovegor519.tasks.service.TaskService;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final String TASK_NOT_FOUND = "Task not found.";
    private static final String USER_NOT_FOUND = "User not found!";
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Task create(Task task, String email, String assignedEmail) {
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Author not found."));
        User assignedUser = userRepository.findByEmail(assignedEmail)
                .orElseThrow(() -> new UserNotFoundException("Assigned user not found."));

        task.setAuthor(author);
        task.setAssigned(assignedUser);

        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public void delete(Long id, String email) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND));

        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        if (!task.getAuthor().getEmail().equals(author.getEmail())) {
            throw new ForbiddenChangesException("Changes of data must do only his author!");
        } else {
            taskRepository.delete(task);
        }
    }

    @Override
    @Transactional
    public Task editStatus(Long id, TaskStatus status, String email) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        if (!task.getAuthor().getEmail().equals(user.getEmail())
                && !task.getAssigned().getEmail().equals(user.getEmail())) {
            throw new ForbiddenChangesException("Changes of data must do only his author, or assigned user!");
        } else {
            task.setStatus(status);
            return taskRepository.save(task);
        }
    }

    @Override
    @Transactional
    public Task editPriority(Long id, TaskPriority priority, String email) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND));

        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        if (!task.getAuthor().getEmail().equals(author.getEmail())) {
            throw new ForbiddenChangesException("Changes of data must do only his author!");
        } else {
            task.setPriority(priority);
            return taskRepository.save(task);
        }
    }

    @Override
    @Transactional
    public Task editNameAndDescription(
            Long id, Task task, String email) {
        Task task1 = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND));

        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        if (!task1.getAuthor().getEmail().equals(author.getEmail())) {
            throw new ForbiddenChangesException("Changes of data must do only his author!");
        } else {
            task1.setName(task.getName());
            task1.setDescription(task.getDescription());
            return taskRepository.save(task1);
        }
    }

    @Override
    @Transactional
    public Task editAssignedUser(Long id, String assignedEmail, String email) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND));

        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        User assignedUser = userRepository.findByEmail(assignedEmail)
                .orElseThrow(() -> new UserNotFoundException("Assigned user not found!"));

        if (!task.getAuthor().getEmail().equals(author.getEmail())) {
            throw new ForbiddenChangesException("Changes of data must do only his author!");
        } else {
            task.setAssigned(assignedUser);
            return taskRepository.save(task);
        }
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
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        return taskRepository.findAllByAuthor(author, pageable);
    }
}
