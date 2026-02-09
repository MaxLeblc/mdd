package com.openclassrooms.mdd.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostCreateDto(
    @NotBlank
    @Size(max = 255, message = "Title must not exceed 255 characters")
    String title,
    
    @NotBlank
    String content,
    
    @NotNull
    Long topicId
) {}
