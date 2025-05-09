package com.exam.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.exam.dao.QuestionDao;
import com.exam.entities.Exam;
import com.exam.entities.Question;
import com.exam.util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class QuestionDaoImpl implements QuestionDao {

    private static final Logger logger = LoggerFactory.getLogger(QuestionDaoImpl.class);
    private SessionFactory sessionFactory;

    public QuestionDaoImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();  // Get the session factory from HibernateUtil
   }
    
    public QuestionDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Method to save a question to the database
    @Override
    public Question createQuestion(Question question) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();  // Open a session
            transaction = session.beginTransaction();  // Begin transaction
            session.save(question);  // Save the question entity
            transaction.commit();  // Commit transaction
            logger.info("Question saved successfully with ID: {}", question.getQuestionId());
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();  // Rollback on error
            }
            logger.error("Error while saving question: {}", e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();  // Close the session
            }
        }
		return question;
    }

    @Override
    public Question getQuestionById(int questionId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Question.class, questionId);
        } catch (HibernateException e) {
            logger.error("Error while fetching question by ID: {}", e.getMessage());
        }
        return null;
    }

//    @Override
//    public List<Question> getAllQuestions() {
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            return session.createQuery("FROM Question", Question.class).list();
//        } catch (HibernateException e) {
//            logger.error("Error while fetching all questions: {}", e.getMessage());
//        }
//        return new ArrayList<>();  // Return an empty list instead of null
//    }
    
    @Override
    public List<Question> getAllQuestions() {
        Session session = sessionFactory.openSession();
        List<Question> questions = null;
        try {
            session.beginTransaction();
            Query<Question> query = session.createQuery("from Question", Question.class);
            questions = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return questions;
    }

    @Override
    public void updateQuestion(Question question) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(question);
            transaction.commit();
            logger.info("Question with ID {} updated successfully", question.getQuestionId());
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error while updating question: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void deleteQuestion(int questionId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Question question = session.get(Question.class, questionId);
            if (question != null) {
                session.delete(question);
                logger.info("Question with ID {} deleted successfully", questionId);
            }
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error while deleting question: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Question> getQuestionsByExam(Exam exam) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Question q WHERE q.exam = :exam", Question.class)
                          .setParameter("exam", exam)
                          .getResultList();
        } catch (HibernateException e) {
            logger.error("Error while fetching questions for exam {}: {}", exam.getTitle(), e.getMessage());
        }
        return new ArrayList<>();  // Return an empty list instead of null
    }
    
//    @Override
//    public Question getQuestionByIdWithOptions(int questionId) {
//        Session session = null;
//        Question question = null;
//        try {
//            session = sessionFactory.openSession();
//            String hql = "SELECT q FROM Question q LEFT JOIN FETCH q.options WHERE q.questionId = :id";
//            Query<Question> query = session.createQuery(hql, Question.class);
//            query.setParameter("id", questionId);
//            question = query.uniqueResult();
//        } catch (HibernateException e) {
//            logger.error("Error while fetching question with options by ID: {}", e.getMessage());
//            e.printStackTrace();
//        } finally {
//            if (session != null) {
//                session.close();
//            }
//        }
//        return question;
//    }
    
    public Question getQuestionByIdWithOptions(int questionId) {
        Session session = sessionFactory.openSession();
        Question question = null;
        try {
            session.beginTransaction();
            // Important: Fetch Question with Options eagerly
            question = session.createQuery(
                    "SELECT q FROM Question q LEFT JOIN FETCH q.options WHERE q.questionId = :questionId", 
                    Question.class)
                .setParameter("questionId", questionId)
                .uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return question;
    }


}
