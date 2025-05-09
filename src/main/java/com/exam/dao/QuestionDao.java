package com.exam.dao;

import com.exam.entities.Exam;
import com.exam.entities.Question;
import java.util.List;

public interface QuestionDao {

    // Creates and saves a new question to the database.
    Question createQuestion(Question question);

    // Retrieves a question by its unique ID, used for managing individual questions.
    Question getQuestionById(int questionId);

    // Retrieves a question by its unique ID along with its available options.
    // This is useful when you need the options alongside the question text.
    Question getQuestionByIdWithOptions(int questionId);

    // Retrieves all questions stored in the system, useful for listings or admin views.
    List<Question> getAllQuestions();

    // Updates the details of an existing question (e.g., text, options).
    void updateQuestion(Question question);

    // Deletes a question from the system by its ID, typically for cleanup or removal.
    void deleteQuestion(int questionId);

    // Retrieves a list of questions that belong to a specific exam.
    // Useful for getting all the questions related to a specific exam.
    List<Question> getQuestionsByExam(Exam exam);

}
