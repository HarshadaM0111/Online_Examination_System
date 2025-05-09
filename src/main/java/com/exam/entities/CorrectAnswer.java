package com.exam.entities;

import javax.persistence.*;

@Entity // Marks this class as a JPA entity
@Table(name = "correct_answers") // Maps this entity to the "correct_answers" table in the database
public class CorrectAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID using the database identity column
    private int id;

    @Column(nullable = false)
    private String correctOption; // Represents the correct option: A, B, C, or D

    @Column(name = "correct_answer_text", nullable = false)
    private String correctAnswerText; // Stores the actual text of the correct answer

    @OneToOne(mappedBy = "correctAnswer", cascade = CascadeType.ALL, optional = false)
    private Question question; // Links to the associated Question entity; one-to-one relationship

    // Default constructor is required by JPA
    public CorrectAnswer() {}

    // Parameterized constructor to create a correct answer with option and text
    public CorrectAnswer(String correctOption, String correctAnswerText) {
        this.correctOption = correctOption;
        this.correctAnswerText = correctAnswerText;
    }

    // === Getters and Setters === //

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public String getCorrectAnswerText() {
        return correctAnswerText;
    }

    public void setCorrectAnswerText(String correctAnswerText) {
        this.correctAnswerText = correctAnswerText;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    // Useful for debugging and logging purposes
    @Override
    public String toString() {
        return "CorrectAnswer [ID=" + id + ", CorrectOption=" + correctOption + ", CorrectAnswerText=" + correctAnswerText + "]";
    }
}
