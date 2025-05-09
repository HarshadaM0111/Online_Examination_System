package com.exam.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.exam.dao.TopicDao;
import com.exam.entities.Topic;
import com.exam.util.HibernateUtil;
import java.util.List;

public class TopicDaoImpl implements TopicDao {

    @Override
    public Topic createTopic(Topic topic) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.save(topic);
            session.getTransaction().commit();
            return topic;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Topic getTopicById(int topicId) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Topic.class, topicId);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Topic> getAllTopics() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM Topic", Topic.class).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateTopic(Topic topic) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.update(topic);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTopic(int topicId) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Topic topic = session.get(Topic.class, topicId);
            if (topic != null) {
                session.delete(topic);
            }
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
    
    // New method to get a topic by its name
    @Override
    public Topic getTopicByName(String topicName) {
        try (Session session = HibernateUtil.getSession()) {
            // Use HQL query to search for topic by name
            String hql = "FROM Topic t WHERE t.topicName = :topicName";
            Query<Topic> query = session.createQuery(hql, Topic.class);
            query.setParameter("topicName", topicName);
            return query.uniqueResult();  // Returns a single result (null if not found)
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }
}
