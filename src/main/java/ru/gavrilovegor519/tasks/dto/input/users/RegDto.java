package ru.gavrilovegor519.tasks.dto.input.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegDto {
    @Size(max = 50, message = "Email must be less than 50 characters")
    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Invalid email")
    private String email;

    @NotEmpty(message = "Password can't be empty")
    private String password;
}
