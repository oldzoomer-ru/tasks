package ru.oldzoomer.tasks.dto.input.comments;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCommentDto(
        @NotNull Long taskId,
        @Size(max = 300, message = "Comment must be less than 300 characters")
        @NotEmpty(message = "Comment can't be empty") String text
) {
}
