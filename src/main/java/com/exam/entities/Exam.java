package com.exam.entities;

import java.util.List;
import javax.persistence.*;
import org.hibernate.annotations.BatchSize;

@Entity // Marks this class as a JPA entity to map to a database table
@Table(name = "exams") // Maps this entity to the "exams" table
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment the primary key for each exam
    private int examId;

    @Column(nullable = false) // Title of the exam is mandatory
    private String title;

    @Column(nullable = false) // Duration of the exam in minutes
    private int duration;

    @Column(nullable = false) // Total number of questions in the exam
    private int numQuestions;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false) // Many exams can be managed by one admin
    private Admin admin;

    @OneToMany(mappedBy = "exam", fetch = FetchType.EAGER) // An exam can have many questions; eager loading for questions
    @BatchSize(size = 10) // Optimizes the number of questions loaded per batch
    private List<Question> questions;

    @ManyToMany
    @JoinTable(
        name = "student_exams", // Join table for many-to-many relationship between exams and students
        joinColumns = @JoinColumn(name = "exam_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;

    @Column(nullable = true) // Type of the exam (e.g., "MCQ", "Essay")
    private String examType;

    @Column(nullable = true) // Scheduled time for the exam
    private String scheduledTime;

    @Column(nullable = true) // Proctoring method used for the exam (e.g., "AI", "Live Proctor")
    private String proctoring;

    @Column(length = 1000) // Instructions for the exam, can be long text
    private String instructions;

    // NEW FIELDS - Additional properties
    @Column(nullable = true) // Course associated with the exam (e.g., "CS101")
    private String course;

    @Column(nullable = true) // Mode of the exam (e.g., "Open Book", "Closed Book")
    private String mode;

    @Column(nullable = true) // Passing marks required for the exam (percentage)
    private Integer passingMarks;

    @Column(nullable = true) // Whether the exam has auto-grading enabled
    private Boolean autoGrading;

    // === Constructors ===

    public Exam() {
        // No-arg constructor for JPA
    }

    // Main constructor to create an Exam object with essential details
    public Exam(String title, int duration, int numQuestions, String examType,
                String scheduledTime, String proctoring, String instructions) {
        this.title = title;
        this.duration = duration;
        this.numQuestions = numQuestions;
        this.examType = examType;
        this.scheduledTime = scheduledTime;
        this.proctoring = proctoring;
        this.instructions = instructions;
    }

    // === Getters and Setters ===

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public void setNumQuestions(int numQuestions) {
        this.numQuestions = numQuestions;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getProctoring() {
        return proctoring;
    }

    public void setProctoring(String proctoring) {
        this.proctoring = proctoring;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    // === Getters and Setters for New Fields ===

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getPassingMarks() {
        return passingMarks;
    }

    public void setPassingMarks(Integer passingMarks) {
        this.passingMarks = passingMarks;
    }

    public Boolean getAutoGrading() {
        return autoGrading;
    }

    public void setAutoGrading(Boolean autoGrading) {
        this.autoGrading = autoGrading;
    }

    // This method ensures that null values in autoGrading are handled to prevent exceptions
    public boolean isAutoGrading() {
        return autoGrading != null ? autoGrading : false;
    }

    // This method calculates the total marks based on the number of questions
    public int getTotalMarks() {
        if (questions != null) {
            return questions.size(); // Assuming each question is worth 1 mark
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Exam [examId=" + examId + ", title=" + title + ", duration=" + duration
                + ", numQuestions=" + numQuestions + ", admin=" + (admin != null ? admin.getAdminId() : "null")
                + ", examType=" + examType + ", scheduledTime=" + scheduledTime
                + ", proctoring=" + proctoring + ", course=" + course + ", mode=" + mode
                + ", passingMarks=" + passingMarks + ", autoGrading=" + autoGrading
                + ", instructions=" + instructions + "]";
    }
}
