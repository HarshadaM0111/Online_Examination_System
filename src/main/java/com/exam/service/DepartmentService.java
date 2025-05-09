package com.exam.service;

import com.exam.dao.DepartmentDao;
import com.exam.dao.InstructorDao;
import com.exam.entities.Department;
import com.exam.entities.Instructor;

import java.util.List;

public interface DepartmentService {
	
    Department createDepartment(Department department);
    Department getDepartmentById(int departmentId);
    Department getDepartmentByName(String deptName);
    List<Department> getAllDepartments();
    void updateDepartment(Department department);
    void deleteDepartment(int departmentId);

    // 🔽 New method to check for duplicates
    boolean departmentExistsByName(String deptName);
    
 // New method to restore a deleted department
    boolean restoreDepartment(int departmentId);
    boolean assignInstructorToDepartment(int deptId, int instructorId);

  //  boolean removeInstructorFromDepartment(int deptId, int instructorId);

    boolean removeInstructorFromDepartment(int deptId, int instructorId, int newDepartmentId);
    
    List<Department> searchDepartmentsByName(String keyword);
    
   
}
