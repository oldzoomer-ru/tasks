package ru.oldzoomer.tasks.dto.input.tasks;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record EditTaskDto(
        @Size(max = 100, message = "Name should be less than 100 symbols")
        @NotEmpty(message = "Name should not be empty") String name,
        @Size(max = 300, message = "The description should be less than 300 symbols")
        @NotEmpty(message = "Description should not be empty") String description
) {
}
