package com.openclassrooms.mdd.services;

import com.openclassrooms.mdd.models.Comment;
import com.openclassrooms.mdd.models.Post;
import com.openclassrooms.mdd.models.User;
import com.openclassrooms.mdd.repository.CommentRepository;
import com.openclassrooms.mdd.repository.PostRepository;
import com.openclassrooms.mdd.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // Retrieve comments from an article
    public List<Comment> findAllByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    // Create a comment
    public Comment create(Long postId, String username, String content) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));

        Comment comment = Comment.builder()
                .content(content)
                .author(user)
                .post(post)
                .build();

        return commentRepository.save(comment);
    }
}
