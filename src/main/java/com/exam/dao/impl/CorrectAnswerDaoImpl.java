package com.exam.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import com.exam.dao.CorrectAnswerDao;
import com.exam.entities.CorrectAnswer;
import com.exam.util.HibernateUtil;

import java.util.List;

public class CorrectAnswerDaoImpl implements CorrectAnswerDao {

    @Override
    public CorrectAnswer createCorrectAnswer(CorrectAnswer correctAnswer) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.save(correctAnswer);
            session.getTransaction().commit();
            return correctAnswer;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CorrectAnswer getCorrectAnswerByQuestionId(int questionId) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM CorrectAnswer ca WHERE ca.question.id = :questionId", CorrectAnswer.class)
                    .setParameter("questionId", questionId)
                    .uniqueResult();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CorrectAnswer> getAllCorrectAnswers() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM CorrectAnswer", CorrectAnswer.class).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateCorrectAnswer(CorrectAnswer correctAnswer) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.update(correctAnswer);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCorrectAnswer(int answerId) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            CorrectAnswer correctAnswer = session.get(CorrectAnswer.class, answerId);
            if (correctAnswer != null) {
                session.delete(correctAnswer);
            }
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
}
