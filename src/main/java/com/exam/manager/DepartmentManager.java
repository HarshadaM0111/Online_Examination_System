package com.exam.manager;

import com.exam.entities.Department;
import com.exam.entities.Instructor;
import com.exam.service.DepartmentService;
import com.exam.service.impl.DepartmentServiceImpl;
import com.exam.service.InstructorService;
import com.exam.service.impl.InstructorServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class DepartmentManager {
    static Scanner scanner = new Scanner(System.in);
    static DepartmentService departmentService = new DepartmentServiceImpl();
    static InstructorService instructorService = new InstructorServiceImpl();


    public static void manageDepartments() {
        while (true) {
            System.out.println("\n==== Department Management ===="); 
            System.out.println("1. Add Department");
            System.out.println("2. View Department by ID");
            System.out.println("3. View All Departments");
            System.out.println("4. Update Department");
            System.out.println("5. Delete Department");
            System.out.println("6. View Department by Name");
            System.out.println("7. Assign Instructor to Department");
            System.out.println("8. Remove Instructor from Department");
            System.out.println("9. Restore Deleted Department");
            System.out.println("10. Search Departments by Name");
            System.out.println("11. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = getIntInput();

            // Ensuring valid choices for actions
            switch (choice) {
                case 1 -> addDepartment();
                case 2 -> viewDepartment();
                case 3 -> viewAllDepartments();
                case 4 -> updateDepartment();
                case 5 -> deleteDepartment();
                case 6 -> viewDepartmentByName();
                case 7 -> assignInstructorToDepartment();
                case 8 -> removeInstructorFromDepartment();
                case 9 -> restoreDepartment();  // Method for restoring soft-deleted departments
                case 10 -> searchDepartments();  // Method for searching departments by name
                case 11 -> {
                    return; // Back to the main menu
                }
                default -> System.out.println("❌ Invalid choice! Please try again.");
            }
        }
    }


    private static void addDepartment() {
        System.out.println("\n==== Add Department ====");
        System.out.print("Enter Department Name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("❌ Department name cannot be empty.");
            return;
        }

        // Check for existing department with the same name
        if (departmentService.departmentExistsByName(name)) {
            System.out.println("⚠️ Department '" + name + "' already exists.");
            return;
        }

        Department department = new Department(name);
        department.setStatus("Active");
        department.setCreatedAt(LocalDateTime.now());

        departmentService.createDepartment(department);

        // Assuming the department ID is set during creation
        System.out.println("✅ Department added successfully!");
        System.out.println("----------------------------------------");
        System.out.println("📄 Summary:");
        System.out.println("🆔 Department ID : " + department.getDeptId());
        System.out.println("🏷️  Name          : " + department.getDeptName());
        System.out.println("📅 Created At     : " + department.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        System.out.println("✅ Status         : " + department.getStatus());
    }


    private static void viewDepartment() {
        System.out.print("Enter Department ID: ");
        int departmentId = getIntInput(); 
        Department department = departmentService.getDepartmentById(departmentId);

        if (department != null) {
            System.out.println("\n📄 Department Details");
            System.out.println("--------------------------------------------------------------");
            System.out.printf("Department ID    : %d\n", department.getDeptId());
            System.out.printf("Department Name  : %s\n", department.getDeptName());
            System.out.printf("Status           : %s\n", department.getStatus());
            System.out.println("Instructors      :");

            if (department.getInstructors() == null || department.getInstructors().isEmpty()) {
                System.out.println("  - No instructors assigned.");
            } else {
                for (Instructor instructor : department.getInstructors()) {
                    System.out.printf("  - %d: %s (Email: %s)\n", instructor.getInstructorId(), instructor.getName(), instructor.getEmail());
                }
            }

            System.out.println("--------------------------------------------------------------");
        } else {
            System.out.println("❌ Department not found.");
        }
    }


    private static void viewAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();

        if (departments == null || departments.isEmpty()) {
            System.out.println("⚠️ No departments found.");
            return;
        }

        System.out.println("\n📋 Department List:");
        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.printf("| %-5s | %-25s | %-30s | %-10s |\n", "ID", "Department Name", "Instructors Assigned", "Status");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (Department dept : departments) {
            String instructorNames = dept.getInstructors() != null && !dept.getInstructors().isEmpty()
                    ? dept.getInstructors().stream()
                            .map(i -> i.getInstructorId() + ": " + i.getName())
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("None")
                    : "None";

            System.out.printf("| %-5d | %-25s | %-30s | %-10s |\n",
                    dept.getDeptId(),
                    dept.getDeptName(),
                    instructorNames,
                    dept.getStatus());
        }

        System.out.println("---------------------------------------------------------------------------------------------");
    }

    private static void updateDepartment() {
        System.out.print("Enter Department ID to update: ");
        int departmentId = getIntInput(); 
        Department department = departmentService.getDepartmentById(departmentId);

        if (department != null) {
            // Show current department details
            System.out.println("\n📄 Current Department Details");
            System.out.println("--------------------------------------------------------------");
            System.out.printf("Department ID    : %d\n", department.getDeptId());
            System.out.printf("Department Name  : %s\n", department.getDeptName());
            System.out.println("--------------------------------------------------------------");

            // Get new department name input
            System.out.print("Enter New Department Name (current: " + department.getDeptName() + "): ");
            String newDeptName = scanner.nextLine().trim();

            // Check if the new department name is provided
            if (newDeptName.isEmpty()) {
                System.out.println("❌ Department name cannot be empty. Update failed.");
                return;
            }

            // Set new name and update department
            department.setDeptName(newDeptName);
            departmentService.updateDepartment(department);

            // Provide feedback to the user
            System.out.println("\n✅ Department updated successfully!");
            System.out.println("Updated Department Details:");
            System.out.println("--------------------------------------------------------------");
            System.out.printf("Department ID    : %d\n", department.getDeptId());
            System.out.printf("New Department Name  : %s\n", department.getDeptName());
            System.out.println("--------------------------------------------------------------");
        } else {
            System.out.println("❌ Department not found.");
        }
    }


    private static void deleteDepartment() {
        System.out.print("Enter Department ID to delete: ");
        int departmentId = getIntInput(); 
        Department department = departmentService.getDepartmentById(departmentId);

        if (department != null) {
            // Ask for confirmation before deletion
            System.out.print("⚠️ Are you sure you want to delete '" + department.getDeptName() + "'? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if ("yes".equals(confirmation)) {
                // Perform soft delete by marking the department as deleted
                departmentService.deleteDepartment(departmentId);
                System.out.println("✅ Department '" + department.getDeptName() + "' marked as deleted successfully.");
            } else {
                System.out.println("❌ Deletion canceled.");
            }
        } else {
            System.out.println("❌ Department not found.");
        }
    }

    private static void searchDepartments() {
        System.out.print("Enter keyword to search: ");
        String keyword = scanner.nextLine().trim();

        List<Department> departments = departmentService.searchDepartmentsByName(keyword);

        if (departments.isEmpty()) {
            System.out.println("❌ No departments found matching the keyword.");
            return;
        }

        System.out.println("🔍 Matching Departments:");
        System.out.println("---------------------------------------------------");
        System.out.printf("| %-3s | %-25s | %-10s |\n", "ID", "Name", "Status");
        System.out.println("---------------------------------------------------");

        for (Department dept : departments) {
            String status = dept.getInstructors().isEmpty() ? "Deleted" : "Active";
            System.out.printf("| %-3d | %-25s | %-10s |\n", dept.getDeptId(), dept.getDeptName(), status);
        }

        System.out.println("---------------------------------------------------");
    }

    private static void restoreDepartment() {
        System.out.print("Enter Department ID to restore: ");
        int departmentId = getIntInput(); 

        // Here you would call a service method to restore the department based on the ID
        boolean success = departmentService.restoreDepartment(departmentId);
        
        if (success) {
            System.out.println("✅ Department restored successfully!");
        } else {
            System.out.println("❌ Department could not be restored. It may not exist or is not marked as deleted.");
        }
    }

    private static void viewDepartmentByName() {
        System.out.print("Enter Department Name: ");
        String deptName = scanner.nextLine();
        Department department = departmentService.getDepartmentByName(deptName);
        
        if (department != null) {
            System.out.println("\n📄 Department Details");
            System.out.println("--------------------------------------------------------------");
            System.out.println("Department ID    : " + department.getDeptId());
            System.out.println("Name             : " + department.getDeptName());
            System.out.println("Instructors      : ");
            for (Instructor instructor : department.getInstructors()) {
                System.out.println("  - " + instructor.getInstructorId() + ": " + instructor.getName() + " (Email: " + instructor.getEmail() + ")");
            }
        } else {
            System.out.println("❌ Department not found.");
        }
    }
  
    private static void assignInstructorToDepartment() {
        System.out.print("Enter Department Name: ");
        String deptName = getStringInput();

        System.out.print("Enter Instructor Name to assign: ");
        String instructorName = getStringInput();

        Department department = departmentService.getDepartmentByName(deptName);
        Instructor instructor = instructorService.getInstructorByName(instructorName);

        if (department == null) {
            System.out.println("❌ Department not found. Please check the name.");
            return;
        }

        if (instructor == null) {
            System.out.println("❌ Instructor not found. Please check the name.");
            return;
        }

        boolean success = departmentService.assignInstructorToDepartment(department.getDeptId(), instructor.getInstructorId());

        if (success) {
            Department updatedDepartment = departmentService.getDepartmentById(department.getDeptId());

            System.out.println("\n✅ Instructor assigned successfully!");
            System.out.println("\n📄 Updated Department Details:");
            System.out.println("--------------------------------------------------------------");
            System.out.printf("Department ID    : %d%n", updatedDepartment.getDeptId());
            System.out.printf("Department Name  : %s%n", updatedDepartment.getDeptName());
            System.out.printf("Status           : %s%n", updatedDepartment.getStatus() != null ? updatedDepartment.getStatus() : "Unknown");
            System.out.println("Instructors      :");

            List<Instructor> instructors = updatedDepartment.getInstructors();
            if (instructors != null && !instructors.isEmpty()) {
                for (Instructor assignedInstructor : instructors) {
                    System.out.printf("  - ID %-3d : %s%n", assignedInstructor.getInstructorId(), assignedInstructor.getName());
                }
            } else {
                System.out.println("  - No instructors assigned.");
            }
            System.out.println("--------------------------------------------------------------");

        } else {
            System.out.println("❌ Failed to assign instructor. Please try again.");
        }
    }

    private static void removeInstructorFromDepartment() {
        System.out.print("Enter Current Department Name: ");
        String currentDeptName = getStringInput();

        System.out.print("Enter Instructor Name to remove: ");
        String instructorName = getStringInput();

        System.out.print("Enter New Department Name to reassign (or leave blank to unassign): ");
        String newDeptName = getStringInputOptional(); // allow optional input

        Department currentDept = departmentService.getDepartmentByName(currentDeptName);
        Instructor instructor = instructorService.getInstructorByName(instructorName);

        if (currentDept == null) {
            System.out.println("❌ Current department not found.");
            return;
        }

        if (instructor == null) {
            System.out.println("❌ Instructor not found.");
            return;
        }

        Department newDept = null;
        if (!newDeptName.trim().isEmpty()) {
            newDept = departmentService.getDepartmentByName(newDeptName);
            if (newDept == null) {
                System.out.println("❌ New department not found.");
                return;
            }
        }

        boolean success = departmentService.removeInstructorFromDepartment(
            currentDept.getDeptId(),
            instructor.getInstructorId(),
            newDept != null ? newDept.getDeptId() : -1 // pass -1 to indicate unassign
        );

        if (success) {
            System.out.println("\n✅ Instructor reassigned successfully!");
        } else {
            System.out.println("❌ Failed to reassign instructor.");
            return;
        }

        // Show updated department(s)
        System.out.println("\n📄 Updated Department Info:");
        displayDepartmentDetails(currentDept.getDeptId());
        if (newDept != null && newDept.getDeptId() != currentDept.getDeptId()) {
            displayDepartmentDetails(newDept.getDeptId());
        }
    }

    private static void displayDepartmentDetails(int deptId) {
        Department dept = departmentService.getDepartmentById(deptId);
        System.out.println("--------------------------------------------------------------");
        System.out.printf("Department ID    : %d%n", dept.getDeptId());
        System.out.printf("Department Name  : %s%n", dept.getDeptName());
        System.out.printf("Status           : %s%n", dept.getStatus() != null ? dept.getStatus() : "Unknown");
        System.out.println("Instructors      :");
        List<Instructor> instructors = dept.getInstructors();
        if (instructors != null && !instructors.isEmpty()) {
            for (Instructor i : instructors) {
                System.out.printf("  - ID %-3d : %s%n", i.getInstructorId(), i.getName());
            }
        } else {
            System.out.println("  - No instructors assigned.");
        }
        System.out.println("--------------------------------------------------------------");
    }


    //  Added `getIntInput()` method
    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("❌ Invalid input! Please enter a valid number: ");
            }
        }
    }
    
    private static String getStringInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
    
    private static String getStringInputOptional() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().trim(); // returns empty string if user just presses Enter
    }


}
