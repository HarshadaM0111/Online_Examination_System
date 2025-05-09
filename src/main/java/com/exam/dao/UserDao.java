package com.exam.dao;

import com.exam.entities.Admin;
import com.exam.entities.Student;
import com.exam.entities.Instructor;
import com.exam.entities.User;
import com.exam.util.HibernateUtil;
import org.hibernate.Session;

public class UserDao {

    /**
     * Authenticate a user based on email and password.
     * The method checks if the user is an Admin, Student, or Instructor.
     *
     * @param username The user's email (used as username)
     * @param password The user's password
     * @return A User object if credentials match any role, otherwise null
     */
    public User login(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Try logging in as Admin
            Admin admin = session.createQuery(
                "FROM Admin WHERE email = :email AND password = :pass", Admin.class)
                .setParameter("email", username)
                .setParameter("pass", password)
                .uniqueResult();

            if (admin != null) {
                return new User(admin.getEmail(), "admin");  // Return user with admin role
            }

            // Try logging in as Student
            Student student = session.createQuery(
                "FROM Student WHERE email = :email AND password = :pass", Student.class)
                .setParameter("email", username)
                .setParameter("pass", password)
                .uniqueResult();

            if (student != null) {
                return new User(student.getEmail(), "student");  // Return user with student role
            }

            // Try logging in as Instructor
            Instructor instructor = session.createQuery(
                "FROM Instructor WHERE email = :email AND password = :pass", Instructor.class)
                .setParameter("email", username)
                .setParameter("pass", password)
                .uniqueResult();

            if (instructor != null) {
                return new User(instructor.getEmail(), "instructor");  // Return user with instructor role
            }

        } catch (Exception e) {
            e.printStackTrace();  // Log the exception (ideally use a logger in production)
        }

        return null;  // Return null if login fails for all roles
    }
}
