package com.exam.dao;

import com.exam.entities.Exam;
import java.util.List;

public interface ExamDao {

    // Creates and saves a new exam in the system.
    Exam createExam(Exam exam);

    // Retrieves an exam by its unique ID. This is necessary for managing exams.
    Exam getExamById(int examId);  // ← Required for Eclipse

    // Retrieves an exam by its title, useful for finding exams by name.
    Exam getExamByTitle(String title);

    // Retrieves an exam by its ID along with the associated questions.
    // This helps avoid LazyInitializationException when trying to access questions.
    Exam getExamByIdWithQuestions(int examId); // ← For solving LazyInitializationException

    // Retrieves a list of all exams in the system.
    List<Exam> getAllExams();

    // Allows updating details of an existing exam (e.g., title, date, etc.).
    void updateExam(Exam exam);

    // Deletes an exam by its ID from the system.
    void deleteExam(int examId);

    // Retrieves a list of exams that use a specific proctoring type.
    List<Exam> getExamsByProctoring(String proctoringType);

    // Retrieves a list of exams that match a specific exam type (e.g., written, practical).
    List<Exam> getExamsByExamType(String examType);

    // Retrieves a list of exams associated with a particular admin by their ID.
    List<Exam> getExamsByAdminId(int adminId);

    // Retrieves a list of exams associated with a specific course.
    List<Exam> getExamsByCourse(int courseId);

    // Retrieves a list of exams by the course's name.
    List<Exam> getExamsByCourseName(String courseName);

    // Retrieves a list of exams based on the course's unique code.
    List<Exam> getExamsByCourseId(String courseCode);

}
