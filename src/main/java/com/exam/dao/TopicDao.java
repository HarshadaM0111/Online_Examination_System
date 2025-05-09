package com.exam.dao;

import com.exam.entities.Topic;
import java.util.List;

public interface TopicDao {

    // Create and save a new topic in the system (e.g., "Algebra", "Java Basics").
    Topic createTopic(Topic topic);

    // Retrieve a topic using its unique ID.
    Topic getTopicById(int topicId);

    // Fetch all topics available in the system.
    List<Topic> getAllTopics();

    // Update an existing topic’s details (such as name or description).
    void updateTopic(Topic topic);

    // Delete a topic from the system based on its ID.
    void deleteTopic(int topicId);

    // Retrieve a topic using its name (useful for duplicate checks or lookups).
    Topic getTopicByName(String topicName);
}
