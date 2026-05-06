package ru.oldzoomer.tasks.dto.output.comments;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentOutputDto {
    private Long id;

    private String text;

    private String authorEmail;
}
