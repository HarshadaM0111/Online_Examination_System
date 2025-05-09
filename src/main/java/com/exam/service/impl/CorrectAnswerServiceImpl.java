package com.exam.service.impl;

import com.exam.dao.CorrectAnswerDao;
import com.exam.entities.CorrectAnswer;
import com.exam.service.CorrectAnswerService;
import java.util.List;

public class CorrectAnswerServiceImpl implements CorrectAnswerService {
    
    private CorrectAnswerDao correctAnswerDao;

    // Constructor injection or setter injection to initialize the DAO
    public CorrectAnswerServiceImpl(CorrectAnswerDao correctAnswerDao) {
        this.correctAnswerDao = correctAnswerDao;
    }

    @Override
    public CorrectAnswer createCorrectAnswer(CorrectAnswer correctAnswer) {
        return correctAnswerDao.createCorrectAnswer(correctAnswer);
    }

    @Override
    public CorrectAnswer getCorrectAnswerByQuestionId(int questionId) {
        return correctAnswerDao.getCorrectAnswerByQuestionId(questionId);
    }

    @Override
    public List<CorrectAnswer> getAllCorrectAnswers() {
        return correctAnswerDao.getAllCorrectAnswers();
    }

    @Override
    public void updateCorrectAnswer(CorrectAnswer correctAnswer) {
        correctAnswerDao.updateCorrectAnswer(correctAnswer);
    }

    @Override
    public void deleteCorrectAnswer(int answerId) {
        correctAnswerDao.deleteCorrectAnswer(answerId);
    }
}
