package com.exam.entities;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import org.hibernate.annotations.BatchSize;

// This class represents a "Student" entity in the exam system, storing details about the student.
@Entity // Marks this class as a JPA entity that is mapped to a database table
@Table(name = "students") // Specifies the table name in the database for storing student records
public class Student {

    // Primary key for the Student entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generates and increments the student ID
    private int studentId;

    // First and last name of the student (required fields)
    @Column(nullable = false) // These fields are mandatory (cannot be null)
    private String firstName;

    @Column(nullable = false) // This field is required (cannot be null)
    private String lastName;

    // Email field, must be unique for each student
    @Column(nullable = false, unique = true) // This field is required and must be unique
    private String email;

    // Address of the student (required field)
    @Column(nullable = false) // This field is required (cannot be null)
    private String address;

    // Password for the student (required field)
    @Column(nullable = false) // This field is required (cannot be null)
    private String password;

    // Many-to-many relationship between students and courses
    @ManyToMany(fetch = FetchType.EAGER) // EAGER loading to fetch courses with the student data
    @BatchSize(size = 10) // Optimizes performance by batching queries (fetch 10 courses at a time)
    @JoinTable( // Specifies the join table to map the many-to-many relationship
        name = "student_courses", // Join table name
        joinColumns = @JoinColumn(name = "student_id"), // Foreign key column in the join table for student ID
        inverseJoinColumns = @JoinColumn(name = "course_id") // Foreign key column in the join table for course ID
    )
    private Set<Course> enrolledCourses = new LinkedHashSet<>(); // A set of courses the student is enrolled in

    // Many-to-many relationship between students and exams (lazy loading)
    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY) // Maps the relationship from the other side (in Exam entity)
    private List<Exam> exams;

    // Optional Fields
    @Column // Field for phone number (not required)
    private String phone;

    @Column // Field for gender (not required)
    private String gender;

    @Column // Field for Date of Birth (not required)
    private LocalDate dob;

    // Default constructor required by JPA
    public Student() {}

    // Constructor to initialize a Student object with basic fields and a collection of courses
    public Student(String firstName, String lastName, String email, String address, String password, Collection<Course> courses) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.password = password;
        this.enrolledCourses = new LinkedHashSet<>(courses); // Initialize the set with the provided courses
    }

    // Getters and Setters for each field

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Course> getEnrolledCourses() {
        return enrolledCourses; // Returns the set of courses the student is enrolled in
    }

    public void setEnrolledCourses(Collection<Course> courses) {
        this.enrolledCourses = new LinkedHashSet<>(courses); // Sets the courses using a LinkedHashSet for unique and ordered entries
    }

    public List<Exam> getExams() {
        return exams; // Returns the list of exams the student has enrolled in
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams; // Sets the list of exams
    }

    public String getPhone() {
        return phone; // Returns the student's phone number
    }

    public void setPhone(String phone) {
        this.phone = phone; // Sets the student's phone number
    }

    public String getGender() {
        return gender; // Returns the student's gender
    }

    public void setGender(String gender) {
        this.gender = gender; // Sets the student's gender
    }

    public LocalDate getDob() {
        return dob; // Returns the student's date of birth
    }

    public void setDob(LocalDate dob) {
        this.dob = dob; // Sets the student's date of birth
    }

    // Helper method to return the full name of the student
    public String getName() {
        return firstName + " " + lastName; // Concatenates first and last name
    }

    // Override the toString method to return a readable representation of the Student object
    @Override
    public String toString() {
        return "Student [studentId=" + studentId + ", firstName=" + firstName + ", lastName=" + lastName
                + ", email=" + email + ", address=" + address + ", phone=" + phone + ", gender=" + gender
                + ", dob=" + dob + ", enrolledCourses=" + enrolledCourses + "]"; // Displays key student attributes
    }
}
