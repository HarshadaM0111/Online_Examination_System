package com.exam.service;

import com.exam.entities.Admin;
import java.util.List;

public interface AdminService {

    // Admin CRUD Operations
    Admin createAdmin(Admin admin);
    Admin getAdminById(int adminId);
    List<Admin> getAllAdmins();
    void updateAdmin(Admin admin);
    void deleteAdmin(int adminId);
    List<Admin> searchAdmin(String keyword);

    // Restore a deleted admin
    void restoreAdmin(int adminId);

    // Lock and Unlock Admin Accounts
    void lockAdmin(int adminId);
    void unlockAdmin(int adminId);

    // Lookup admin by email
    Admin getAdminByEmail(String email);

    // Update admin status
    void updateAdminStatus(int adminId, String status);

    // Update last login timestamp
    void updateLastLogin(int adminId);

    // Fetch deleted admins
    List<Admin> getDeletedAdmins();

    // ✅ New search filters
    List<Admin> searchAdminByRole(String role);
    List<Admin> searchAdminByStatus(String status);
    List<Admin> searchAdminByKeyword(String keyword);
}
