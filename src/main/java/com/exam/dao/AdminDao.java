package com.exam.dao;

import com.exam.entities.Admin;
import java.util.List;

public interface AdminDao {

    // Create a new admin
    Admin createAdmin(Admin admin);

    // Get an admin by their unique ID
    Admin getAdminById(int adminId);

    // Get all active admins (excluding deleted or locked ones)
    List<Admin> getAllAdmins();

    // Update an existing admin (update email, name, password, etc.)
    void updateAdmin(Admin admin);

    // Soft delete an admin by changing their status to "Deleted"
    void deleteAdmin(int adminId);

    // Restore a soft-deleted admin (e.g., status="Deleted" → "Active")
    void restoreAdmin(int adminId);

    // Search admin by keyword (email, name, or role)
    List<Admin> searchAdmin(String keyword);

    // Update the status of an admin (Active, Locked, etc.)
    void updateAdminStatus(int adminId, String status);

    // Retrieve an admin by their email (useful for login or duplicate check)
    Admin getAdminByEmail(String email);

    // Retrieve admins who are marked as deleted (for restoration purposes)
    List<Admin> getDeletedAdmins();

    // Update the last login time of an admin (used when admin logs in)
    void updateLastLogin(int adminId);
    
 // Search admins by specific role (e.g., Admin, Superadmin, Moderator, etc.)
    List<Admin> searchAdminByRole(String role);

    // Search admins by their status (Active, Locked, Deleted, etc.)
    List<Admin> searchAdminByStatus(String status);

    // Search admins by email, name, or role (general keyword-based search)
    List<Admin> searchAdminByKeyword(String keyword);

}
