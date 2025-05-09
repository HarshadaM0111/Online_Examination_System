package com.exam.dao;

import com.exam.entities.Student;
import java.util.List;

public interface StudentDao {

    // Add a new student to the system (e.g., during registration).
    Student createStudent(Student student);

    // Retrieve a specific student by their unique ID.
    Student getStudentById(int studentId);

    // Fetch a list of all registered students.
    List<Student> getAllStudents();

    // Get all students enrolled in a specific course by course ID.
    List<Student> getStudentsByCourseId(int courseId);

    // Get all students who have taken or are associated with a specific exam.
    List<Student> getStudentsByExamId(int examId);

    // Update student details such as name, email, or password.
    void updateStudent(Student student);

    // Remove a student from the system (typically a soft delete in production).
    void deleteStudent(int studentId);

    // Retrieve a student by their login credentials (used during login).
    Student getStudentByEmailAndPassword(String email, String password);

    // Remove a student's enrollment from a specific course.
    boolean removeStudentFromCourse(int studentId, int courseId);

    // Enroll a student into a specific course.
    boolean enrollStudentInCourse(int studentId, int courseId);
}
