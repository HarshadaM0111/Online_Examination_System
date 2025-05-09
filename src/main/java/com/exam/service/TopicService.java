package com.exam.service;

import com.exam.entities.Topic;
import java.util.List;

public interface TopicService {
    Topic createTopic(Topic topic);
    Topic getTopicById(int topicId);
    List<Topic> getAllTopics();
    void updateTopic(Topic topic);
    void deleteTopic(int topicId);
    
    // Get a topic by its name (New method)
    Topic getTopicByName(String topicName); 
}
