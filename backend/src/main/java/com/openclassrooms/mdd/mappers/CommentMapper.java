package com.openclassrooms.mdd.mappers;

import com.openclassrooms.mdd.models.Comment;
import com.openclassrooms.mdd.dto.response.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper (componentModel = "spring")
public interface CommentMapper {
    
    @Mapping(source="author.username", target="authorUsername")
    CommentDto toDto(Comment comment);

    List<CommentDto> toDtos(List<Comment> comments);
}
