package com.openclassrooms.mdd.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.openclassrooms.mdd.dto.response.PostDto;
import com.openclassrooms.mdd.models.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {

    // MapStruct will automatically do: post.getAuthor().getUsername()
    @Mapping(source = "author.username", target = "authorName") // Map the author's username to the DTO
    @Mapping(source = "topic.title", target = "topicName") // Map the topic's title to the DTO
    PostDto toDto(Post post);

    List<PostDto> toDtos(List<Post> posts);
}
