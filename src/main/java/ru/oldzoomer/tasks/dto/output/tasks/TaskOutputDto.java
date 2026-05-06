package ru.oldzoomer.tasks.dto.output.tasks;

import lombok.Getter;
import lombok.Setter;
import ru.oldzoomer.tasks.constant.TaskPriority;
import ru.oldzoomer.tasks.constant.TaskStatus;

@Getter
@Setter
public class TaskOutputDto {
    private Long id;

    private String name;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private String authorEmail;

    private String assignedEmail;
}
