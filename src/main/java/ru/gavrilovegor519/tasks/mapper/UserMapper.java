package ru.gavrilovegor519.tasks.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.gavrilovegor519.tasks.dto.input.users.LoginDto;
import ru.gavrilovegor519.tasks.dto.input.users.RegDto;
import ru.gavrilovegor519.tasks.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User map(LoginDto dto);
    User map(RegDto dto);
}
