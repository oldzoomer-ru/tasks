package ru.oldzoomer.tasks.dto.input.tasks;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.oldzoomer.tasks.constant.TaskPriority;
import ru.oldzoomer.tasks.constant.TaskStatus;

public record CreateTaskDto(
        @Size(max = 100, message = "Name should be less than 100 symbols")
        @NotEmpty(message = "Name should not be empty") String name,
        @Size(max = 300, message = "The description should be less than 300 symbols")
        @NotEmpty(message = "Description should not be empty") String description,
        @NotNull(message = "Status should not be null") TaskStatus status,
        @NotNull(message = "Priority should not be null") TaskPriority priority,
        @Email(message = "Invalid email")
        @Size(max = 50, message = "Email must be less than 50 characters")
        @NotEmpty(message = "Email can't be empty") String assignedEmail
) {
}
