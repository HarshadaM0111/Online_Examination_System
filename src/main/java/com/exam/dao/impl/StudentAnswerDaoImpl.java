package com.exam.dao.impl;

import com.exam.dao.StudentAnswerDao;
import com.exam.entities.Student;
import com.exam.entities.StudentAnswer;
import com.exam.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;

public class StudentAnswerDaoImpl implements StudentAnswerDao {

	 private SessionFactory sessionFactory;
	 
	 public StudentAnswerDaoImpl() {
	        this.sessionFactory = HibernateUtil.getSessionFactory();  // Get the session factory from HibernateUtil
	   }
	 
	 public StudentAnswerDaoImpl(SessionFactory sessionFactory) {
	        this.sessionFactory = sessionFactory;
	    }
	
    @Override
    public StudentAnswer createStudentAnswer(StudentAnswer studentAnswer) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(studentAnswer);
            tx.commit();
            return studentAnswer;  // Return the saved entity
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public StudentAnswer getStudentAnswerById(int answerId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(StudentAnswer.class, answerId);
        } finally {
            session.close();
        }
    }

    @Override
    public List<StudentAnswer> getStudentAnswersByStudentId(int studentId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM StudentAnswer WHERE student.id = :studentId", StudentAnswer.class)
                          .setParameter("studentId", studentId)
                          .list();
        } finally {
            session.close();
        }
    }

    @Override
    public void updateStudentAnswer(StudentAnswer studentAnswer) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(studentAnswer);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteStudentAnswer(int answerId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            StudentAnswer studentAnswer = session.get(StudentAnswer.class, answerId);
            if (studentAnswer != null) {
                session.delete(studentAnswer);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    @Override
    public StudentAnswer saveStudentAnswer(StudentAnswer answer) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(answer);
            transaction.commit();
            return answer;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<StudentAnswer> getAnswersByStudent(Student student) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM StudentAnswer sa WHERE sa.student = :student", StudentAnswer.class)
                          .setParameter("student", student)
                          .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteAnswersByStudent(Student student) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM StudentAnswer sa WHERE sa.student = :student")
                   .setParameter("student", student)
                   .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
