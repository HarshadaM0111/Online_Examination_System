package com.exam.dao;

import com.exam.entities.Result;
import java.util.List;

public interface ResultDao {

    // Saves a new result into the system (e.g., after an exam is completed).
    Result saveResult(Result result);

    // Retrieves all results associated with a particular student, used for displaying their performance across exams.
    List<Result> getResultsByStudentId(int studentId);

    // Retrieves all results associated with a particular exam, useful for exam analysis or grading reports.
    List<Result> getResultsByExamId(int examId);

    // Updates an existing result, for instance, when modifying the score or status after review.
    void updateResult(Result result);

    // Retrieves the result of a specific student for a particular exam, useful for showing their specific performance.
    Result getResultByStudentAndExam(int studentId, int examId);
}
