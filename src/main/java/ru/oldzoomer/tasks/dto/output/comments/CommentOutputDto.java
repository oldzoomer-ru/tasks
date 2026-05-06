package ru.oldzoomer.tasks.dto.output.comments;

public record CommentOutputDto(
        Long id,
        String text,
        String authorEmail
) {
}
