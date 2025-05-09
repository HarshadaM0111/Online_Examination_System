package com.exam.service;

import com.exam.entities.Instructor;
import com.exam.entities.Course;

import java.util.List;

public interface InstructorService {
    Instructor createInstructor(Instructor instructor);
    Instructor getInstructorById(int instructorId);
    Instructor getInstructorByName(String name);
    List<Instructor> getAllInstructors();
    void updateInstructor(Instructor instructor);
    void deleteInstructor(int instructorId);

    // ✅ Add this
    List<Course> viewAssignedCourses(int instructorId);
}
