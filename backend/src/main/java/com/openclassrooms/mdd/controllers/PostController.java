package com.openclassrooms.mdd.controllers;

import com.openclassrooms.mdd.mappers.PostMapper;
import com.openclassrooms.mdd.models.Post;
import com.openclassrooms.mdd.dto.request.PostCreateDto;
import com.openclassrooms.mdd.dto.response.PostDto;
import com.openclassrooms.mdd.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "D. Posts", description = "Endpoints for managing posts")
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    public PostController(PostService postService, PostMapper postMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(@RequestParam(defaultValue = "true") boolean sort) { // Optional parameter for sorting (?sort=desc by default)
        List<Post> posts = postService.findAll(sort);
        return ResponseEntity.ok(postMapper.toDtos(posts));
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostDto>> getFeed(@RequestParam(defaultValue = "true") boolean sort, Principal principal) {
        // Récupère uniquement les posts des topics auxquels l'utilisateur est abonné
        // principal.getName() returns the username contained in the JWT token
        List<Post> posts = postService.findByUserSubscriptions(principal.getName(), sort);
        return ResponseEntity.ok(postMapper.toDtos(posts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        Post post = postService.findById(id);
        return ResponseEntity.ok(postMapper.toDto(post));
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostCreateDto input, Principal principal) {
        // principal.getName() returns the username contained in the JWT token
        Post createdPost = postService.create(principal.getName(), input);
        return ResponseEntity.ok(postMapper.toDto(createdPost));
    }
}
