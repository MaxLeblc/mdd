package com.openclassrooms.mdd.mappers;

import com.openclassrooms.mdd.models.Topic;
import com.openclassrooms.mdd.dto.response.TopicDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface TopicMapper {
    
    // Transforms an Entity into a DTO
    TopicDto toDto(Topic topic);

    // Transforms a list of Entities into a list of DTOs
    List<TopicDto> toDtos(List<Topic> topics);
}
