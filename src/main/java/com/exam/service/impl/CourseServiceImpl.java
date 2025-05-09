package com.exam.service.impl;

import com.exam.dao.CourseDao;
import com.exam.entities.Course;
import com.exam.service.CourseService;
import com.exam.util.HibernateUtil;
import com.exam.dao.impl.CourseDaoImpl;

import java.util.List;


import org.hibernate.Session;
import org.hibernate.Transaction;

public class CourseServiceImpl implements CourseService {

    private CourseDao courseDao = new CourseDaoImpl();

    @Override
    public Course createCourse(Course course) {
        return courseDao.createCourse(course);
    }

    @Override
    public Course getCourseById(int courseId) {
        return courseDao.getCourseById(courseId);
    }

    @Override
    public Course getCourseByName(String courseName) {
        for (Course course : getAllCourses()) {
            if (course.getCourseName().equalsIgnoreCase(courseName.trim())) {
                return course;
            }
        }
        return null;
    }

    
    @Override
    public List<Course> getAllCourses() {
        return courseDao.getAllCourses();
    }

//    @Override
//    public void updateCourse(Course course) {
//        courseDao.updateCourse(course);
//    }
    
    @Override
    public void updateCourse(Course course) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(course);  // or session.merge(course);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }


    @Override
    public void deleteCourse(int courseId) {
        courseDao.deleteCourse(courseId);
    }
    
    // Implement the new methods
    @Override
    public List<Course> searchCoursesByName(String name) {
        return courseDao.searchCoursesByName(name);  // Delegating to the DAO
    }

    @Override
    public List<Course> getCoursesByInstructorId(int instructorId) {
        return courseDao.getCoursesByInstructorId(instructorId);  // Delegating to the DAO
    }
}

