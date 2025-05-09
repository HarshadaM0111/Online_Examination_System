package com.exam;

import com.exam.dao.UserDao;
import com.exam.entities.User;
import java.util.Scanner;

public class Main {
    // Scanner instance to capture user input
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Infinite loop to display the main menu continuously
        while (true) {
            // Display main menu with options
            System.out.println("\n==== Online Examination System ====");
            System.out.println("1. Admin Login");
            System.out.println("2. Student Login");
            System.out.println("3. Instructor Login");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            // Capture user input and convert it to an integer
            int choice = getIntInput();

            // Handle the user's choice using a switch statement
            switch (choice) {
                case 1 -> handleLogin("admin"); // Admin login
                case 2 -> handleLogin("student"); // Student login
                case 3 -> handleLogin("instructor"); // Instructor login
                case 4 -> {
                    System.out.println("✅ Exiting system... Thank you!");
                    System.exit(0); // Exit the application
                }
                default -> System.out.println("❌ Invalid choice! Please try again."); // Invalid input
            }
        }
    }

    /**
     * Method to handle login logic for different roles (admin, student, instructor).
     * Validates that the username and password are non-empty.
     */
    private static void handleLogin(String expectedRole) {
        System.out.println("\n🔐 Login: " + expectedRole.substring(0, 1).toUpperCase() + expectedRole.substring(1));
        printDivider(); // Print a divider for better UI formatting

        // Prompt user for username and validate non-empty input
        System.out.print("Username : ");
        String username = scanner.nextLine().trim(); // Read username input
        if (username.isEmpty()) {
            System.out.println("❌ Username cannot be empty.");
            return; // Exit if username is empty
        }

        // Prompt user for password and validate non-empty input
        System.out.print("Password : ");
        String password = scanner.nextLine().trim(); // Read password input
        if (password.isEmpty()) {
            System.out.println("❌ Password cannot be empty.");
            return; // Exit if password is empty
        }

        // Initialize UserDao and attempt login with the given credentials
        UserDao userDao = new UserDao();
        User user = userDao.login(username, password);

        // Check if the login is successful and if the role matches the expected role
        if (user != null && user.getRole().equalsIgnoreCase(expectedRole)) {
            System.out.println("✅ Login successful! Welcome, " + user.getUsername());
            navigateToRoleMenu(expectedRole); // Navigate to the appropriate menu based on the role
        } else {
            System.out.println("❌ Login failed: Invalid credentials or role mismatch."); // Invalid login attempt
        }

        System.out.println("Returning to main menu...\n"); // Inform the user about the transition to the main menu
    }

    /**
     * Helper method to print a divider line for UI formatting.
     * Used to visually separate sections for better readability.
     */
    private static void printDivider() {
        System.out.println("----------------------------------------");
    }

    /**
     * Method to navigate to the corresponding menu based on the user's role.
     * Switches between different menus based on whether the user is an admin, student, or instructor.
     * @param role The role of the user to determine where to navigate.
     */
    private static void navigateToRoleMenu(String role) {
        // Switch-case based on the role, each case handles role-specific navigation
        switch (role.toLowerCase()) {
            case "admin" -> AdminMenu.showMenu(); // Redirect to Admin Menu
            case "student" -> StudentMenu.main(null); // Redirect to Student Menu
            case "instructor" -> InstructorMenu.main(null); // Redirect to Instructor Menu
            default -> System.out.println("❌ Role mismatch. Unable to navigate.");
        }
    }

    /**
     * Helper method to get integer input from the user with proper validation.
     * Ensures the user enters a valid integer.
     * @return the valid integer input from the user.
     */
    private static int getIntInput() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                
                // Validate non-empty input
                if (input.isEmpty()) {
                    System.out.print("❌ Input cannot be empty. Enter a number: ");
                    continue;
                }

                int choice = Integer.parseInt(input); // Try to parse the input to an integer
                if (choice < 1 || choice > 4) { // Ensure the choice is valid (1-4)
                    System.out.print("❌ Invalid option! Please choose between 1 and 4: ");
                    continue;
                }
                return choice; // Return the valid choice
            } catch (NumberFormatException e) {
                System.out.print("❌ Invalid input! Enter a valid number between 1 and 4: "); // Invalid input message
            }
        }
    }
}
