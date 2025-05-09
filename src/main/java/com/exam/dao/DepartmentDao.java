package com.exam.dao;

import com.exam.entities.Department;
import java.util.List;

public interface DepartmentDao {

    // Creates and saves a new department in the system.
    Department createDepartment(Department department);

    // Retrieves a department by its unique ID.
    Department getDepartmentById(int departmentId);

    // Returns a list of all departments currently in the system.
    List<Department> getAllDepartments();

    // Allows updating details of an existing department (e.g., name, head).
    void updateDepartment(Department department);

    // Deletes a department by its ID. This removes it from the system.
    void deleteDepartment(int departmentId);

    //  Checks if a department already exists by its name.
    // Returns true if a department with the given name exists, otherwise false.
    boolean departmentExistsByName(String deptName);
    
    // Retrieves a department by its name, useful when you know the name but not the ID.
    Department getDepartmentByName(String deptName);

    // Assigns an instructor to a department. Returns true if successful, false otherwise.
    boolean assignInstructorToDepartment(int deptId, int instructorId);

    // Removes an instructor from a department. Returns true if successful, false otherwise.
    boolean removeInstructorFromDepartment(int deptId, int instructorId);

    // Searches departments based on a keyword (e.g., part of the department's name).
    List<Department> searchDepartmentsByName(String keyword);
}
