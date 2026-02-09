package com.openclassrooms.mdd.controllers;

import com.openclassrooms.mdd.models.Topic;
import com.openclassrooms.mdd.dto.response.TopicDto;
import com.openclassrooms.mdd.mappers.TopicMapper;
import com.openclassrooms.mdd.services.TopicService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
@Tag(name = "B. Topics", description = "Endpoints for managing topics")
public class TopicController {
    
    private final TopicService topicService;
    private final TopicMapper topicMapper;

    public TopicController(TopicService topicService, TopicMapper topicMapper) {
        this.topicService = topicService;
        this.topicMapper = topicMapper;
    }

    @GetMapping
    public ResponseEntity<List<TopicDto>> getAllTopics() {
        // 1. Retrieve entities via the service
        List<Topic> topics = topicService.findAll();

        // 2. Convert to DTO via the mapper
        List<TopicDto> topicDtos = topicMapper.toDtos(topics);
        return ResponseEntity.ok(topicDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicDto> getTopicById(@PathVariable Long id) {
        // 1. Retrieve entity via the service
        Topic topic = topicService.findById(id);
        
        // 2. Convert to DTO via the mapper
        TopicDto topicDto = topicMapper.toDto(topic);
        return ResponseEntity.ok(topicDto);
    }
}
