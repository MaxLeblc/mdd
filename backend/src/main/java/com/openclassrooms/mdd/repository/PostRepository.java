package com.openclassrooms.mdd.repository;

import com.openclassrooms.mdd.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
