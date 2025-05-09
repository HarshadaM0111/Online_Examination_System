package com.exam.dao;

import com.exam.entities.Course;
import java.util.List;

public interface CourseDao {

    // This method allows you to create and save a new course to the database.
    Course createCourse(Course course);

    // If you need to retrieve a course by its unique ID, this method will help you fetch it.
    Course getCourseById(int courseId);

    // Want to get a list of all courses? This method will return all the courses from the database.
    List<Course> getAllCourses();

    // If you're looking for courses taught by a specific instructor, this will give you that list.
    List<Course> getCoursesByInstructorId(int instructorId); // ✅ NEW

    // Need to update a course's details? This method allows you to modify any course information.
    void updateCourse(Course course);

    // This method deletes a course from the system by its ID, essentially removing it.
    void deleteCourse(int courseId);

    // Want to search courses by their name? This method helps you find courses with a matching name.
    List<Course> searchCoursesByName(String name);

    // This method returns a list of courses associated with a specific student by their ID.
    List<Course> getCoursesByStudentId(int studentId);
}
