package com.openclassrooms.mdd.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record UserDto(
    Long id,
    String username,
    String email,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<TopicDto> subscriptions
) {}
