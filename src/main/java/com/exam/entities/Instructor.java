package com.exam.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity // This annotation marks the class as a JPA entity, meaning it corresponds to a table in the database.
@Table(name = "instructors") // This annotation maps this entity to the "instructors" table in the database.
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment the primary key for each instructor.
    private int instructorId;

    @Column(nullable = false) // Name is required and cannot be null in the database.
    private String name;

    @Column(nullable = false, unique = true) // Email is required, unique, and cannot be duplicated in the database.
    @NotNull(message = "Email cannot be null") // Email cannot be null (validation constraint).
    private String email;

    @Column(nullable = false) // Password is mandatory.
    private String password;

    @ManyToOne // Many instructors can belong to one department.
    @JoinColumn(name = "dept_id", nullable = false) // Foreign key to the "department" table.
    private Department department;

    @OneToMany(mappedBy = "instructor", fetch = FetchType.LAZY) // One instructor can teach many courses; Lazy fetching.
    private List<Course> courses;

    // === Constructors ===
    public Instructor() {
        // Default constructor for JPA.
    }

    // Constructor with required fields (excluding the department which will be set later)
    public Instructor(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // === Getters and Setters ===

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    // Password getter and setter
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // === toString Method ===
    @Override
    public String toString() {
        return "Instructor [instructorId=" + instructorId + ", name=" + name + ", email=" + email + 
               ", department=" + (department != null ? department.getDeptName() : "N/A") + "]";
    }
}
