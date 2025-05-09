package com.exam.dao.impl;

import java.util.ArrayList;
import java.util.List;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.exam.dao.StudentDao;
import com.exam.entities.Course;
import com.exam.entities.Student;
import com.exam.util.HibernateUtil;

public class StudentDaoImpl implements StudentDao {

	@Override
	public Student getStudentByEmailAndPassword(String email, String password) {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	    	String hql = "FROM Student s LEFT JOIN FETCH s.enrolledCourses WHERE s.email = :email AND s.password = :password";
	    	Query<Student> query = session.createQuery(hql, Student.class);
	        query.setParameter("email", email);
	        query.setParameter("password", password); // assumes you have password field
	        return query.uniqueResult();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	
    @Override
    public Student createStudent(Student student) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.save(student);
            session.getTransaction().commit();
            return student;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Student getStudentById(int studentId) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Student.class, studentId);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Student> getAllStudents() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM Student", Student.class).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Student> getStudentsByCourseId(int courseId) {
        List<Student> students = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Updated query to use enrolledCourses instead of courses
            String hql = "SELECT s FROM Student s JOIN s.enrolledCourses c WHERE c.courseId = :courseId";
            Query<Student> query = session.createQuery(hql, Student.class);
            query.setParameter("courseId", courseId);
            students = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }


    @Override
    public void updateStudent(Student student) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.update(student);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteStudent(int studentId) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            if (student != null) {
                session.delete(student);
            }
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<Student> getStudentsByExamId(int examId) {
        try (Session session = HibernateUtil.getSession()) {
        	String hql = "SELECT s FROM Student s JOIN s.enrolledCourses c WHERE c.courseId = :courseId";
        			return session.createQuery(hql, Student.class)
                          .setParameter("examId", examId)
                          .list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean removeStudentFromCourse(int studentId, int courseId) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();  // Begin the transaction

            // Load the student and course from the database
            Student student = session.get(Student.class, studentId);  // Get the student by ID
            Course course = session.get(Course.class, courseId);  // Get the course by ID

            if (student != null && course != null) {  // If both student and course exist
                // Remove the course from the student's list of enrolled courses
                student.getEnrolledCourses().remove(course);

                // Remove the student from the course's list of students
                course.getStudents().remove(student);

                // Update both the student and course objects in the database
                session.update(student);  // Update the student entity
                session.update(course);   // Update the course entity
                
                tx.commit();  // Commit the transaction
                return true;  // Return true, meaning the removal was successful
            }

            tx.commit();  // Commit the transaction even if the student/course was not found
        } catch (HibernateException e) {
            e.printStackTrace();  // Print any exceptions
            return false;  // Return false if there was an error
        }
        return false;  // Return false if the student or course was not found
    }

    @Override
    public boolean enrollStudentInCourse(int studentId, int courseId) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();  // Start the transaction

            // Load the student and course from the database
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);

            if (student != null && course != null) {
                // Add the course to the student's enrolled courses
                student.getEnrolledCourses().add(course);

                // Add the student to the course's student list
                course.getStudents().add(student);

                // Update both the student and course objects in the database
                session.update(student);
                session.update(course);

                tx.commit();  // Commit the transaction
                return true;  // Return true if the enrollment is successful
            }

            tx.commit();  // Commit the transaction even if either the student or course was not found
        } catch (HibernateException e) {
            e.printStackTrace();  // Print any exceptions
            return false;  // Return false if there was an error
        }
        return false;  // Return false if either student or course was not found
    }


}