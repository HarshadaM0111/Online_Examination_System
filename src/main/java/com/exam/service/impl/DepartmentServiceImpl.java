package com.exam.service.impl;

import com.exam.dao.DepartmentDao;
import com.exam.dao.InstructorDao;
import com.exam.dao.impl.DepartmentDaoImpl;
import com.exam.dao.impl.InstructorDaoImpl;
import com.exam.entities.Department;
import com.exam.entities.Instructor;
import com.exam.service.DepartmentService;

import java.util.List;

public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentDao departmentDao = new DepartmentDaoImpl();
    private InstructorDao instructorDao = new InstructorDaoImpl(); // ✅ added

    @Override
    public Department createDepartment(Department department) {
        return departmentDao.createDepartment(department);
    }

    @Override
    public Department getDepartmentById(int departmentId) {
        return departmentDao.getDepartmentById(departmentId);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentDao.getAllDepartments();
    }

    @Override
    public void updateDepartment(Department department) {
        departmentDao.updateDepartment(department);
    }

    @Override
    public void deleteDepartment(int departmentId) {
        departmentDao.deleteDepartment(departmentId);
    }

    @Override
    public boolean departmentExistsByName(String deptName) {
        return departmentDao.departmentExistsByName(deptName);
    }

    @Override
    public List<Department> searchDepartmentsByName(String keyword) {
        return departmentDao.searchDepartmentsByName(keyword);
    }

    @Override
    public boolean restoreDepartment(int departmentId) {
        Department department = departmentDao.getDepartmentById(departmentId);
        if (department != null && "Deleted".equalsIgnoreCase(department.getStatus())) {
            department.setStatus("Active");
            departmentDao.updateDepartment(department);
            return true;
        }
        return false;
    }

    @Override
    public Department getDepartmentByName(String deptName) {
        return departmentDao.getDepartmentByName(deptName);
    }

    @Override
    public boolean assignInstructorToDepartment(int deptId, int instructorId) {
        Instructor instructor = instructorDao.getInstructorById(instructorId);
        Department department = departmentDao.getDepartmentById(deptId);
        if (instructor != null && department != null) {
            instructor.setDepartment(department);
            instructorDao.updateInstructor(instructor);
            return true;
        }
        return false;
    }

//    @Override
//    public boolean removeInstructorFromDepartment(int deptId, int instructorId) {
//        Instructor instructor = instructorDao.getInstructorById(instructorId);
//        if (instructor != null && instructor.getDepartment() != null &&
//            instructor.getDepartment().getDeptId() == deptId) {
//            instructor.setDepartment(null);
//            instructorDao.updateInstructor(instructor);
//            return true;
//        }
//        return false;
//    }
    
    @Override
    public boolean removeInstructorFromDepartment(int deptId, int instructorId, int newDepartmentId) {
        // Fetch the instructor by ID
        Instructor instructor = instructorDao.getInstructorById(instructorId);

        // Check if the instructor exists and is part of the department
        if (instructor != null && instructor.getDepartment() != null &&
            instructor.getDepartment().getDeptId() == deptId) {

            // Fetch the new department where the instructor will be reassigned
            Department newDepartment = departmentDao.getDepartmentById(newDepartmentId);

            // Check if the new department exists
            if (newDepartment != null) {
                // Reassign the instructor to the new department
                instructor.setDepartment(newDepartment);

                // Update the instructor in the database
                instructorDao.updateInstructor(instructor);
                return true; // Successfully reassigned the instructor to a new department
            } else {
                System.out.println("New department not found.");
            }
        }
        return false; // Instructor not found or not part of the department
    }

    // Other methods from the DepartmentService interface
}
