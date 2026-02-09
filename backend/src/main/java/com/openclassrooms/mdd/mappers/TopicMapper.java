package com.openclassrooms.mdd.mappers;

import com.openclassrooms.mdd.models.Topic;
import com.openclassrooms.mdd.dto.response.TopicDto;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    
    // Transforms an Entity into a DTO
    TopicDto toDto(Topic topic);

    // Transforms a list of Entities into a list of DTOs
    List<TopicDto> toDtos(List<Topic> topics);
}
