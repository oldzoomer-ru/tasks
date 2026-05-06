package ru.oldzoomer.tasks.dto.input.tasks;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.oldzoomer.tasks.constant.TaskStatus;

@Getter
@Setter
public class EditTaskStatusDto {
    @NotNull
    private TaskStatus status;
}
