package com.team2.hrbank.changelog.mapper;

import com.team2.hrbank.changelog.domain.ChangeLog;
import com.team2.hrbank.changelog.dto.ChangeLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChangeLogMapper {

    @Mapping(target = "ipAddress", source = "ipAddress.ipAddress")
    ChangeLogDto toDto(ChangeLog changeLog);

    List<ChangeLogDto> toDtoList(List<ChangeLog> changeLogs);

}
