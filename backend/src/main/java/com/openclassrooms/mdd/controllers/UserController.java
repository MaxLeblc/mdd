package com.openclassrooms.mdd.controllers;

import com.openclassrooms.mdd.models.User;
import com.openclassrooms.mdd.dto.request.UserUpdateRequest;
import com.openclassrooms.mdd.dto.response.UserDto;
import com.openclassrooms.mdd.services.UserService;
import com.openclassrooms.mdd.mappers.UserMapper;
import com.openclassrooms.mdd.security.services.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "C. Users", description = "Endpoints for user profile and subscriptions")
public class UserController {
    
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    // Retrieve a user's profile
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping("/{id}/subscribe/{topicId}")
    public ResponseEntity<Void> subscribe(@PathVariable Long id, @PathVariable Long topicId) {
        userService.subscribe(id, topicId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/unsubscribe/{topicId}")
    public ResponseEntity<Void> unsubscribe(@PathVariable Long id, @PathVariable Long topicId) {
        userService.unsubscribe(id, topicId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        // Vérifier que l'utilisateur modifie bien son propre profil
        if (!userDetails.getId().equals(id)) {
            throw new AccessDeniedException("You can only update your own profile");
        }

        // Mettre à jour l'utilisateur
        User updatedUser = userService.updateUser(id, request);

        // Retourner les données mises à jour (pas besoin de nouveau token car userId ne change jamais)
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }
}
