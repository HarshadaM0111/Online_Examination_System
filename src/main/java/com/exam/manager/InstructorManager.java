package com.exam.manager;

import com.exam.entities.Instructor;
import com.exam.entities.Department; // Assuming you have a Department entity
import com.exam.service.InstructorService;
import com.exam.service.DepartmentService; // Assuming you have a DepartmentService
import com.exam.service.impl.InstructorServiceImpl;
import com.exam.service.impl.DepartmentServiceImpl; // Assuming you have a DepartmentServiceImpl

import java.util.List;
import java.util.Scanner;

public class InstructorManager {
    static Scanner scanner = new Scanner(System.in);
    static InstructorService instructorService = new InstructorServiceImpl();
    static DepartmentService departmentService = new DepartmentServiceImpl(); // Initialize DepartmentService

    public static void manageInstructors() {
        while (true) {
            System.out.println("\n==== Instructor Management ====");
            System.out.println("1. Add Instructor");
            System.out.println("2. View Instructor by ID");
            System.out.println("3. View All Instructors");
            System.out.println("4. Update Instructor");
            System.out.println("5. Delete Instructor");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> addInstructor();
                case 2 -> viewInstructor();
                case 3 -> viewAllInstructors();
                case 4 -> updateInstructor();
                case 5 -> deleteInstructor();
                case 6 -> { return; }
                default -> System.out.println("❌ Invalid choice! Please try again.");
            }
        }
    }

    private static void addInstructor() {
        System.out.print("Enter Instructor Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Instructor Email: ");
        String email = scanner.nextLine();

        if (email == null || email.trim().isEmpty()) {
            System.out.println("❌ Email cannot be empty.");
            return;
        }

        System.out.print("Enter Instructor Password: ");
        String password = scanner.nextLine();

        if (password == null || password.trim().isEmpty()) {
            System.out.println("❌ Password cannot be empty.");
            return;
        }

        // Display available departments for selection
        System.out.println("Select Department:");
        List<Department> departments = departmentService.getAllDepartments();
        for (int i = 0; i < departments.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, departments.get(i).getDeptName());
        }

        System.out.print("Enter department number: ");
        int deptChoice = getIntInput() - 1;  // Adjusting index for 0-based selection

        if (deptChoice < 0 || deptChoice >= departments.size()) {
            System.out.println("❌ Invalid department selection. Instructor not added.");
            return;
        }

        Department selectedDepartment = departments.get(deptChoice);

        // Create new Instructor object and set values
        Instructor instructor = new Instructor();
        instructor.setName(name);
        instructor.setEmail(email);
        instructor.setPassword(password);
        instructor.setDepartment(selectedDepartment);

        try {
            instructorService.createInstructor(instructor);
            System.out.println("✅ Instructor added successfully!");
        } catch (Exception e) {
            System.out.println("❌ Error adding instructor: " + e.getMessage());
        }
    }

    private static void viewInstructor() {
        System.out.print("Enter Instructor ID: ");
        int instructorId = getIntInput();
        Instructor instructor = instructorService.getInstructorById(instructorId);
        
        if (instructor != null) {
            String departmentName = (instructor.getDepartment() != null) ? instructor.getDepartment().getDeptName() : "N/A";
            
            // Print the instructor details in a table-like format
            System.out.println("---------------------------------------------------------------");
            System.out.printf("| %-15s | %-20s | %-25s | %-20s |%n", 
                              "Instructor ID", "Name", "Email", "Department");
            System.out.println("---------------------------------------------------------------");
            System.out.printf("| %-15d | %-20s | %-25s | %-20s |%n", 
                              instructor.getInstructorId(), instructor.getName(), instructor.getEmail(), departmentName);
            System.out.println("---------------------------------------------------------------");
        } else {
            System.out.println("❌ Instructor with ID " + instructorId + " not found.");
        }
    }


    private static void viewAllInstructors() {
        List<Instructor> instructors = instructorService.getAllInstructors();
        if (instructors.isEmpty()) {
            System.out.println("❌ No instructors found.");
        } else {
            // Print the table header
            System.out.println("---------------------------------------------------------------");
            System.out.printf("| %-15s | %-20s | %-25s | %-20s |%n", "Instructor ID", "Name", "Email", "Department");
            System.out.println("---------------------------------------------------------------");

            // Loop through the list and print each instructor in tabular format
            for (Instructor instructor : instructors) {
                System.out.printf("| %-15d | %-20s | %-25s | %-20s |%n",
                        instructor.getInstructorId(), instructor.getName(), instructor.getEmail(), instructor.getDepartment());
            }

            System.out.println("---------------------------------------------------------------");
        }
    }

    private static void updateInstructor() {
        System.out.println("Update Instructor by: ");
        System.out.println("1. Instructor ID");
        System.out.println("2. Instructor Name");
        System.out.print("Enter your choice: ");
        int choice = getIntInput();

        Instructor instructor = null;

        if (choice == 1) {
            // Update by Instructor ID
            System.out.print("Enter Instructor ID to update: ");
            int instructorId = getIntInput();
            instructor = instructorService.getInstructorById(instructorId);
        } else if (choice == 2) {
            // Update by Instructor Name
            System.out.print("Enter Instructor Name to update: ");
            String instructorName = scanner.nextLine().trim();
            instructor = instructorService.getInstructorByName(instructorName);
        } else {
            System.out.println("❌ Invalid choice. Instructor update failed.");
            return;
        }

        if (instructor != null) {
            // Ask user what field they want to update
            System.out.println("What would you like to update?");
            System.out.println("1. Name");
            System.out.println("2. Email");
            System.out.println("3. Department");
            System.out.print("Enter your choice: ");
            int updateChoice = getIntInput();

            switch (updateChoice) {
                case 1:
                    // Update Name
                    System.out.print("Enter New Instructor Name: ");
                    String newName = scanner.nextLine().trim();
                    if (!newName.isEmpty()) {
                        instructor.setName(newName);
                    } else {
                        System.out.println("❌ Name cannot be empty.");
                        return;
                    }
                    break;

                case 2:
                    // Update Email
                    System.out.print("Enter New Instructor Email: ");
                    String newEmail = scanner.nextLine().trim();
                    if (!newEmail.isEmpty()) {
                        instructor.setEmail(newEmail);
                    } else {
                        System.out.println("❌ Email cannot be empty.");
                        return;
                    }
                    break;

                case 3:
                    // Update Department
                    System.out.println("Select Department to Update:");
                    List<Department> departments = departmentService.getAllDepartments();
                    for (int i = 0; i < departments.size(); i++) {
                        System.out.printf("%d. %s%n", i + 1, departments.get(i).getDeptName());
                    }

                    System.out.print("Enter department number: ");
                    int deptChoice = getIntInput() - 1;  // Adjusting index for 0-based selection

                    if (deptChoice < 0 || deptChoice >= departments.size()) {
                        System.out.println("❌ Invalid department selection.");
                        return;
                    }
                    Department newDepartment = departments.get(deptChoice);
                    instructor.setDepartment(newDepartment);  // Set the new department
                    break;

                default:
                    System.out.println("❌ Invalid update option.");
                    return;
            }

            try {
                instructorService.updateInstructor(instructor);
                System.out.println("✅ Instructor updated successfully!");
            } catch (Exception e) {
                System.out.println("❌ Error updating instructor: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Instructor not found.");
        }
    }

    
    private static void deleteInstructor() {
        // Offer user the choice of deleting by ID or name
        System.out.println("Delete Instructor by:");
        System.out.println("1. Instructor ID");
        System.out.println("2. Instructor Name");
        System.out.print("Enter your choice: ");
        int choice = getIntInput();

        Instructor instructor = null;

        switch (choice) {
            case 1: // Delete by Instructor ID
                System.out.print("Enter Instructor ID to delete: ");
                int instructorId = getIntInput();

                // Check if instructor exists
                instructor = instructorService.getInstructorById(instructorId);
                if (instructor == null) {
                    System.out.println("❌ Instructor with ID " + instructorId + " not found.");
                    return;
                }
                break;

            case 2: // Delete by Instructor Name
                System.out.print("Enter Instructor Name to delete: ");
                String instructorName = scanner.nextLine().trim();

                // Fetch instructor by name (case-insensitive)
                instructor = instructorService.getInstructorByName(instructorName);
                if (instructor == null) {
                    System.out.println("❌ Instructor with name '" + instructorName + "' not found.");
                    return;
                }
                break;

            default:
                System.out.println("❌ Invalid choice.");
                return;
        }

        // Ask for confirmation before deletion
        System.out.println("Are you sure you want to delete the following instructor?");
        System.out.println("Instructor ID: " + instructor.getInstructorId());
        System.out.println("Name: " + instructor.getName());
        System.out.println("Email: " + instructor.getEmail());
        System.out.println("Department: " + (instructor.getDepartment() != null ? instructor.getDepartment().getDeptName() : "N/A"));
        System.out.print("Enter 'yes' to confirm or 'no' to cancel: ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if ("yes".equals(confirmation)) {
            // Proceed to delete the instructor
            try {
                instructorService.deleteInstructor(instructor.getInstructorId());
                System.out.println("✅ Instructor deleted successfully!");
            } catch (Exception e) {
                System.out.println("❌ Error deleting instructor: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Deletion canceled.");
        }
    }


    public static int getIntInput() {
        while (true) {
            try {
                int input = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                return input;
            } catch (Exception e) {
                scanner.nextLine(); // Clear invalid input
                System.out.print("❌ Invalid input! Enter a valid number: ");
            }
        }
    }
}
