package com.exam.entities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity // This marks the class as a JPA entity that maps to a database table
@Table(name = "admins") // Specifies the name of the table in the database
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing primary key
    private int adminId;

    @Column(nullable = false, unique = true)
    private String email; // Email should be unique for each admin

    @Column(nullable = false)
    private String name; // Full name of the admin

    @Column(nullable = false)
    private String password; // Password is stored in hashed format

    @Column(nullable = false)
    private String role; // Admin role (e.g., superadmin, moderator)

    @Column(nullable = false)
    private String status;  // Represents account status: "Active" or "Locked"

    @Column(nullable = true)
    private LocalDateTime lastLogin;  // Last login time of the admin

    @Column(nullable = true)
    private LocalDateTime lockedAt; // Timestamp when the admin account was locked

    @Column(nullable = true)
    private LocalDateTime unlockedAt; // Timestamp when the account was unlocked

    @Column(nullable = false)
    private int failedLoginAttempts = 0; // Counter for failed login attempts

    @Column(nullable = true)
    private LocalDateTime deletedAt;  // Used to track when the admin was soft-deleted (not physically removed)


    // Default constructor, used by JPA
    public Admin() {
        this.status = "Active"; // New accounts are active by default
        this.lastLogin = LocalDateTime.now(); // Assume registration time as first login
    }

    // Parameterized constructor to quickly initialize an admin object
    public Admin(String email, String name, String password, String role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.status = "Active";
        this.lastLogin = LocalDateTime.now();
    }

    // === Getters and Setters === //

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    // Set and hash the password before storing it
    public void setPassword(String password) {
        this.password = hashPassword(password);
    }

    // Utility method to hash passwords using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b)); // Convert bytes to hex
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // Ideally should log this exception
        }
        return null; // Return null if hashing fails
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public LocalDateTime getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(LocalDateTime lockedAt) {
        this.lockedAt = lockedAt;
    }

    public LocalDateTime getUnlockedAt() {
        return unlockedAt;
    }

    public void setUnlockedAt(LocalDateTime unlockedAt) {
        this.unlockedAt = unlockedAt;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    // Formats lastLogin into a readable string like "2025-05-05 03:30 PM"
    public String getFormattedLastLogin() {
        if (this.lastLogin != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
            return this.lastLogin.format(formatter);
        }
        return "N/A"; // Default if no last login is set
    }

    // Helpful for logging or printing object details during debugging
    @Override
    public String toString() {
        return "Admin [adminId=" + adminId + ", email=" + email + ", name=" + name + ", role=" + role
                + ", status=" + status + ", lastLogin=" + getFormattedLastLogin()
                + ", failedLoginAttempts=" + failedLoginAttempts + "]";
    }
}
