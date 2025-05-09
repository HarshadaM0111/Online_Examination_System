package com.exam.service.impl;

import com.exam.dao.InstructorDao;
import com.exam.entities.Course;
import com.exam.entities.Instructor;
import com.exam.service.InstructorService;
import com.exam.dao.CourseDao;
import com.exam.dao.impl.CourseDaoImpl;

import com.exam.dao.impl.InstructorDaoImpl;

import java.util.List;

public class InstructorServiceImpl implements InstructorService {

    private InstructorDao instructorDao = new InstructorDaoImpl();
    private CourseDao courseDao = new CourseDaoImpl();

//    @Override
//    public Instructor createInstructor(Instructor instructor) {
//        return instructorDao.createInstructor(instructor);
//    }
    
    @Override
    public Instructor createInstructor(Instructor instructor) {
        if (instructor.getPassword() == null || instructor.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

       
        return instructorDao.createInstructor(instructor);
    }


    @Override
    public Instructor getInstructorById(int instructorId) {
        return instructorDao.getInstructorById(instructorId);
    }

    @Override
    public List<Instructor> getAllInstructors() {
        return instructorDao.getAllInstructors();
    }
    
    @Override
    public List<Course> viewAssignedCourses(int instructorId) {
        return courseDao.getCoursesByInstructorId(instructorId);
    }

    @Override
    public void updateInstructor(Instructor instructor) {
        instructorDao.updateInstructor(instructor);
    }

    @Override
    public void deleteInstructor(int instructorId) {
        instructorDao.deleteInstructor(instructorId);
    }
    
    @Override
    public Instructor getInstructorByName(String name) {
        return instructorDao.getInstructorByName(name); // Assuming you have this method in InstructorDao
    }
    
}

