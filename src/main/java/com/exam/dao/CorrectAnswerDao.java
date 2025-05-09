package com.exam.dao;

import com.exam.entities.CorrectAnswer;
import java.util.List;

public interface CorrectAnswerDao {

    // This method is used to create and save a new correct answer in the database.
    CorrectAnswer createCorrectAnswer(CorrectAnswer correctAnswer);

    // If you need to find the correct answer for a specific question, this method will return it.
    CorrectAnswer getCorrectAnswerByQuestionId(int questionId);

    // This will fetch all the correct answers from the database, whether you need to view them all or do something else with the data.
    List<CorrectAnswer> getAllCorrectAnswers();

    // Want to update an existing correct answer? This method allows you to modify the answer.
    void updateCorrectAnswer(CorrectAnswer correctAnswer);

    // If an answer needs to be deleted, you can call this method to remove it from the database.
    void deleteCorrectAnswer(int answerId);
}
