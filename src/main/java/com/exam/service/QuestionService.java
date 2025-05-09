package com.exam.service;

import com.exam.entities.Exam;
import com.exam.entities.Question;
import java.util.List;

public interface QuestionService {
    // Creates a new question
    Question createQuestion(Question question);
    
    // Fetches a question by its ID
    Question getQuestionById(int questionId);
    
    // Retrieves all questions
    List<Question> getAllQuestions();
    
    // Updates an existing question
    void updateQuestion(Question question);
    
    // Deletes a question by its ID
    void deleteQuestion(int questionId);
    
    // Retrieves all questions for a specific exam
    List<Question> getQuestionsByExam(Exam exam);
}
