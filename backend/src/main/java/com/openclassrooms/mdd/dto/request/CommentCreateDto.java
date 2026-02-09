package com.openclassrooms.mdd.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateDto(
    @NotBlank
    @Size(max = 2000)
    String content
) {}
