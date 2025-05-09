package com.exam.manager;

import com.exam.entities.StudentAnswer;
import com.exam.entities.Option;
import com.exam.entities.Question;
import com.exam.entities.Student;
import com.exam.service.impl.StudentAnswerServiceImpl;
import com.exam.service.impl.ExamServiceImpl;
import com.exam.service.impl.QuestionServiceImpl;
import com.exam.service.impl.StudentServiceImpl;
import com.exam.util.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class StudentAnswerManager {
   
	static Scanner scanner = new Scanner(System.in);

	// Step 1: Create ExamService
	static ExamServiceImpl examService = new ExamServiceImpl(HibernateUtil.getSessionFactory());

	// Step 2: Create other services
	static StudentAnswerServiceImpl studentAnswerService = new StudentAnswerServiceImpl();
	static QuestionServiceImpl questionService = new QuestionServiceImpl(examService, HibernateUtil.getSessionFactory());
	static StudentServiceImpl studentService = new StudentServiceImpl();
    

    public static void manageStudentAnswers() {
        while (true) {
            System.out.println("\n==== Student Answer Management ====");
            System.out.println("1. Add Answer");
            System.out.println("2. View Answer by ID");
            System.out.println("3. View All Answers");
            System.out.println("4. Update Answer");
            System.out.println("5. Delete Answer");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> addStudentAnswer();
                case 2 -> viewStudentAnswer();
                case 3 -> viewAllStudentAnswers();
                case 4 -> updateStudentAnswer();
                case 5 -> deleteStudentAnswer();
                case 6 -> { return; }
                default -> System.out.println("❌ Invalid choice! Please try again.");
            }
        }
    }

//    private static void addStudentAnswer() {
//        System.out.print("Enter Question ID: ");
//        int questionId = getIntInput();
//    //   Question question = questionService.getQuestionById(questionId);
//        Question question = questionService.getQuestionByIdWithOptions(questionId);
//
//
//        if (question == null) {
//            System.out.println("❌ Question not found.");
//            return;
//        }
//
//        // Display options dynamically
//        if (question.getOptions() != null && !question.getOptions().isEmpty()) {
//            System.out.println("Options:");
//
//            // Dynamically handle the options based on the number of options available
//            char label = 'A';
//            Option[] optionArray = new Option[question.getOptions().size()];
//            int index = 0;
//            for (Option option : question.getOptions()) {
//                System.out.println(label + ": " + option.getOptionText());
//                optionArray[index++] = option;
//                label++;  // Increment label from 'A' to 'B', 'C', etc.
//            }
//
//            // Get user choice
//            System.out.print("Enter your choice (A/B/C/D...): ");
//            String choice = scanner.nextLine().trim().toUpperCase();
//
//            // Validate the choice
//            if (choice.length() != 1 || choice.charAt(0) < 'A' || choice.charAt(0) >= ('A' + optionArray.length)) {
//                System.out.println("❌ Invalid option selected.");
//                return;
//            }
//
//            // Convert letter to index and get the corresponding option
//            int selectedIndex = choice.charAt(0) - 'A';
//            Option selectedOption = optionArray[selectedIndex];
//
//            System.out.print("Enter Student ID: ");
//            int studentId = getIntInput();
//            Student student = studentService.getStudentById(studentId);
//            if (student == null) {
//                System.out.println("❌ Student not found.");
//                return;
//            }
//
//            // Create StudentAnswer
//            StudentAnswer studentAnswer = new StudentAnswer(selectedOption.getOptionText(), question, student);
//            studentAnswerService.createStudentAnswer(studentAnswer);
//
//            System.out.println("✅ Answer added! Correct: " + selectedOption.isCorrect());
//        } else {
//            System.out.println("⚠️ No options available for this question.");
//        }
//    }
    
    private static void addStudentAnswer() {
        System.out.print("Enter Question ID: ");
        int questionId = getIntInput();

        // ❗ Correct method to fetch question + options
        Question question = questionService.getQuestionByIdWithOptions(questionId);

        if (question == null) {
            System.out.println("❌ Question not found.");
            return;
        }

        // Display options dynamically
        if (question.getOptions() != null && !question.getOptions().isEmpty()) {
            System.out.println("Options:");
            char label = 'A';
            Option[] optionArray = new Option[question.getOptions().size()];
            int index = 0;
            for (Option option : question.getOptions()) {
                System.out.println(label + ": " + option.getOptionText());
                optionArray[index++] = option;
                label++;
            }

            System.out.print("Enter your choice (A/B/C/D...): ");
            String choice = scanner.nextLine().trim().toUpperCase();

            if (choice.length() != 1 || choice.charAt(0) < 'A' || choice.charAt(0) >= ('A' + optionArray.length)) {
                System.out.println("❌ Invalid option selected.");
                return;
            }

            int selectedIndex = choice.charAt(0) - 'A';
            Option selectedOption = optionArray[selectedIndex];

            System.out.print("Enter Student ID: ");
            int studentId = getIntInput();
            Student student = studentService.getStudentById(studentId);
            if (student == null) {
                System.out.println("❌ Student not found.");
                return;
            }

            StudentAnswer studentAnswer = new StudentAnswer(selectedOption.getOptionText(), question, student);
            studentAnswerService.createStudentAnswer(studentAnswer);

            System.out.println("✅ Answer added! Correct: " + selectedOption.isCorrect());
        } else {
            System.out.println("⚠️ No options available for this question.");
        }
    }


    private static void viewStudentAnswer() {
        System.out.print("Enter Student Answer ID: ");
        int answerId = getIntInput();
        StudentAnswer studentAnswer = studentAnswerService.getStudentAnswerById(answerId);
        System.out.println(studentAnswer != null ? studentAnswer : "❌ Answer not found.");
    }

    private static void viewAllStudentAnswers() {
        System.out.print("Enter Student ID to view all answers: ");
        int studentId = getIntInput();
        List<StudentAnswer> studentAnswers = studentAnswerService.getStudentAnswersByStudentId(studentId);
        if (studentAnswers != null && !studentAnswers.isEmpty()) {
            for (StudentAnswer studentAnswer : studentAnswers) {
                System.out.println(studentAnswer);
                if (studentAnswer.getQuestion() != null) {
                    System.out.println("Question: " + studentAnswer.getQuestion().getQuestionText());
                }
                System.out.println("Student: " + (studentAnswer.getStudent() != null ? studentAnswer.getStudent().getName() : "Unknown"));
            }
        } else {
            System.out.println("❌ No answers found for the student.");
        }
    }

    private static void updateStudentAnswer() {
        System.out.print("Enter Student Answer ID to update: ");
        int answerId = getIntInput();
        StudentAnswer studentAnswer = studentAnswerService.getStudentAnswerById(answerId);

        if (studentAnswer != null) {
            System.out.print("Enter New Answer Text: ");
            String newText = scanner.nextLine();
            studentAnswer.setAnswerText(newText);

            Question question = studentAnswer.getQuestion();
            if (question != null) {
                boolean isCorrect = newText.trim().equalsIgnoreCase(question.getCorrectAnswerText().trim());
                // Update correctness of the answer
                // You can decide to add isCorrect if required
            }

            studentAnswerService.updateStudentAnswer(studentAnswer);
            System.out.println("✅ Student Answer updated successfully!");
        } else {
            System.out.println("❌ Answer not found.");
        }
    }

    private static void deleteStudentAnswer() {
        System.out.print("Enter Student Answer ID to delete: ");
        int answerId = getIntInput();
        studentAnswerService.deleteStudentAnswer(answerId);
        System.out.println("✅ Student Answer deleted successfully!");
    }

    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("❌ Invalid input. Please enter a valid number: ");
            }
        }
    }
}
