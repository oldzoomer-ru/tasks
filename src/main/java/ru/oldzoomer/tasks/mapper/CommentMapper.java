package ru.oldzoomer.tasks.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.oldzoomer.tasks.dto.input.comments.CreateCommentDto;
import ru.oldzoomer.tasks.dto.input.comments.EditCommentDto;
import ru.oldzoomer.tasks.dto.output.comments.CommentOutputDto;
import ru.oldzoomer.tasks.entity.Comments;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    Comments map(CreateCommentDto dto);

    Comments map(EditCommentDto dto);

	CommentOutputDto map(Comments comment);
}
