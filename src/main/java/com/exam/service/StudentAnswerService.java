package com.exam.service;

import com.exam.entities.StudentAnswer;
import java.util.List;

public interface StudentAnswerService {

    StudentAnswer createStudentAnswer(StudentAnswer studentAnswer);

    StudentAnswer getStudentAnswerById(int answerId);

    List<StudentAnswer> getStudentAnswersByStudentId(int studentId);

    void updateStudentAnswer(StudentAnswer studentAnswer);

    void deleteStudentAnswer(int answerId);
}
