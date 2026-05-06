package ru.oldzoomer.tasks.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.oldzoomer.tasks.dto.input.tasks.CreateTaskDto;
import ru.oldzoomer.tasks.dto.input.tasks.EditTaskDto;
import ru.oldzoomer.tasks.dto.output.tasks.TaskOutputDto;
import ru.oldzoomer.tasks.entity.Task;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {
    Task map(CreateTaskDto dto);

    Task map(EditTaskDto dto);

    TaskOutputDto map(Task task);
}
