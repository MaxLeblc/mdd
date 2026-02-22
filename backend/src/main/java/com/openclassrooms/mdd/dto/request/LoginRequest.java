package com.openclassrooms.mdd.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank String emailOrUsername,
    @NotBlank String password
) {}
