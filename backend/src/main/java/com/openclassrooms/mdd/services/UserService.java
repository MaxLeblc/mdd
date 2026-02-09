package com.openclassrooms.mdd.services;

import com.openclassrooms.mdd.models.User;
import com.openclassrooms.mdd.models.Topic;
import com.openclassrooms.mdd.repository.UserRepository;
import com.openclassrooms.mdd.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TopicRepository topicRepository;

    public UserService(UserRepository userRepository, TopicRepository topicRepository) {
        this.userRepository = userRepository;
        this.topicRepository = topicRepository; 
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
}
