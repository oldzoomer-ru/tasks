package ru.gavrilovegor519.tasks.dto.output.tasks;

import lombok.Getter;
import lombok.Setter;
import ru.gavrilovegor519.tasks.constant.TaskPriority;
import ru.gavrilovegor519.tasks.constant.TaskStatus;

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
