package com.exam.entities;

import java.util.List;
import javax.persistence.*;

// This is the Option entity class which represents a possible answer choice for a question
@Entity // Marks this class as an entity to be persisted in a database
@Table(name = "options") // Specifies the table name in the database where options are stored
public class Option {

    // Primary key for the Option entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically increments the ID value for each option
    private int optionId;

    // Text of the option (e.g., "Option A", "Option B", etc.)
    @Column(nullable = false) // Ensures that optionText cannot be null in the database
    private String optionText;

    // Whether this option is the correct answer (true or false)
    @Column(nullable = false) // Ensures that isCorrect is always provided in the database
    private boolean isCorrect;

    // Many options belong to one question (foreign key relationship)
    @ManyToOne(fetch = FetchType.EAGER) // Fetch the associated question eagerly (loaded immediately with the option)
    @JoinColumn(name = "question_id", nullable = false) // Maps the foreign key to the question_id column in the options table
    private Question question;

    // Default constructor required by JPA for instantiation
    public Option() {}

    // Constructor that excludes the isCorrect flag (useful if you just want to add options without specifying correctness)
    public Option(String optionText, Question question) {
        this.optionText = optionText;
        this.question = question;
    }

    // Full constructor to initialize all fields
    public Option(String optionText, boolean isCorrect, Question question) {
        this.optionText = optionText;
        this.isCorrect = isCorrect;
        this.question = question;
    }

    // Getters and Setters for each field
    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    // Override toString method to provide a readable representation of an Option
    @Override
    public String toString() {
        return "Option{" +
                "optionId=" + optionId +
                ", optionText='" + optionText + '\'' +
                ", isCorrect=" + isCorrect +
                '}'; // Format output for easy inspection of Option objects
    }
}
