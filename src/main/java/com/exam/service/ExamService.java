package com.exam.service;

import java.util.List;
import com.exam.entities.Admin;
import com.exam.entities.Exam;
import com.exam.entities.Student;

public interface ExamService {
    // Creates a new exam
    Exam createExam(Exam exam);
    
    // Fetches an exam by its ID
    Exam getExamById(int examId);
    
    // Retrieves all exams
    List<Exam> getAllExams();
    
    // Updates an existing exam
    void updateExam(Exam exam);
    
    // Deletes an exam by its ID
    void deleteExam(int examId);
    
    // Fetches the admin associated with a specific ID
    Admin getAdminById(int adminId);
    
    // Fetches an exam by its title
    Exam getExamByTitle(String title);
    
    void takeExam(Student student, int examId);
    
    List<Exam> getExamsByProctoring(String proctoringType);
    List<Exam> getExamsByExamType(String examType);
    List<Exam> getExamsByAdminId(int adminId);
    List<Exam> getExamsByCourse(int courseId);
    List<Exam> getExamsByCourseName(String courseName);


}
