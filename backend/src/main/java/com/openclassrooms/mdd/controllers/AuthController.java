package com.openclassrooms.mdd.controllers;

import com.openclassrooms.mdd.dto.request.LoginRequest;
import com.openclassrooms.mdd.dto.request.RegisterRequest;
import com.openclassrooms.mdd.dto.response.JwtResponse;
import com.openclassrooms.mdd.dto.response.MessageResponse;
import com.openclassrooms.mdd.models.User;
import com.openclassrooms.mdd.repository.UserRepository;
import com.openclassrooms.mdd.security.services.CustomUserDetails;
import com.openclassrooms.mdd.security.jwt.JwtUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "A. Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    // 1. LOGIN
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // A. The manager is asked to authenticate the user.
        // This will automatically call UserDetailsServiceImpl.loadUserByUsername.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.emailOrUsername(), loginRequest.password()));

        // B. If it passes, we put the auth in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // C. Generate the JWT
        String jwt = jwtUtils.generateJwtToken(authentication);

        // D. We retrieve the logged-in user's information from CustomUserDetails
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // E. Get username from User entity
        User user = userRepository.findByEmail(userDetails.getEmail()).orElseThrow();

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                user.getUsername(),
                userDetails.getEmail()));
    }

    // 2. REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        // A. Check if the email or username already exists
        if (userRepository.existsByUsername(signUpRequest.username())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.email())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // B. Create the new user
        User user = User.builder()
                .username(signUpRequest.username())
                .email(signUpRequest.email())
                .password(encoder.encode(signUpRequest.password())) // Always encrypt
                .build();

        // C. Save
        userRepository.save(user);

        // D. Authenticate the user automatically
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signUpRequest.email(), signUpRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // E. Generate JWT token
        String jwt = jwtUtils.generateJwtToken(authentication);

        // F. Return token like login endpoint (UX improvement)
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                user.getUsername(),
                userDetails.getEmail()));
    }
}
