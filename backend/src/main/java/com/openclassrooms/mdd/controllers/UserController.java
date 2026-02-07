package com.openclassrooms.mdd.controllers;

import com.openclassrooms.mdd.models.User;
import com.openclassrooms.mdd.dto.response.UserDto;
import com.openclassrooms.mdd.services.UserService;
import com.openclassrooms.mdd.mappers.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
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
}
