package com.openclassrooms.mdd.controllers;

import com.openclassrooms.mdd.dto.request.CommentCreateDto;
import com.openclassrooms.mdd.dto.response.CommentDto;
import com.openclassrooms.mdd.mappers.CommentMapper;
import com.openclassrooms.mdd.models.Comment;
import com.openclassrooms.mdd.services.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "E. Comments", description = "Gestion des commentaires")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    public CommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    // GET : View comments on an article
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByPost(@PathVariable Long postId) {
        List<Comment> comments = commentService.findAllByPostId(postId);
        return ResponseEntity.ok(commentMapper.toDtos(comments));
    }

    // POST : Add a comment
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateDto input,
            Principal principal) {
        
        Comment comment = commentService.create(postId, principal.getName(), input.content());
        return ResponseEntity.ok(commentMapper.toDto(comment));
    }
}
