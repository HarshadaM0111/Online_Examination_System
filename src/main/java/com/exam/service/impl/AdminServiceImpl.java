package com.exam.service.impl;

import com.exam.dao.AdminDao;
import com.exam.entities.Admin;
import com.exam.service.AdminService;
import com.exam.dao.impl.AdminDaoImpl;

import java.time.LocalDateTime;
import java.util.List;

public class AdminServiceImpl implements AdminService {

    private AdminDao adminDao = new AdminDaoImpl();  // DAO handles persistence

    @Override
    public Admin createAdmin(Admin admin) {
        return adminDao.createAdmin(admin);
    }

    @Override
    public Admin getAdminById(int adminId) {
        return adminDao.getAdminById(adminId);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminDao.getAllAdmins();
    }

    @Override
    public void updateAdmin(Admin admin) {
        adminDao.updateAdmin(admin);
    }

    @Override
    public void deleteAdmin(int adminId) {
        adminDao.deleteAdmin(adminId);
    }

    @Override
    public List<Admin> searchAdmin(String keyword) {
        return adminDao.searchAdmin(keyword);
    }

    @Override
    public void restoreAdmin(int adminId) {
        adminDao.restoreAdmin(adminId);
    }

    @Override
    public void lockAdmin(int adminId) {
        adminDao.updateAdminStatus(adminId, "Locked");
    }

    @Override
    public void unlockAdmin(int adminId) {
        adminDao.updateAdminStatus(adminId, "Active");
    }

    @Override
    public Admin getAdminByEmail(String email) {
        return adminDao.getAdminByEmail(email);
    }
    
    @Override
    public void updateAdminStatus(int adminId, String status) {
        adminDao.updateAdminStatus(adminId, status);
    }

    // Update last login timestamp for the admin
    @Override
    public void updateLastLogin(int adminId) {
        Admin admin = adminDao.getAdminById(adminId);
        if (admin != null) {
            // Set the last login time to the current LocalDateTime
            admin.setLastLogin(LocalDateTime.now());
            adminDao.updateAdmin(admin);  // Save the updated admin object
        }
    }

    @Override
    public List<Admin> getDeletedAdmins() {
        return adminDao.getDeletedAdmins();
    }
    
    @Override
    public List<Admin> searchAdminByRole(String role) {
        return adminDao.searchAdminByRole(role);
    }

    @Override
    public List<Admin> searchAdminByStatus(String status) {
        return adminDao.searchAdminByStatus(status);
    }

    @Override
    public List<Admin> searchAdminByKeyword(String keyword) {
        return adminDao.searchAdminByKeyword(keyword);
    }

}
