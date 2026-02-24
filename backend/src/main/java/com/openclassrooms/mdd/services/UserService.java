package com.openclassrooms.mdd.services;

import com.openclassrooms.mdd.models.User;
import com.openclassrooms.mdd.models.Topic;
import com.openclassrooms.mdd.dto.request.UserUpdateRequest;
import com.openclassrooms.mdd.repository.UserRepository;
import com.openclassrooms.mdd.repository.TopicRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, TopicRepository topicRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    }

    // Subscription management
    @Transactional
    public void subscribe(Long userId, Long topicId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException("Topic not found with id: " + topicId));

        if (!user.getSubscriptions().contains(topic)) {
            user.getSubscriptions().add(topic);
            userRepository.save(user);
        }
    }
    
    @Transactional
    public void unsubscribe(Long userId, Long topicId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException("Topic not found with id: " + topicId));

        user.getSubscriptions().remove(topic);
        userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        // Update username if provided
        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            user.setUsername(request.getUsername());
        }

        // Update email if provided
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user.setEmail(request.getEmail());
        }

        // Update password if provided
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userRepository.save(user);
    }
}
