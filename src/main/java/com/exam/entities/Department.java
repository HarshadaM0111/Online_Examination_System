package com.exam.entities;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

@Entity // Declares this class as a JPA entity to be mapped to a database table
@Table(name = "departments") // Maps this entity to the "departments" table
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private int deptId;

    @Column(nullable = false, unique = true) // Department name must be unique and not null
    private String deptName;

    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER) 
    // One department can have many instructors; 'department' is the field in Instructor class
    private List<Instructor> instructors;

    @Column(name = "created_at") // Timestamp of when the department was created
    private LocalDateTime createdAt;

    @Column(length = 20) // Optional status field (e.g., "Active", "Inactive")
    private String status;

    // === Constructors ===

    public Department() {
        // Default constructor required by JPA
    }

    // Constructor to create a department with just a name
    public Department(String deptName) {
        this.deptName = deptName;
    }

    // Constructor with all key fields
    public Department(int deptId, String deptName, List<Instructor> instructors) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.instructors = instructors;
    }

    // === Getters and Setters ===

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public List<Instructor> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<Instructor> instructors) {
        this.instructors = instructors;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Useful for logging or debugging
    @Override
    public String toString() {
        return "Department [deptId=" + deptId +
               ", deptName=" + deptName +
               ", instructors=" + instructors +
               ", createdAt=" + createdAt +
               ", status=" + status + "]";
    }
}
