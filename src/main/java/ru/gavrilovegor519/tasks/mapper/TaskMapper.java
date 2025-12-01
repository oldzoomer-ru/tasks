package ru.gavrilovegor519.tasks.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.gavrilovegor519.tasks.dto.input.tasks.CreateTaskDto;
import ru.gavrilovegor519.tasks.dto.input.tasks.EditTaskDto;
import ru.gavrilovegor519.tasks.dto.output.tasks.TaskOutputDto;
import ru.gavrilovegor519.tasks.entity.Task;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {
    Task map(CreateTaskDto dto);

    Task map(EditTaskDto dto);

    TaskOutputDto map(Task task);
}
