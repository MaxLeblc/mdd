package com.openclassrooms.mdd.repository;

import com.openclassrooms.mdd.models.Post;
import com.openclassrooms.mdd.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"author", "topic"})
    List<Post> findAll(Sort sort);

    @EntityGraph(attributePaths = {"author", "topic"})
    Optional<Post> findById(Long id);

    @EntityGraph(attributePaths = {"author", "topic"})
    List<Post> findByTopicIn(List<Topic> topics, Sort sort);
}
