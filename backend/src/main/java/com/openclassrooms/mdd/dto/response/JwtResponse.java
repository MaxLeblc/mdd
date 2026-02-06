package com.openclassrooms.mdd.dto.response;

public record JwtResponse(
    String token,
    String type,
    Long id,
    String username,
    String email
) {
    public JwtResponse(String accessToken, Long id, String username, String email) {
        this(accessToken, "Bearer", id, username, email);
    }
}
