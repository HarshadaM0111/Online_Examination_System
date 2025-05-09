package com.exam.service.impl;

import com.exam.dao.StudentDao;
import com.exam.dao.impl.StudentDaoImpl;
import com.exam.entities.Student;
import com.exam.service.StudentService;

import java.util.List;

public class StudentServiceImpl implements StudentService {

    private StudentDao studentDao;

    public StudentServiceImpl() {
        this.studentDao = new StudentDaoImpl();
    }

    @Override
    public Student createStudent(Student student) {
        return studentDao.createStudent(student);
    }

    @Override
    public Student getStudentById(int studentId) {
        return studentDao.getStudentById(studentId);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDao.getAllStudents();
    }

    @Override
    public void updateStudent(Student student) {
        studentDao.updateStudent(student);
    }

    @Override
    public void deleteStudent(int studentId) {
        studentDao.deleteStudent(studentId);
    }
}