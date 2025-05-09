package com.exam.manager;

import com.exam.entities.CorrectAnswer;
import com.exam.entities.Course;
import com.exam.entities.Exam;
import com.exam.entities.Option;
import com.exam.entities.Question;
import com.exam.service.CourseService;
import com.exam.service.ExamService;
import com.exam.service.QuestionService;
import com.exam.service.impl.ExamServiceImpl;
import com.exam.service.impl.CourseServiceImpl;
import com.exam.service.impl.QuestionServiceImpl;
import com.exam.util.HibernateUtil;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Scanner;

public class QuestionManager {

    private static final Scanner scanner = new Scanner(System.in);
    private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private static ExamService examService = new ExamServiceImpl(sessionFactory);
    private static QuestionService questionService = new QuestionServiceImpl(examService, sessionFactory);
    private static CourseService CourseService = new CourseServiceImpl();
    
    // Main method to manage questions
    public static void manageQuestions() {
        while (true) {
            System.out.println("\n==== Question Management ====");
            System.out.println("1. Add Question");
            System.out.println("2. View Question by ID");
            System.out.println("3. View Questions by Exam Title");
            System.out.println("4. View All Questions");
            System.out.println("5. Update Question");
            System.out.println("6. Delete Question");
            System.out.println("7. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    addQuestion();
                    break;
                case 2:
                    viewQuestionById();
                    break;
                case 3:
                    viewQuestionsByExamTitle();
                    break;
                case 4:
                    viewAllQuestions();
                    break;
                case 5:
                    updateQuestion();
                    break;
                case 6:
                    deleteQuestion();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("❌ Invalid choice! Please try again.");
            }
        }
    }

    public static void addQuestion() {
    	
    	 System.out.println("==== Question Management: Add Questions ====");

        // Fetch Courses
        List<Course> courses = CourseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("❌ No courses available.");
            return;
        }

        System.out.println("\n🎓 Select a Course:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getCourseName());
        }
        System.out.print("Enter your choice: ");
        int courseChoice = getIntInput();

        if (courseChoice < 1 || courseChoice > courses.size()) {
            System.out.println("❌ Invalid course selection.");
            return;
        }

        Course selectedCourse = courses.get(courseChoice - 1);

        // Fetch Exams
        List<Exam> exams = examService.getExamsByCourseName(selectedCourse.getCourseName());
        if (exams == null || exams.isEmpty()) {
            System.out.println("❌ No exams found for the selected course.");
            return;
        }

        System.out.println("\n📚 Select an Exam:");
        for (int i = 0; i < exams.size(); i++) {
            System.out.println((i + 1) + ". " + exams.get(i).getTitle());
        }
        System.out.print("Enter your choice: ");
        int examChoice = getIntInput();

        if (examChoice < 1 || examChoice > exams.size()) {
            System.out.println("❌ Invalid exam selection.");
            return;
        }

        Exam selectedExam = exams.get(examChoice - 1);
        int totalQuestions = selectedExam.getNumQuestions();
        System.out.println("\n📝 You need to enter " + totalQuestions + " questions for exam: " + selectedExam.getTitle());

        List<Question> addedQuestions = new ArrayList<>();

        for (int i = 1; i <= totalQuestions; i++) {
            System.out.println("\n➤ [" + i + "/" + totalQuestions + "] Add Question");

            System.out.print("Enter Question Text: ");
            String questionText = scanner.nextLine().trim();
            if (questionText.isEmpty()) {
                System.out.println("❌ Question text cannot be empty.");
                i--;
                continue;
            }

            System.out.println("\nSelect Question Type:\n1. Multiple Choice (MCQ)\n2. True / False");
            System.out.print("Enter choice: ");
            int typeChoice = getIntInput();
            String questionType = (typeChoice == 1) ? "MCQ" : (typeChoice == 2) ? "TrueFalse" : "";

            if (questionType.isEmpty()) {
                System.out.println("❌ Invalid question type.");
                i--;
                continue;
            }

            Question question = new Question();
            question.setQuestionText(questionText);
            question.setQuestionType(questionType);
            question.setExam(selectedExam);

            if (questionType.equalsIgnoreCase("MCQ")) {
                System.out.println("\nEnter 4 unique options:");
                System.out.print("Option A: ");
                String a = scanner.nextLine();
                System.out.print("Option B: ");
                String b = scanner.nextLine();
                System.out.print("Option C: ");
                String c = scanner.nextLine();
                System.out.print("Option D: ");
                String d = scanner.nextLine();

                System.out.print("\nCorrect Option (A/B/C/D): ");
                String correctOption = scanner.nextLine().trim().toUpperCase();
                String correctAnswer;
                switch (correctOption) {
                    case "A": correctAnswer = a; break;
                    case "B": correctAnswer = b; break;
                    case "C": correctAnswer = c; break;
                    case "D": correctAnswer = d; break;
                    default:
                        System.out.println("❌ Invalid option.");
                        i--;
                        continue;
                }

                // Preview
                System.out.println("\nPreview:");
                System.out.println("Q: " + questionText);
                System.out.println("   A. " + a);
                System.out.println("   B. " + b);
                System.out.println("   C. " + c);
                System.out.println("   D. " + d);
                System.out.println("   ✅ Correct Answer: " + correctAnswer);

                System.out.print("Confirm and save? (Y/N): ");
                if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                    System.out.println("⚠️ Skipping question...");
                    i--;
                    continue;
                }

                question.setOptionA(a);
                question.setOptionB(b);
                question.setOptionC(c);
                question.setOptionD(d);
                question.setCorrectAnswerText(correctAnswer);

            } else { // True/False
                System.out.print("Correct Answer (True/False): ");
                String correct = scanner.nextLine().trim();
                if (!correct.equalsIgnoreCase("True") && !correct.equalsIgnoreCase("False")) {
                    System.out.println("❌ Invalid True/False input.");
                    i--;
                    continue;
                }

                // Preview
                System.out.println("\nPreview:");
                System.out.println("Q: " + questionText);
                System.out.println("   ✅ Correct Answer: " + correct);

                System.out.print("Confirm and save? (Y/N): ");
                if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                    System.out.println("⚠️ Skipping question...");
                    i--;
                    continue;
                }

                question.setCorrectAnswerText(correct);
            }

            questionService.createQuestion(question);
            addedQuestions.add(question);
            System.out.println("✅ Question " + i + " saved successfully.\n------------------------------------------------");
        }

        // Summary
        System.out.println("\n📋 Summary of Questions Added to Exam: " + selectedExam.getTitle());
        System.out.println("------------------------------------------------");
        for (int i = 0; i < addedQuestions.size(); i++) {
            Question q = addedQuestions.get(i);
            System.out.println((i + 1) + ". " + q.getQuestionText() + " (" + q.getQuestionType() + ")");
            if (q.getQuestionType().equalsIgnoreCase("MCQ")) {
                System.out.println("   A. " + q.getOptionA());
                System.out.println("   B. " + q.getOptionB());
                System.out.println("   C. " + q.getOptionC());
                System.out.println("   D. " + q.getOptionD());
            }
            System.out.println("   ✅ Correct Answer: " + q.getCorrectAnswerText());
            System.out.println("------------------------------------------------");
        }

        System.out.println("🎉 All " + totalQuestions + " questions added successfully!");
    }


    // Get integer input safely
    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid input! Please enter a valid number.");
            scanner.next(); // Consume wrong input
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Important! consume leftover newline
        return value;
    }

    // View a question by ID
    private static void viewQuestionById() {
        try {
            // Prompt user for the Question ID
            System.out.print("Enter Question ID: ");
            int questionId = getIntInput();

            // Fetch question by ID
            Question question = questionService.getQuestionById(questionId);

            // Check if question exists
            if (question != null) {
                // Display Exam Title first
                System.out.println("\nExam Title: " + question.getExam().getTitle());

                // Display Question details
                displayQuestion(question);
            } else {
                System.out.println("❌ Question not found.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error occurred while retrieving the question: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to display the question details
    private static void displayQuestion(Question question) {
        System.out.println("\n📌 Question ID: " + question.getQuestionId());
        System.out.println("Question Text: " + question.getQuestionText());

        if (question.getQuestionType().equalsIgnoreCase("MCQ")) {
            System.out.println("Options:");
            System.out.println("  A. " + question.getOptionA());
            System.out.println("  B. " + question.getOptionB());
            System.out.println("  C. " + question.getOptionC());
            System.out.println("  D. " + question.getOptionD());
            System.out.println("Correct Answer: " + question.getCorrectAnswerText() + " ✅");
        } else if (question.getQuestionType().equalsIgnoreCase("TrueFalse")) {
            System.out.println("Correct Answer: " + question.getCorrectAnswerText() + " ✅");
        }
    }


    // View questions by exam title
    private static void viewQuestionsByExamTitle() {
        try {
            System.out.print("Enter Exam Title: ");
            String examTitle = scanner.nextLine();

            // Fetch the exam based on the provided title
            Exam exam = examService.getExamByTitle(examTitle);

            if (exam == null) {
                System.out.println("❌ Exam not found. Please ensure the exam title is correct.");
                return;
            }

            // Display the exam title first
            System.out.println("\n📚 Exam Title: " + exam.getTitle());
            System.out.println("--------------------------------------------------");

            // Fetch the questions associated with the exam
            List<Question> questions = questionService.getQuestionsByExam(exam);

            // If no questions exist for the exam
            if (questions.isEmpty()) {
                System.out.println("❌ No questions found for this exam.");
            } else {
                // Iterate and display each question
                for (Question question : questions) {
                    displayQuestion(question);
                }
            }

        } catch (Exception e) {
            System.out.println("❌ Error occurred while retrieving questions: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // View all questions
    private static void viewAllQuestions() {
        try {
            List<Question> questions = questionService.getAllQuestions();

            // If no questions are available
            if (questions.isEmpty()) {
                System.out.println("❌ No questions available in the system.");
                return;
            }

            // Ask user for confirmation before displaying all questions
            System.out.print("Do you want to view all questions? (Y/N): ");
            String userConfirmation = scanner.nextLine().trim().toUpperCase();

            if (userConfirmation.equals("Y")) {
                System.out.println("\n📋 All Questions in the System:");
                System.out.println("--------------------------------------------------");

                // Iterate through all the questions and display them
                int count = 1;
                for (Question question : questions) {
                    System.out.println("Question " + count++ + ":");
                    displayQuestion(question);
                    System.out.println("--------------------------------------------------");
                }

                // Display total number of questions
                System.out.println("\nTotal number of questions available: " + questions.size());
            } else {
                System.out.println("⚠️ Operation canceled by the user.");
            }

        } catch (Exception e) {
            // Improved error handling with specific exception messages
            System.out.println("❌ Error occurred while retrieving questions. Please try again later.");
            e.printStackTrace(); // For debugging purposes
        }
    }


    // Update a question
    private static void updateQuestion() {
        try {
            System.out.println("==== Question Management: Update Question ====");

            // Step 1: Select Course
            List<Course> courses = CourseService.getAllCourses();
            if (courses.isEmpty()) {
                System.out.println("❌ No courses available.");
                return;
            }

            System.out.println("\n🎓 Select a Course:");
            for (int i = 0; i < courses.size(); i++) {
                System.out.println((i + 1) + ". " + courses.get(i).getCourseName());
            }
            System.out.print("Enter your choice: ");
            int courseChoice = getIntInput();

            if (courseChoice < 1 || courseChoice > courses.size()) {
                System.out.println("❌ Invalid course selection.");
                return;
            }

            Course selectedCourse = courses.get(courseChoice - 1);

            // Step 2: Select Exam
            List<Exam> exams = examService.getExamsByCourseName(selectedCourse.getCourseName());
            if (exams.isEmpty()) {
                System.out.println("❌ No exams found for the selected course.");
                return;
            }

            System.out.println("\n📚 Select an Exam:");
            for (int i = 0; i < exams.size(); i++) {
                System.out.println((i + 1) + ". " + exams.get(i).getTitle());
            }
            System.out.print("Enter your choice: ");
            int examChoice = getIntInput();

            if (examChoice < 1 || examChoice > exams.size()) {
                System.out.println("❌ Invalid exam selection.");
                return;
            }

            Exam selectedExam = exams.get(examChoice - 1);

            // Step 3: Select Question
            List<Question> questions = questionService.getQuestionsByExam(selectedExam);
            if (questions.isEmpty()) {
                System.out.println("❌ No questions found for the selected exam.");
                return;
            }

            System.out.println("\n📝 Select a Question to Update:");
            for (int i = 0; i < questions.size(); i++) {
                System.out.println((i + 1) + ". " + questions.get(i).getQuestionText());
            }
            System.out.print("Enter your choice: ");
            int questionChoice = getIntInput();

            if (questionChoice < 1 || questionChoice > questions.size()) {
                System.out.println("❌ Invalid question selection.");
                return;
            }

            Question question = questions.get(questionChoice - 1);

            // Display current question
            System.out.println("\nCurrent Question Details:");
            displayQuestion(question);

            // --- Begin Update ---
            System.out.print("\nEnter New Question Text (Leave blank to keep current): ");
            String newText = scanner.nextLine().trim();
            if (!newText.isEmpty()) {
                question.setQuestionText(newText);
            }

            System.out.println("\nSelect New Question Type (Leave blank to keep current):\n1. MCQ\n2. True / False");
            String typeInput = scanner.nextLine().trim();
            String questionType = question.getQuestionType();

            if (typeInput.equals("1")) questionType = "MCQ";
            else if (typeInput.equals("2")) questionType = "TrueFalse";

            question.setQuestionType(questionType);

            if (questionType.equalsIgnoreCase("MCQ")) {
                System.out.print("Option A (Leave blank to keep current): ");
                String a = scanner.nextLine().trim();
                if (!a.isEmpty()) question.setOptionA(a);

                System.out.print("Option B (Leave blank to keep current): ");
                String b = scanner.nextLine().trim();
                if (!b.isEmpty()) question.setOptionB(b);

                System.out.print("Option C (Leave blank to keep current): ");
                String c = scanner.nextLine().trim();
                if (!c.isEmpty()) question.setOptionC(c);

                System.out.print("Option D (Leave blank to keep current): ");
                String d = scanner.nextLine().trim();
                if (!d.isEmpty()) question.setOptionD(d);

                System.out.print("Correct Option (A/B/C/D): ");
                String correctOption = scanner.nextLine().trim().toUpperCase();

                switch (correctOption) {
                    case "A": question.setCorrectAnswerText(question.getOptionA()); break;
                    case "B": question.setCorrectAnswerText(question.getOptionB()); break;
                    case "C": question.setCorrectAnswerText(question.getOptionC()); break;
                    case "D": question.setCorrectAnswerText(question.getOptionD()); break;
                    default:
                        System.out.println("❌ Invalid correct option.");
                        return;
                }

            } else if (questionType.equalsIgnoreCase("TrueFalse")) {
                System.out.print("Correct Answer (True/False): ");
                String tfAnswer = scanner.nextLine().trim();
                if (tfAnswer.equalsIgnoreCase("True") || tfAnswer.equalsIgnoreCase("False")) {
                    question.setCorrectAnswerText(tfAnswer);
                } else {
                    System.out.println("❌ Invalid input for True/False.");
                    return;
                }
            }

            // Confirm update
            System.out.println("\n🔍 Review Updated Question:");
            displayQuestion(question);

            System.out.print("Confirm update? (Y/N): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                System.out.println("⚠️ Update canceled.");
                return;
            }

            questionService.updateQuestion(question);
            System.out.println("✅ Question updated successfully!");

        } catch (Exception e) {
            System.out.println("❌ Error occurred while updating the question: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Delete a question
    private static void deleteQuestion() {
        try {
            System.out.println("==== Question Management: Delete Question ====");

            // Fetch Courses
            List<Course> courses = CourseService.getAllCourses();
            if (courses.isEmpty()) {
                System.out.println("❌ No courses available.");
                return;
            }

            System.out.println("\n🎓 Select a Course:");
            for (int i = 0; i < courses.size(); i++) {
                System.out.println((i + 1) + ". " + courses.get(i).getCourseName());
            }
            System.out.print("Enter your choice: ");
            int courseChoice = getIntInput();

            if (courseChoice < 1 || courseChoice > courses.size()) {
                System.out.println("❌ Invalid course selection.");
                return;
            }

            Course selectedCourse = courses.get(courseChoice - 1);

            // Fetch Exams
            List<Exam> exams = examService.getExamsByCourseName(selectedCourse.getCourseName());
            if (exams == null || exams.isEmpty()) {
                System.out.println("❌ No exams found for the selected course.");
                return;
            }

            System.out.println("\n📚 Select an Exam:");
            for (int i = 0; i < exams.size(); i++) {
                System.out.println((i + 1) + ". " + exams.get(i).getTitle());
            }
            System.out.print("Enter your choice: ");
            int examChoice = getIntInput();

            if (examChoice < 1 || examChoice > exams.size()) {
                System.out.println("❌ Invalid exam selection.");
                return;
            }

            Exam selectedExam = exams.get(examChoice - 1);

            List<Question> questions = questionService.getQuestionsByExam(selectedExam);
            if (questions.isEmpty()) {
                System.out.println("❌ No questions found for this exam.");
                return;
            }

            System.out.println("\n🗑️ Select a Question to Delete:");
            for (int i = 0; i < questions.size(); i++) {
                System.out.println((i + 1) + ". " + questions.get(i).getQuestionText());
            }
            System.out.print("Enter your choice: ");
            int questionChoice = getIntInput();

            if (questionChoice < 1 || questionChoice > questions.size()) {
                System.out.println("❌ Invalid question selection.");
                return;
            }

            Question questionToDelete = questions.get(questionChoice - 1);

            // Confirm
            System.out.println("\n⚠️ Are you sure you want to delete the following question?");
            System.out.println("Q: " + questionToDelete.getQuestionText());
            System.out.print("Type 'YES' to confirm: ");
            String confirm = scanner.nextLine().trim();

            if (!confirm.equalsIgnoreCase("YES")) {
                System.out.println("⚠️ Deletion cancelled.");
                return;
            }

            questionService.deleteQuestion(questionToDelete.getQuestionId());
            System.out.println("✅ Question deleted successfully!");

        } catch (Exception e) {
            System.out.println("❌ Error occurred while deleting the question: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
