package ru.oldzoomer.tasks.dto.input.tasks;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditTaskAssignedUserDto {
    @Email
    @NotBlank
    @Size(max = 50)
    private String assignedEmail;
}
