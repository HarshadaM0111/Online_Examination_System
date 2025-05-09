package com.exam.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.exam.dao.CourseDao;
import com.exam.entities.Course;
import com.exam.util.HibernateUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CourseDaoImpl implements CourseDao {

    @Override
    public Course createCourse(Course course) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.save(course);
            session.getTransaction().commit();
            return course;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Course getCourseById(int courseId) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Course.class, courseId);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Course> getAllCourses() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM Course", Course.class).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Course> getCoursesByInstructorId(int instructorId) {
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
    public void updateCourse(Course course) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.update(course);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCourse(int courseId) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Course course = session.get(Course.class, courseId);
            if (course != null) {
                session.delete(course);
            }
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
    
    // New method to search courses by name
    @Override
    public List<Course> searchCoursesByName(String name) {
        try (Session session = HibernateUtil.getSession()) {
            // Use LIKE to search for courses with a name matching the provided string
            String queryStr = "FROM Course WHERE courseName LIKE :name";
            Query<Course> query = session.createQuery(queryStr, Course.class);
            query.setParameter("name", "%" + name + "%");  // % wildcard on both sides for partial matching
            return query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Course> getCoursesByStudentId(int studentId) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "SELECT c FROM Student s JOIN s.enrolledCourses c WHERE s.studentId = :studentId";
            Query<Course> query = session.createQuery(hql, Course.class);
            query.setParameter("studentId", studentId);
            return query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    
}
