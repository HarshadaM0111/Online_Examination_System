package com.exam.dao;

import com.exam.entities.Student;
import com.exam.entities.StudentAnswer;
import java.util.List;

public interface StudentAnswerDao {

    // Save a new student answer to the database (e.g., during or after an exam submission).
    StudentAnswer createStudentAnswer(StudentAnswer studentAnswer);

    // Retrieve a specific student answer by its unique ID.
    StudentAnswer getStudentAnswerById(int answerId);

    // Get all answers submitted by a particular student.
    List<StudentAnswer> getStudentAnswersByStudentId(int studentId);

    // Update an existing student answer (e.g., if there's a correction or manual grading involved).
    void updateStudentAnswer(StudentAnswer studentAnswer);

    // Delete a specific student answer by ID (e.g., if it was submitted in error or needs to be reset).
    void deleteStudentAnswer(int answerId);

    // Save or update a student answer (used in scenarios where answer might already exist).
    StudentAnswer saveStudentAnswer(StudentAnswer answer);

    // Retrieve all answers given by a specific student entity (more object-oriented variant).
    List<StudentAnswer> getAnswersByStudent(Student student);

    // Delete all answers submitted by a particular student (e.g., when resetting their exam).
    void deleteAnswersByStudent(Student student);
}
