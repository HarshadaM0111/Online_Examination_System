package com.exam.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

// This class represents a "Result" entity in the exam system, storing information about a student's exam result.
@Entity // Marks this class as a JPA entity that is mapped to a database table
@Table(name = "results") // Specifies the table name in the database where the results are stored
public class Result {

    // Primary key for the Result entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generates and increments the result ID
    private int resultId;

    // Many results belong to one student (foreign key relationship)
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false) // Foreign key to the "student" table (student cannot be null)
    private Student student;

    // Many results belong to one exam (foreign key relationship)
    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false) // Foreign key to the "exam" table (exam cannot be null)
    private Exam exam;

    // The number of correct answers the student got in the exam
    @Column(nullable = false) // This field is required (cannot be null)
    private double score;

    // The date and time when the student attempted the exam
    @Column(name = "attempt_date", nullable = false) // This field is required (cannot be null)
    private LocalDateTime attemptDate;

    // Whether the student passed the exam or not
    @Column(nullable = false) // This field is required (cannot be null)
    private boolean passed;

    // The percentage of correct answers the student achieved
    @Column(name = "percentage", nullable = false) // This field is required (cannot be null)
    private double percentage;

    // Total marks available in the exam, which is equivalent to the number of questions
    @Column(name = "total_marks", nullable = false) // This field is required (cannot be null)
    private Integer totalMarks;

    // Default constructor required by JPA (Java Persistence API)
    public Result() {}

    // Constructor to initialize a Result with student, exam, score, attempt date, pass status, and percentage
    public Result(Student student, Exam exam, double score, LocalDateTime attemptDate, boolean passed, double percentage) {
        this.student = student;
        this.exam = exam;
        this.score = score;
        this.attemptDate = attemptDate;
        this.passed = passed;
        this.percentage = percentage;
    }

    // Getters and setters for each field

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public LocalDateTime getAttemptDate() {
        return attemptDate;
    }

    public void setAttemptDate(LocalDateTime attemptDate) {
        this.attemptDate = attemptDate;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public double getPercentage() {
        // Calculate the percentage based on the score and total number of questions
        if (exam != null && exam.getQuestions() != null && !exam.getQuestions().isEmpty()) {
            int totalMarks = exam.getQuestions().size(); // Total number of questions (marks)
            if (totalMarks > 0 && score <= totalMarks) {
                return (score / totalMarks) * 100.0; // Correct percentage calculation
            } else {
                // If the score exceeds total questions, ensure it doesn't cause overflow
                return 100.0;
            }
        }
        return 0.0; // Return 0 if exam or questions are unavailable
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    // Set totalMarks directly (but this can be auto-calculated based on the number of questions)
    public void setTotalMarks(int totalMarks) {
        // Ensure totalMarks is set to 0 if not calculated correctly
        if (totalMarks == 0) {
            this.totalMarks = 1; // Default value to prevent null issues
        } else {
            this.totalMarks = totalMarks;
        }
    }

    // Calculate the total marks based on the number of questions in the associated exam
    public int getTotalMarks() {
        if (exam != null && exam.getQuestions() != null) {
            return exam.getQuestions().size(); // Calculate based on the number of questions
        }
        return 1; // Default to 1 to prevent null or zero issues
    }

    // Override the toString method to return a string representation of the Result
    @Override
    public String toString() {
        return "Result [resultId=" + resultId +
               ", student=" + student.getStudentId() +
               ", exam=" + exam.getExamId() +
               ", score=" + score +
               ", attemptDate=" + attemptDate +
               ", passed=" + passed +
               ", percentage=" + percentage + "]";
    }
}
