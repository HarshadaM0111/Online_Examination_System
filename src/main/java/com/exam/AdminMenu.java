package com.exam;

import com.exam.manager.*;

import java.util.Scanner;

public class AdminMenu {

    // Shared Scanner object for reading user input
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Displays the admin menu and handles user interaction.
     * Allows the admin to manage different parts of the system.
     */
    public static void showMenu() {
        while (true) {
            // Display menu options
            System.out.println("\n==== Admin Menu ====");
            System.out.println("1. Manage Students");
            System.out.println("2. Manage Courses");
            System.out.println("3. Manage Exams");
            System.out.println("4. Manage Questions");
            System.out.println("5. Manage Topics");
            System.out.println("6. Manage Instructors");
            System.out.println("7. Manage Departments");
            System.out.println("8. Manage Admins");
            System.out.println("9. Manage Answers");
            System.out.println("10. Logout");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();

            // Handle the user's choice using enhanced switch
            switch (choice) {
                case 1 -> StudentManager.manageStudents();           // Open Student Management
                case 2 -> CourseManager.manageCourses();             // Open Course Management
                case 3 -> ExamManager.manageExams();                 // Open Exam Management
                case 4 -> QuestionManager.manageQuestions();         // Open Question Management
                case 5 -> TopicManager.manageTopics();               // Open Topic Management
                case 6 -> InstructorManager.manageInstructors();     // Open Instructor Management
                case 7 -> DepartmentManager.manageDepartments();     // Open Department Management
                case 8 -> AdminManager.manageAdmins();               // Open Admin Management
                case 9 -> StudentAnswerManager.manageStudentAnswers(); // Open Answer Management
                case 10 -> {
                    System.out.println("✅ Logged out.");           // Logout message
                    return;                                         // Exit the loop and return to main menu or program
                }
                default -> System.out.println("❌ Invalid choice! Try again.");  // Handle invalid input
            }
        }
    }

    /**
     * Reads and validates an integer input from the user.
     * Keeps prompting until a valid integer is entered.
     *
     * @return a valid integer entered by the user
     */
    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("❌ Invalid input! Enter a number: ");
            }
        }
    }
}
