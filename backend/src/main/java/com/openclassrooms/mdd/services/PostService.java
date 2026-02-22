package com.openclassrooms.mdd.services;

import java.util.NoSuchElementException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import com.openclassrooms.mdd.models.User;
import com.openclassrooms.mdd.models.Topic;
import com.openclassrooms.mdd.dto.request.PostCreateDto;
import com.openclassrooms.mdd.models.Post;
import com.openclassrooms.mdd.repository.UserRepository;
import com.openclassrooms.mdd.repository.TopicRepository;
import com.openclassrooms.mdd.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, TopicRepository topicRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    public List<Post> findAll(boolean sortByDateDesc) {
        Sort sort = sortByDateDesc
            ? Sort.by("createdAt").descending()
            : Sort.by("createdAt").ascending();

        return postRepository.findAll(sort);
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id: " + id));
    }

    public List<Post> findByUserSubscriptions(String email, boolean sortByDateDesc) {
        // 1. Récupérer l'utilisateur avec ses abonnements
        User user = userRepository.findById(userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found with email: " + email))
                .getId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // 2. Si pas d'abonnements, retourner liste vide
        if (user.getSubscriptions() == null || user.getSubscriptions().isEmpty()) {
            return List.of();
        }

        // 3. Récupérer les posts des topics auxquels l'utilisateur est abonné
        Sort sort = sortByDateDesc
            ? Sort.by("createdAt").descending()
            : Sort.by("createdAt").ascending();

        return postRepository.findByTopicIn(user.getSubscriptions(), sort);
    }

    public Post create(String email, PostCreateDto input) {
        // 1. Find the author
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found with email: " + email));

        // 2. Find the theme
        Topic topic = topicRepository.findById(input.topicId())
                .orElseThrow(() -> new NoSuchElementException("Topic not found with id: " + input.topicId()));

        // 3. Create the post
        Post post = Post.builder()
                .title(input.title())
                .content(input.content())
                .author(user)
                .topic(topic)
                .build();
        // The createdAt date will be automatically managed by @CreatedDate in the entity

        return postRepository.save(post);
    }
}
