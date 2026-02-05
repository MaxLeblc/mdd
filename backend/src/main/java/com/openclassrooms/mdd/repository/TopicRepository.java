package com.openclassrooms.mdd.repository;

import com.openclassrooms.mdd.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
