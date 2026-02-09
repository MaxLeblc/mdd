package com.openclassrooms.mdd.dto.response;

import java.time.LocalDate;

public record CommentDto(
    Long id,
    String content,
    String authorUsername,
    LocalDate createdAt
) {}
