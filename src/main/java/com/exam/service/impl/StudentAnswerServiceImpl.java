package com.exam.service.impl;

import com.exam.dao.StudentAnswerDao;
import com.exam.dao.impl.StudentAnswerDaoImpl;
import com.exam.entities.StudentAnswer;
import com.exam.service.StudentAnswerService;
import java.util.List;

public class StudentAnswerServiceImpl implements StudentAnswerService {

    private StudentAnswerDao studentAnswerDao;

    public StudentAnswerServiceImpl() {
        this.studentAnswerDao = new StudentAnswerDaoImpl();
    }

    @Override
    public StudentAnswer createStudentAnswer(StudentAnswer studentAnswer) {
        return studentAnswerDao.createStudentAnswer(studentAnswer);
    }

    @Override
    public StudentAnswer getStudentAnswerById(int answerId) {
        return studentAnswerDao.getStudentAnswerById(answerId);
    }

    @Override
    public List<StudentAnswer> getStudentAnswersByStudentId(int studentId) {
        return studentAnswerDao.getStudentAnswersByStudentId(studentId);
    }

    @Override
    public void updateStudentAnswer(StudentAnswer studentAnswer) {
        studentAnswerDao.updateStudentAnswer(studentAnswer);
    }

    @Override
    public void deleteStudentAnswer(int answerId) {
        studentAnswerDao.deleteStudentAnswer(answerId);
    }
}
