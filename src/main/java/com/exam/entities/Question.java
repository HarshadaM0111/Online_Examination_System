package com.exam.entities;

import java.util.List;
import javax.persistence.*;

// This class represents a "Question" entity in the exam system
@Entity // Marks this class as an entity that can be persisted in a database
@Table(name = "questions") // Specifies the table name in the database where questions are stored
public class Question {

    // Primary key for the Question entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generates and increments the question ID
    @Column(name = "questionId") // Specifies the name of the column in the table
    private int questionId;

    // The text of the question
    @Column(nullable = false) // This field is required (cannot be null)
    private String questionText;

    // The type of the question (e.g., multiple choice, true/false, etc.)
    @Column(nullable = false) // This field is required (cannot be null)
    private String questionType;

    // Option A for the multiple choice question
    @Column(name = "option_a") // Specifies the column name for option A
    private String optionA;

    // Option B for the multiple choice question
    @Column(name = "option_b") // Specifies the column name for option B
    private String optionB;

    // Option C for the multiple choice question
    @Column(name = "option_c") // Specifies the column name for option C
    private String optionC;

    // Option D for the multiple choice question
    @Column(name = "option_d") // Specifies the column name for option D
    private String optionD;

    // The correct answer for the question in text form
    @Column(nullable = false) // This field is required (cannot be null)
    private String correctAnswerText;

    // Relationships (Foreign Keys)

    // Many questions belong to one exam (foreign key relationship)
    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false) // Foreign key to the "exam" table
    private Exam exam;

    // Many questions may belong to one topic (optional relationship)
    @ManyToOne
    @JoinColumn(name = "topic_id") // Foreign key to the "topic" table (nullable)
    private Topic topic;

    // One-to-one relationship with the CorrectAnswer entity
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "correct_answer_id") // Foreign key to the "correct_answer" table
    private CorrectAnswer correctAnswer;
    
    // One-to-many relationship with the Option entity
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY) // One question can have multiple options
    private List<Option> options;

    // Default constructor required by JPA (Java Persistence API)
    public Question() {}

    // Constructor to initialize a Question with text, type, options, and exam details
    public Question(String questionText, String questionType, String optionA, String optionB,
                    String optionC, String optionD, String correctAnswerText, Exam exam, Topic topic) {
        this.questionText = questionText;
        this.questionType = questionType;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswerText = correctAnswerText;
        this.exam = exam;
        this.topic = topic;
    }

    // Getters and Setters for each field

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectAnswerText() {
        return correctAnswerText;
    }

    public void setCorrectAnswerText(String correctAnswerText) {
        this.correctAnswerText = correctAnswerText;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public CorrectAnswer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(CorrectAnswer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<Option> getOptions() {
        return options; // Returns the list of options for this question
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    // Override the toString method to print a readable representation of the Question
    @Override
    public String toString() {
        return "Question{" +
                "questionId=" + questionId +
                ", questionText='" + questionText + '\'' +
                ", exam=" + (exam != null ? exam.getExamId() : "null") + // Display exam ID if exam exists
                ", topic=" + (topic != null ? topic.getTopicId() : "null") + // Display topic ID if topic exists
                '}';
    }
}
