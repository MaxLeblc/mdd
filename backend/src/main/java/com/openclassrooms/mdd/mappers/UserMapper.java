package com.openclassrooms.mdd.mappers;

import com.openclassrooms.mdd.models.User;
import com.openclassrooms.mdd.dto.response.UserDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {TopicMapper.class}) // “uses” allows the Topics mapper to be reused
public interface UserMapper {
    UserDto toDto(User user);
}
