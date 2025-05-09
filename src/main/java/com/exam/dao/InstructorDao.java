package com.exam.dao;

import com.exam.entities.Course;
import com.exam.entities.Instructor;
import java.util.List;

public interface InstructorDao {

    // Creates and saves a new instructor in the system.
    Instructor createInstructor(Instructor instructor);

    // Retrieves an instructor by their unique ID. This is essential for individual management.
    Instructor getInstructorById(int instructorId);

    // Retrieves an instructor based on their email and password, typically used for login.
    Instructor getInstructorByEmailAndPassword(String email, String password);

    // Retrieves an instructor by their name, useful for search or filtering.
    Instructor getInstructorByName(String name);

    // Retrieves a list of all instructors in the system.
    List<Instructor> getAllInstructors();

    // Updates an instructor's details (e.g., name, email, contact info).
    void updateInstructor(Instructor instructor);

    // Deletes an instructor by their unique ID, removing them from the system.
    void deleteInstructor(int instructorId);

    // Retrieves a list of courses assigned to a specific instructor based on their ID.
    List<Course> viewAssignedCourses(int instructorId);

}
