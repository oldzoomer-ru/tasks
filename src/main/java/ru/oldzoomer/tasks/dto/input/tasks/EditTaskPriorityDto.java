package ru.oldzoomer.tasks.dto.input.tasks;

import jakarta.validation.constraints.NotNull;
import ru.oldzoomer.tasks.constant.TaskPriority;

public record EditTaskPriorityDto(
        @NotNull TaskPriority priority
) {
}
