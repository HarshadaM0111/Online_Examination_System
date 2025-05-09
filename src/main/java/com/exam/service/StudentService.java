package com.exam.service;

import com.exam.entities.Student;
import java.util.List;

public interface StudentService {
    Student createStudent(Student student);
    Student getStudentById(int studentId);
    List<Student> getAllStudents();
    void updateStudent(Student student);
    void deleteStudent(int studentId);
}