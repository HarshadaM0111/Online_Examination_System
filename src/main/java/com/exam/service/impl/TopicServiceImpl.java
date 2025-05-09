package com.exam.service.impl;

import com.exam.dao.TopicDao;
import com.exam.entities.Topic;
import com.exam.service.TopicService;

import com.exam.dao.impl.TopicDaoImpl;

import java.util.List;

public class TopicServiceImpl implements TopicService {

    private TopicDao topicDao = new TopicDaoImpl();

    @Override
    public Topic createTopic(Topic topic) {
        return topicDao.createTopic(topic);
    }

    @Override
    public Topic getTopicById(int topicId) {
        return topicDao.getTopicById(topicId);
    }

    @Override
    public List<Topic> getAllTopics() {
        return topicDao.getAllTopics();
    }

    @Override
    public void updateTopic(Topic topic) {
        topicDao.updateTopic(topic);
    }

    @Override
    public void deleteTopic(int topicId) {
        topicDao.deleteTopic(topicId);
    }
    
    // New method to get a topic by its name
    @Override
    public Topic getTopicByName(String topicName) {
        // You can delegate this task to the DAO layer or implement it here.
        // Assuming your TopicDao has a method to fetch by name (you would need to implement this).
        return topicDao.getTopicByName(topicName);  // Assuming this is implemented in the DAO
    }
}
