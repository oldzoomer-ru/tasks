package ru.gavrilovegor519.tasks.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gavrilovegor519.tasks.constant.TaskStatus;
import ru.gavrilovegor519.tasks.entity.Task;
import ru.gavrilovegor519.tasks.exception.ForbiddenChangesException;
import ru.gavrilovegor519.tasks.repo.TaskRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    private static final String AUTHOR_EMAIL = "author@email.com";
    private static final String ASSIGNED_EMAIL = "assigned@email.com";

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void editStatusAsAuthor() {
        Task task = mock(Task.class);

        when(task.getAuthorEmail()).thenReturn(AUTHOR_EMAIL);
        when(taskRepository.findById(0L)).thenReturn(Optional.of(task));

        taskService.editStatus(0L, TaskStatus.FINISHED, "author@email.com");

        verify(task).setStatus(TaskStatus.FINISHED);
    }

    @Test
    void editStatusAsAssigned() {
        Task task = mock(Task.class);

        when(task.getAuthorEmail()).thenReturn(AUTHOR_EMAIL);
        when(task.getAssignedEmail()).thenReturn(ASSIGNED_EMAIL);
        when(taskRepository.findById(0L)).thenReturn(Optional.of(task));

        taskService.editStatus(0L, TaskStatus.FINISHED, "assigned@email.com");

        verify(task).setStatus(TaskStatus.FINISHED);
    }

    @Test
    void editStatusAsNotAuthorOrAssigned() {
        Task task = mock(Task.class);

        when(task.getAuthorEmail()).thenReturn(AUTHOR_EMAIL);
        when(task.getAssignedEmail()).thenReturn(ASSIGNED_EMAIL);
        when(taskRepository.findById(0L)).thenReturn(Optional.of(task));

        assertThrows(ForbiddenChangesException.class,
                () -> taskService.editStatus(0L, TaskStatus.FINISHED, "3@1.ru"));
    }
}
