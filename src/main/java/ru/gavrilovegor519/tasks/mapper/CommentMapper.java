package ru.gavrilovegor519.tasks.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.gavrilovegor519.tasks.dto.input.comments.CreateCommentDto;
import ru.gavrilovegor519.tasks.dto.input.comments.EditCommentDto;
import ru.gavrilovegor519.tasks.dto.output.comments.CommentOutputDto;
import ru.gavrilovegor519.tasks.entity.Comments;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    Comments map(CreateCommentDto dto);

    Comments map(EditCommentDto dto);

	CommentOutputDto map(Comments comment);
}
