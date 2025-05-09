package com.exam.manager;

import com.exam.entities.Admin;
import com.exam.service.AdminService;
import com.exam.service.impl.AdminServiceImpl;
import com.exam.util.AuditLogger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminManager {
    static Scanner scanner = new Scanner(System.in);
    static AdminService adminService = new AdminServiceImpl();

    public static void manageAdmins() {
        while (true) {
            System.out.println("\n==== Admin Management ====");
            System.out.println("1. Add Admin");
            System.out.println("2. View Admin by ID");
            System.out.println("3. View All Admins");
            System.out.println("4. Search Admin");
            System.out.println("5. Update Admin");
            System.out.println("6. Delete Admin");
            System.out.println("7. Restore Deleted Admin");
            System.out.println("8. Lock Admin Account");
            System.out.println("9. Unlock Admin Account");
            System.out.println("10. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> addAdmin();
                case 2 -> viewAdmin();
                case 3 -> viewAllAdmins();
                case 4 -> searchAdmin();
                case 5 -> updateAdmin();
                case 6 -> deleteAdmin();
                case 7 -> restoreDeletedAdmin();
                case 8 -> lockAdminAccount();
                case 9 -> unlockAdminAccount();
                case 10 -> { return; }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void addAdmin() {
        System.out.println("\n==== Add New Admin ====");
        
        // Get Admin Email
        System.out.print("Enter Admin Email: ");
        String email = scanner.next();
        
        // Validate Email format (basic check)
        if (!email.contains("@") || !email.contains(".")) {
            System.out.println("❌ Invalid email format. Please try again.");
            return;
        }

        // Get Admin Name
        System.out.print("Enter Admin Name: ");
        String name = scanner.next();

        // Get Admin Password
        System.out.print("Enter Admin Password: ");
        String password = scanner.next();
        
        // Show the Role Selection Menu
        System.out.println("\n==== Select Admin Role ====");
        System.out.println("1. Superadmin");
        System.out.println("2. Admin");
        System.out.println("3. Moderator");
        System.out.println("4. Editor");
        System.out.println("5. Viewer");
        System.out.print("Enter your choice (1-5): ");
        int roleChoice = scanner.nextInt();

        // Set the role based on user selection
        String role = null;
        switch (roleChoice) {
            case 1:
                role = "superadmin";
                break;
            case 2:
                role = "admin";
                break;
            case 3:
                role = "moderator";
                break;
            case 4:
                role = "editor";
                break;
            case 5:
                role = "viewer";
                break;
            default:
                System.out.println("❌ Invalid role choice. Please select a valid option (1-5).");
                return;
        }

        // Create the Admin object
        Admin newAdmin = new Admin(email, name, password, role);
        newAdmin.setStatus("Active");  // Default to Active
        newAdmin.setLastLogin(null);   // Last login is null initially
        
        // Add the admin using the service
        Admin addedAdmin = adminService.createAdmin(newAdmin);  // Returns Admin object, not a boolean
        
        // Provide feedback to the user
        if (addedAdmin != null) {
            System.out.println("\n✅ Admin added successfully!");
            
            // Optionally display the newly added admin's information in a clean format
            System.out.println("\nNew Admin Details:");
            System.out.println("-----------------------------------------------------------");
            System.out.printf("| %-15s | %-40s |\n", "Admin ID", addedAdmin.getAdminId());
            System.out.printf("| %-15s | %-40s |\n", "Name", addedAdmin.getName());
            System.out.printf("| %-15s | %-40s |\n", "Email", addedAdmin.getEmail());
            System.out.printf("| %-15s | %-40s |\n", "Role", addedAdmin.getRole());
            System.out.printf("| %-15s | %-40s |\n", "Status", addedAdmin.getStatus());
            System.out.printf("| %-15s | %-40s |\n", "Last Login", addedAdmin.getLastLogin() != null ? addedAdmin.getLastLogin().toString() : "N/A");
            System.out.println("-----------------------------------------------------------");
        } else {
            System.out.println("❌ Error: Admin could not be added. Please try again.");
        }
    }



    private static void viewAdmin() {
        // Prompt user for the Admin ID
        System.out.print("Enter Admin ID: ");
        int adminId = getIntInput();  // Get the input as integer

        // Retrieve the admin details by ID
        Admin admin = adminService.getAdminById(adminId);

        // Check if the admin is found and print in a formatted way
        if (admin != null) {
            System.out.println("\n==== Admin Details ====");
            System.out.println("-----------------------------------------------------------");
            System.out.printf("| %-15s | %-40s |\n", "Admin ID", admin.getAdminId());
            System.out.printf("| %-15s | %-40s |\n", "Name", admin.getName());
            System.out.printf("| %-15s | %-40s |\n", "Email", admin.getEmail());
            System.out.printf("| %-15s | %-40s |\n", "Role", admin.getRole());
            System.out.printf("| %-15s | %-40s |\n", "Status", admin.getStatus());
            System.out.printf("| %-15s | %-40s |\n", "Last Login", admin.getLastLogin() != null ? admin.getLastLogin().toString() : "N/A");
            System.out.println("-----------------------------------------------------------");
        } else {
            // If admin is not found, display a user-friendly message
            System.out.println("❌ Admin with ID " + adminId + " not found.");
        }
    }


    private static void viewAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();
        if (admins.isEmpty()) {
            System.out.println("❌ No admins found.");
        } else {
            // Print table header
            System.out.println("==== View All Admins ====");
            System.out.println("-----------------------------------------------------");
            System.out.printf("| %-4s | %-15s | %-25s | %-12s | %-8s | %-20s |\n", "ID", "Name", "Email", "Role", "Status", "Last Login");
            System.out.println("-----------------------------------------------------");

            // Iterate through the list of admins and print each admin in a row
            for (Admin admin : admins) {
                System.out.printf("| %-4d | %-15s | %-25s | %-12s | %-8s | %-20s |\n",
                        admin.getAdminId(), admin.getName(), admin.getEmail(), admin.getRole(), admin.getStatus(), admin.getLastLogin());
            }

            System.out.println("-----------------------------------------------------");
        }
    }
    
    private static void searchAdmin() {
        System.out.println("\n==== Search Admin ====");
        System.out.println("Select a filter to search by:");
        System.out.println("1. Search by Role");
        System.out.println("2. Search by Status");
        System.out.println("3. Search by Email/Name/Role");
        System.out.print("Enter your choice (1/2/3): ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        List<Admin> admins = new ArrayList<>();

        switch (choice) {
            case 1: // Search by Role
                System.out.println("\nSelect a Role to search for:");
                System.out.println("1. Superadmin");
                System.out.println("2. Admin");
                System.out.println("3. Moderator");
                System.out.println("4. Editor");
                System.out.println("5. Viewer");
                System.out.print("Enter your choice (1-5): ");
                int roleChoice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                String[] roles = {"superadmin", "admin", "moderator", "editor", "viewer"};
                if (roleChoice < 1 || roleChoice > 5) {
                    System.out.println("❌ Invalid role choice! Returning to main menu.");
                    return;
                }
                admins = adminService.searchAdminByRole(roles[roleChoice - 1]);
                break;

            case 2: // Search by Status
                System.out.println("\nSelect a Status to search for:");
                System.out.println("1. Active");
                System.out.println("2. Inactive");
                System.out.print("Enter your choice (1/2): ");
                int statusChoice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                if (statusChoice != 1 && statusChoice != 2) {
                    System.out.println("❌ Invalid status choice! Returning to main menu.");
                    return;
                }
                String status = (statusChoice == 1) ? "Active" : "Inactive";
                admins = adminService.searchAdminByStatus(status);
                break;

            case 3: // Search by Keyword
                System.out.print("\nEnter keyword to search (email/name/role): ");
                String keyword = scanner.nextLine().trim();
                if (keyword.isEmpty()) {
                    System.out.println("❌ Keyword cannot be empty!");
                    return;
                }
                admins = adminService.searchAdminByKeyword(keyword);
                break;

            default:
                System.out.println("❌ Invalid choice! Returning to main menu.");
                return;
        }

        // Display results
        if (admins == null || admins.isEmpty()) {
            System.out.println("❌ No admins found matching your search criteria.");
        } else {
            System.out.println("\n✅ Search Results:");
            displayAdminResults(admins);
        }
    }


    // Helper method to display the search results in a clean format
    private static void displayAdminResults(List<Admin> admins) {
        System.out.println("--------------------------------------------------------------");
        System.out.printf("| %-5s | %-25s | %-20s | %-10s | %-10s | %-20s |\n", "ID", "Name", "Email", "Role", "Status", "Last Login");
        System.out.println("--------------------------------------------------------------");
        
        for (Admin admin : admins) {
            String lastLogin = admin.getLastLogin() != null ? admin.getLastLogin().toString() : "N/A";
            System.out.printf("| %-5d | %-25s | %-20s | %-10s | %-10s | %-20s |\n", 
                    admin.getAdminId(), admin.getName(), admin.getEmail(), admin.getRole(), admin.getStatus(), lastLogin);
        }
        System.out.println("--------------------------------------------------------------");
    }


    private static void updateAdmin() {
        System.out.print("Enter Admin ID to update: ");
        int adminId = getIntInput();
        Admin admin = adminService.getAdminById(adminId);

        if (admin != null) {
            System.out.println("\n==== Update Admin Details ====");

            // Ask what the user wants to update
            System.out.println("Which detail would you like to update?");
            System.out.println("1. Email");
            System.out.println("2. Name");
            System.out.println("3. Password");
            System.out.println("4. Role");
            System.out.print("Enter choice (1-4): ");
            int updateChoice = getIntInput();

            // Update selected detail
            switch (updateChoice) {
                case 1:
                    System.out.print("Enter New Email: ");
                    admin.setEmail(scanner.next());
                    System.out.println("✅ Email updated successfully!");
                    break;

                case 2:
                    System.out.print("Enter New Name: ");
                    admin.setName(scanner.next());
                    System.out.println("✅ Name updated successfully!");
                    break;

                case 3:
                    System.out.print("Enter New Password: ");
                    admin.setPassword(scanner.next());
                    System.out.println("✅ Password updated successfully!");
                    break;

                case 4:
                    System.out.print("Enter New Role: ");
                    admin.setRole(scanner.next());
                    System.out.println("✅ Role updated successfully!");
                    break;

                default:
                    System.out.println("❌ Invalid choice! No updates made.");
                    return;
            }

            // After updating the field, update the admin in the system
            adminService.updateAdmin(admin);

            // Show the updated admin details
            System.out.println("\nUpdated Admin Information:");
            System.out.println("----------------------------");
            System.out.println("Admin ID: " + admin.getAdminId());
            if (admin.getEmail() != null) System.out.println("Email: " + admin.getEmail());
            if (admin.getName() != null) System.out.println("Name: " + admin.getName());
            if (admin.getPassword() != null) System.out.println("Password: **********");
            if (admin.getRole() != null) System.out.println("Role: " + admin.getRole());
            System.out.println("----------------------------");
            System.out.println("✅ Admin updated successfully!");

        } else {
            System.out.println("❌ Admin not found.");
        }
    }



    private static void deleteAdmin() {
        System.out.print("Enter Admin ID to delete: ");
        int adminId = getIntInput();
        
        // Fetch admin by ID
        Admin admin = adminService.getAdminById(adminId);

        // Check if admin exists
        if (admin == null) {
            System.out.println("❌ Admin with ID " + adminId + " not found.");
            return;
        }

        // Prevent deletion of active or locked admins
        if ("Active".equalsIgnoreCase(admin.getStatus()) || "Locked".equalsIgnoreCase(admin.getStatus())) {
            System.out.println("⚠️ Admin cannot be deleted while active or locked.");
            return;
        }

        // If admin is already deleted, notify the user
        if ("Deleted".equalsIgnoreCase(admin.getStatus())) {
            System.out.println("⚠️ Admin is already deleted.");
            return;
        }

        // Proceed with "soft delete" (mark as deleted)
        admin.setStatus("Deleted");
        admin.setDeletedAt(LocalDateTime.now());  // Optionally, set the deletion time
        adminService.updateAdmin(admin);

        // Log the action (Admin Name, Admin ID, and Action Taken)
        String actionTakenBy = "Admin Name: " + admin.getName() + " (ID: " + admin.getAdminId() + ")";
        AuditLogger.logAction(admin.getAdminId(), admin.getName(), "Deleted");

        // Show success message with admin's details
        System.out.println("✅ Admin with ID " + adminId + " has been successfully marked as deleted.");
        System.out.println("\n[Admin Deleted Information]");
        System.out.println("----------------------------");
        System.out.println("Admin ID: " + admin.getAdminId());
        System.out.println("Name: " + admin.getName());
        System.out.println("Email: " + admin.getEmail());
        System.out.println("Status: " + admin.getStatus());
        System.out.println("Deleted At: " + admin.getDeletedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        System.out.println("----------------------------");
    }


    private static void restoreDeletedAdmin() {
        System.out.print("Enter Admin ID to restore: ");
        int adminId = getIntInput();
        adminService.restoreAdmin(adminId);
        System.out.println("✅ Admin restored successfully!");
    }

    private static void lockAdminAccount() {
        // Display header
        printSectionHeader("LOCK ADMIN ACCOUNT");

        // Input Admin ID
        System.out.print("Enter Admin ID to lock: ");
        int adminId = getIntInput();

        Admin adminToLock = adminService.getAdminById(adminId);

        if (adminToLock == null) {
            System.out.println("❌ Admin with ID " + adminId + " not found.");
            return;
        }

        // Check if the admin is already locked
        if ("Locked".equalsIgnoreCase(adminToLock.getStatus())) {
            System.out.println("⚠️ Admin is already locked.");
            printTimestamp("Locked At", adminToLock.getLockedAt());
            return;
        }

        // Prevent locking deleted admins
        if ("Deleted".equalsIgnoreCase(adminToLock.getStatus())) {
            System.out.println("❌ Cannot lock a deleted admin.");
            return;
        }

        // Lock the admin account
        adminToLock.setStatus("Locked");
        adminToLock.setLockedAt(LocalDateTime.now());
        adminService.updateAdmin(adminToLock);

        // Log the action
        AuditLogger.logAction(adminToLock.getAdminId(), adminToLock.getName(), "Locked");

        // Successful lock message
        System.out.println("\n✅ Admin '" + adminToLock.getName() + "' is now locked.");
        printTimestamp("🔒 Locked At", adminToLock.getLockedAt());
        printDivider();
    }

    private static void unlockAdminAccount() {
        // Display header
        printSectionHeader("UNLOCK ADMIN ACCOUNT");

        // Input Admin ID
        System.out.print("Enter Admin ID to unlock: ");
        int adminId = getIntInput();

        Admin adminToUnlock = adminService.getAdminById(adminId);

        if (adminToUnlock == null) {
            System.out.println("❌ Admin with ID " + adminId + " not found.");
            return;
        }

        // Check if the admin is already active
        if ("Active".equalsIgnoreCase(adminToUnlock.getStatus())) {
            System.out.println("⚠️ Admin is already active (not locked).");
            return;
        }

        // Prevent unlocking deleted admins
        if ("Deleted".equalsIgnoreCase(adminToUnlock.getStatus())) {
            System.out.println("❌ Cannot unlock a deleted admin.");
            return;
        }

        // Unlock the admin account
        adminToUnlock.setStatus("Active");
        adminToUnlock.setUnlockedAt(LocalDateTime.now());
        adminToUnlock.setFailedLoginAttempts(0); // Reset failed login attempts
        adminService.updateAdmin(adminToUnlock);

        // Log the action
        AuditLogger.logAction(adminToUnlock.getAdminId(), adminToUnlock.getName(), "Unlocked");

        // Successful unlock message
        System.out.println("\n✅ Admin '" + adminToUnlock.getName() + "' is now unlocked.");
        printTimestamp("🔓 Unlocked At", adminToUnlock.getUnlockedAt());
        printDivider();
    }

 // Prints a header section to clearly separate different actions in the console
    private static void printSectionHeader(String title) {
        System.out.println("\n---------- " + title.toUpperCase() + " ----------");
    }

    // Prints a divider to visually separate sections in the console
    private static void printDivider() {
        System.out.println("-------------------------------------------\n");
    }

    // Formats and prints the timestamp in a human-readable way
    private static void printTimestamp(String label, LocalDateTime timestamp) {
        if (timestamp != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a");
            System.out.println(label + ": " + timestamp.format(formatter));
        } else {
            System.out.println(label + ": N/A");
        }
    }


    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input! Enter a valid number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }
}
