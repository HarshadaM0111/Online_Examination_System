package com.exam.dao.impl;

import com.exam.dao.ResultDao;
import com.exam.entities.Result;
import com.exam.util.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ResultDaoImpl implements ResultDao {

    @Override
    public Result saveResult(Result result) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(result);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Result> getResultsByStudentId(int studentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Result r WHERE r.student.studentId = :studentId";
            Query<Result> query = session.createQuery(hql, Result.class);
            query.setParameter("studentId", studentId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Result> getResultsByExamId(int examId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Result r WHERE r.exam.examId = :examId";
            Query<Result> query = session.createQuery(hql, Result.class);
            query.setParameter("examId", examId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void updateResult(Result result) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(result);
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Result getResultByStudentAndExam(int studentId, int examId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Result r WHERE r.student.studentId = :studentId AND r.exam.examId = :examId";
            Query<Result> query = session.createQuery(hql, Result.class);
            query.setParameter("studentId", studentId);
            query.setParameter("examId", examId);
            
            List<Result> results = query.list();
            
            if (results.isEmpty()) {
                return null;
            } else {
                // Optionally: sort or filter to return most recent/highest score if needed
                return results.get(0); // Just return the first result found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
