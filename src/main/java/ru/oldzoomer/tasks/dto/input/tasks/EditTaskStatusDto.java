package ru.oldzoomer.tasks.dto.input.tasks;

import jakarta.validation.constraints.NotNull;
import ru.oldzoomer.tasks.constant.TaskStatus;

public record EditTaskStatusDto(
        @NotNull TaskStatus status
) {
}
