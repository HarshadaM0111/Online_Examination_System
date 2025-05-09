package com.exam.service.impl;

import com.exam.dao.QuestionDao;
import com.exam.entities.Exam;
import com.exam.entities.Question;
import com.exam.service.QuestionService;
import com.exam.dao.impl.QuestionDaoImpl;
import com.exam.service.ExamService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class QuestionServiceImpl implements QuestionService {

    private QuestionDao questionDao = new QuestionDaoImpl();
    private ExamService examService;  // Reference to the ExamService to fetch exam by title

    private SessionFactory sessionFactory;  // Hibernate Session Factory
    
    public QuestionServiceImpl(ExamService examService, SessionFactory sessionFactory) {
        if (examService == null) {
            throw new IllegalArgumentException("ExamService cannot be null");
        }
        if (sessionFactory == null) {
            throw new IllegalArgumentException("SessionFactory cannot be null");
        }
        this.examService = examService;
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Question createQuestion(Question question) {
        return questionDao.createQuestion(question);
    }

    @Override
    public Question getQuestionById(int questionId) {
        return questionDao.getQuestionById(questionId);
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionDao.getAllQuestions();
    }

    @Override
    public void updateQuestion(Question question) {
        questionDao.updateQuestion(question);
    }

    @Override
    public void deleteQuestion(int questionId) {
        questionDao.deleteQuestion(questionId);
    }

    // New method to get questions by Exam Title
    public List<Question> getQuestionsByExamTitle(String title) {
        // Fetch Exam object by Title
        Exam exam = examService.getExamByTitle(title);

        // If the exam is found, fetch all associated questions
        if (exam != null) {
            return questionDao.getQuestionsByExam(exam);
        } else {
            throw new IllegalArgumentException("Exam with title " + title + " not found.");
        }
    }

    
    @Override
    public List<Question> getQuestionsByExam(Exam exam) {
        return questionDao.getQuestionsByExam(exam);
    }

    public Question getQuestionByIdWithOptions(int questionId) {
        return questionDao.getQuestionByIdWithOptions(questionId);
    }


}
