package ru.oldzoomer.tasks.dto.input.tasks;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EditTaskAssignedUserDto(
        @Email
        @NotBlank
        @Size(max = 50) String assignedEmail
) {
}
