package com.openclassrooms.mdd.mappers;

import com.openclassrooms.mdd.models.User;
import com.openclassrooms.mdd.dto.response.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TopicMapper.class})
public interface UserMapper {
    UserDto toDto(User user);
}
