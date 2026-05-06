package ru.oldzoomer.tasks.dto.input.tasks;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.oldzoomer.tasks.constant.TaskPriority;

@Getter
@Setter
public class EditTaskPriorityDto {
    @NotNull
    private TaskPriority priority;
}
