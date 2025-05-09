package com.exam;

import com.exam.dao.AdminDao;
import com.exam.entities.Admin;
import com.exam.dao.impl.AdminDaoImpl;

public class App {
    public static void main(String[] args) {
        AdminDao adminDao = new AdminDaoImpl();

        // Create and save Admin using DAO
        Admin admin = new Admin("admin@example.com", "Admin", "password", "ADMIN");
        adminDao.createAdmin(admin);

        System.out.println("Admin saved successfully using DAO!");
    }
}