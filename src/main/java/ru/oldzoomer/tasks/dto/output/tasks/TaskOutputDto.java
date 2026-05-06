package ru.oldzoomer.tasks.dto.output.tasks;

import ru.oldzoomer.tasks.constant.TaskPriority;
import ru.oldzoomer.tasks.constant.TaskStatus;

public record TaskOutputDto(
        Long id,
        String name,
        String description,
        TaskStatus status,
        TaskPriority priority,
        String authorEmail,
        String assignedEmail
) {
}
