package com.exam.service;

import com.exam.entities.CorrectAnswer;
import java.util.List;

public interface CorrectAnswerService {
    CorrectAnswer createCorrectAnswer(CorrectAnswer correctAnswer);
    CorrectAnswer getCorrectAnswerByQuestionId(int questionId);
    List<CorrectAnswer> getAllCorrectAnswers();
    void updateCorrectAnswer(CorrectAnswer correctAnswer);
    void deleteCorrectAnswer(int answerId);
}
