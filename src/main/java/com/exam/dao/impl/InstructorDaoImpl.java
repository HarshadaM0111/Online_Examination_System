package com.exam.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.exam.dao.InstructorDao;
import com.exam.entities.Course;
import com.exam.entities.Instructor;
import com.exam.util.HibernateUtil;
import java.util.List;

public class InstructorDaoImpl implements InstructorDao {

    @Override
    public Instructor createInstructor(Instructor instructor) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.save(instructor);
            session.getTransaction().commit();
            return instructor;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public Instructor getInstructorById(int instructorId) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Instructor.class, instructorId);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Instructor> getAllInstructors() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM Instructor", Instructor.class).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateInstructor(Instructor instructor) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.update(instructor);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteInstructor(int instructorId) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Instructor instructor = session.get(Instructor.class, instructorId);
            if (instructor != null) {
                session.delete(instructor);
            }
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Instructor getInstructorByEmailAndPassword(String email, String password) {
        Transaction transaction = null;
        Instructor instructor = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            instructor = (Instructor) session.createQuery("FROM Instructor WHERE email = :email AND password = :password")
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return instructor;
    }

    @Override
    public List<Course> viewAssignedCourses(int instructorId) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery(
                "FROM Course WHERE instructor.instructorId = :instructorId", Course.class)
                .setParameter("instructorId", instructorId)
                .list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

	@Override
	public Instructor getInstructorByName(String name) {
	    try (Session session = HibernateUtil.getSession()) {
	        return session.createQuery(
	            "FROM Instructor WHERE name = :name", Instructor.class)
	            .setParameter("name", name)
	            .uniqueResult();
	    } catch (HibernateException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	

}
