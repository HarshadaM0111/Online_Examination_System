package com.exam.service;

import com.exam.entities.Course;
import java.util.List;

public interface CourseService {
    Course createCourse(Course course);
    Course getCourseById(int courseId);
    List<Course> getAllCourses();
    Course getCourseByName(String courseName); // <-- Added this line
    void updateCourse(Course course);
    void deleteCourse(int courseId);
    
    List<Course> searchCoursesByName(String name);
    List<Course> getCoursesByInstructorId(int instructorId);
}
