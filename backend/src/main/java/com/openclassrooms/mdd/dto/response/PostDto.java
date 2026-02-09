package com.openclassrooms.mdd.dto.response;

import java.time.LocalDateTime;

public record PostDto(
    Long id,
    String title,
    String content,
    LocalDateTime createdAt,
    String authorName, // We want the username, not just the ID.
    String topicName // We want the theme title
) {}
