package com.exam;

import com.exam.dao.impl.CourseDaoImpl;
import com.exam.dao.impl.ExamDaoImpl;
import com.exam.dao.impl.ResultDaoImpl;
import com.exam.dao.impl.StudentDaoImpl;
import com.exam.dao.impl.InstructorDaoImpl;
import com.exam.entities.*;
import com.exam.service.InstructorService;
import com.exam.service.impl.InstructorServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class InstructorMenu {

    private static final InstructorService instructorService = new InstructorServiceImpl();
    private static final InstructorDaoImpl instructorDao = new InstructorDaoImpl();
    private static final CourseDaoImpl courseDao = new CourseDaoImpl();
    private static final StudentDaoImpl studentDao = new StudentDaoImpl();
    private static final ResultDaoImpl resultDao = new ResultDaoImpl();
    private static final ExamDaoImpl examDao = new ExamDaoImpl();

    private static final Scanner scanner = new Scanner(System.in);
    private static Instructor loggedInInstructor;

    public static void main(String[] args) {
        // Start the login menu and keep asking for credentials until login is successful
        loginMenu();
    }

    private static void loginMenu() {
        boolean loggedIn = false;  // Variable to track if login is successful

        // Loop until login is successful
        while (!loggedIn) {
            System.out.print("Enter your Email (or type 'exit' to quit): ");
            String email = scanner.nextLine();
            if (email.equalsIgnoreCase("exit")) {
                System.out.println("👋 Goodbye!");
                return;  // Exit the application if user types 'exit'
            }

            System.out.print("Enter your Password: ");
            String password = scanner.nextLine();

            // Attempt to get the instructor from the database based on email and password
            loggedInInstructor = instructorDao.getInstructorByEmailAndPassword(email, password);

            if (loggedInInstructor != null) {
                System.out.println("✅ Login successful! Welcome " + loggedInInstructor.getName());
                showMenu(loggedInInstructor);  // Proceed to the instructor's menu after successful login
                loggedIn = true;  // Set loggedIn to true to exit the loop
            } else {
                System.out.println("❌ Invalid credentials. Try again.\n");
            }
        }
    }

    // Instructor Menu
    public static void showMenu(Instructor instructor) {
        int choice = -1;

        while (choice != 8) { // Change to exit when choice is 8 for logout
            System.out.println("\n===== Instructor Menu =====");
            System.out.println("1. View Assigned Courses");
            System.out.println("2. View Enrolled Students in a Course");
            System.out.println("3. Assign Grades to Students");
            System.out.println("4. Update Student Grades");
            System.out.println("5. Remove a Student from a Course");
            System.out.println("6. View/Update Personal Profile");
            System.out.println("7. Enroll Student In Courses");
            System.out.println("8. Logout");  // Changed this to 8 to resolve conflict
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        viewAssignedCourses(instructor);
                        break;
                    case 2:
                        viewEnrolledStudents(instructor, scanner);
                        break;
                    case 3:
                        assignGrades(instructor);
                        break;
                    case 4:
                        updateStudentGrade(instructor);
                        break;
                    case 5:
                        removeStudentFromCourses();
                        break;
                    case 6:
                        viewOrUpdateProfile(instructor);
                        break;
                    case 7:
                        enrollStudentInCourses();
                        break;
                    case 8:
                        System.out.println("✅ Logged out.");
                        break;
                    default:
                        System.out.println("❌ Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠ Please enter a valid number.");
            }
        }
    }


    private static void viewAssignedCourses(Instructor instructor) {
        System.out.println("\n📚 ==== Assigned Courses ====");

        List<Course> courses = instructorService.viewAssignedCourses(instructor.getInstructorId());

        if (courses == null || courses.isEmpty()) {
            System.out.println("❌ No courses have been assigned to you yet.");
            return;
        }

        System.out.printf("%-5s | %-30s\n", "ID", "Course Name");
        System.out.println("------------------------------------------------");

        for (Course course : courses) {
            System.out.printf("%-5d | %-30s\n", course.getCourseId(), course.getCourseName());
        }
    }


    public static void viewEnrolledStudents(Instructor instructor, Scanner scanner) {
        List<Course> courses = courseDao.getCoursesByInstructorId(instructor.getInstructorId());

        if (courses == null || courses.isEmpty()) {
            System.out.println("❌ No courses assigned.");
            return;
        }

        System.out.println("\n📚 ==== Select a Course to View Enrolled Students ====");
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            System.out.printf("%d. %s (Course ID: %d)\n", i + 1, course.getCourseName(), course.getCourseId());
        }

        try {
            System.out.print("\n🔢 Enter your choice (1-" + courses.size() + "): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            if (choice < 1 || choice > courses.size()) {
                System.out.println("❌ Invalid choice. Please select a valid option.");
                return;
            }

            Course selectedCourse = courses.get(choice - 1);
            List<Student> students = studentDao.getStudentsByCourseId(selectedCourse.getCourseId());

            System.out.println("\n📘 Course Selected: " + selectedCourse.getCourseName() + " (Course ID: " + selectedCourse.getCourseId() + ")");

            if (students == null || students.isEmpty()) {
                System.out.println("❌ No students enrolled in this course.");
            } else {
                System.out.println("\n👨‍🎓 Enrolled Students:");

                int index = 1;
                for (Student student : students) {
                    System.out.printf("\n%d. Student ID   : %d\n", index++, student.getStudentId());
                    System.out.printf("   Name         : %s\n", student.getName());
                    System.out.printf("   Email        : %s\n", student.getEmail());
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("❌ Invalid input. Please enter a number.");
            scanner.nextLine(); // consume invalid input
        }
    }

    private static void assignGrades(Instructor instructor) {
        viewAssignedCourses(instructor);

        try {
            System.out.print("\n🔢 Enter Course ID to assign grades: ");
            int courseId = Integer.parseInt(scanner.nextLine());

            List<Student> students = studentDao.getStudentsByCourseId(courseId);
            if (students == null || students.isEmpty()) {
                System.out.println("❌ No students enrolled in this course.");
                return;
            }

            System.out.println("\n👨‍🎓 Enrolled Students:");
            for (int i = 0; i < students.size(); i++) {
                Student s = students.get(i);
                System.out.printf("%d. %s %s (Student ID: %d)\n", i + 1, s.getFirstName(), s.getLastName(), s.getStudentId());
            }

            System.out.print("\n👉 Select a student (number): ");
            int studentIndex = Integer.parseInt(scanner.nextLine()) - 1;
            if (studentIndex < 0 || studentIndex >= students.size()) {
                System.out.println("❌ Invalid selection.");
                return;
            }
            Student student = students.get(studentIndex);

            System.out.print("📝 Enter Exam Title: ");
            String examTitle = scanner.nextLine();
            Exam exam = examDao.getExamByTitle(examTitle);

            if (exam == null || exam.getQuestions() == null || exam.getQuestions().isEmpty()) {
                System.out.println("❌ Exam not found or has no questions.");
                return;
            }

            System.out.print("🎯 Enter grade (percentage): ");
            double percentage = Double.parseDouble(scanner.nextLine());

            int totalMarks = exam.getQuestions().size();
            double score = (percentage / 100.0) * totalMarks;
            boolean passed = percentage >= 40.0;

            Result result = new Result(student, exam, score, LocalDateTime.now(), passed, percentage);
            result.setTotalMarks(totalMarks);

            resultDao.saveResult(result);
            System.out.println("✅ Grade assigned successfully for " + student.getFirstName() + " " + student.getLastName());

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number format. Please enter valid numeric values.");
        } catch (Exception e) {
            System.out.println("❌ An unexpected error occurred: " + e.getMessage());
        }
    }
    
    private static void updateStudentGrade(Instructor instructor) {
        try {
            System.out.print("🔎 Enter Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());

            List<Result> results = resultDao.getResultsByStudentId(studentId);

            if (results == null || results.isEmpty()) {
                System.out.println("❌ No results found for this student.");
                return;
            }

            // Ask if the user wants to view only or update grades
            System.out.print("\nWould you like to (1) View Only or (2) Update Grade? Enter choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            System.out.println("\n📄 ==== Student Exam Results ====");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

            for (int i = 0; i < results.size(); i++) {
                Result r = results.get(i);
                String courseName = r.getExam().getCourse();
                String examTitle = r.getExam().getTitle();
                String attemptDate = r.getAttemptDate() != null ? r.getAttemptDate().format(formatter) : "N/A";

                System.out.printf("\n%d. 📘 Course: %s\n", i + 1, courseName);
                System.out.printf("   📝 Exam: %s\n", examTitle);
                System.out.printf("   📊 Score: %.2f%%\n", r.getScore());
                System.out.printf("   📅 Attempted On: %s\n", attemptDate);
            }

            if (choice == 1) {
                System.out.println("\n🔹 View mode selected. No changes made.");
                return;
            } else if (choice != 2) {
                System.out.println("❌ Invalid choice. Exiting.");
                return;
            }

            // Proceed with updating the grade
            System.out.print("\n🔢 Select result to update (1-" + results.size() + "): ");
            int index = Integer.parseInt(scanner.nextLine()) - 1;

            if (index < 0 || index >= results.size()) {
                System.out.println("❌ Invalid selection.");
                return;
            }

            Result result = results.get(index);

            System.out.print("✏️ Enter new grade (percentage): ");
            double newPercentage = Double.parseDouble(scanner.nextLine());

            if (newPercentage < 0 || newPercentage > 100) {
                System.out.println("❌ Invalid percentage. Must be between 0 and 100.");
                return;
            }

            double newScore = (newPercentage / 100.0) * result.getTotalMarks();
            boolean passed = newPercentage >= 40.0;

            // Confirm update
            System.out.print("✅ Confirm update? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (!confirm.equals("yes")) {
                System.out.println("⚠️ Update cancelled.");
                return;
            }

            result.setScore(newScore);
            result.setPercentage(newPercentage);
            result.setPassed(passed);
            result.setAttemptDate(LocalDateTime.now());  // Update date to now

            resultDao.updateResult(result);
            System.out.println("\n🎉 Grade updated successfully!");
            System.out.println("👤 Updated by: " + instructor.getName());
            System.out.println("🕒 Updated on: " + LocalDateTime.now().format(formatter));

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static void removeStudentFromCourses() {
        System.out.print("🔎 Enter student ID: ");
        int studentId = Integer.parseInt(scanner.nextLine());

        Student student = studentDao.getStudentById(studentId);
        if (student == null) {
            System.out.println("❌ Student not found.");
            return;
        }

        // Display basic info
        System.out.printf("\n👤 Student: %s %s | 📧 %s\n", student.getFirstName(), student.getLastName(), student.getEmail());

        List<Course> enrolledCourses = courseDao.getCoursesByStudentId(studentId);
        if (enrolledCourses == null || enrolledCourses.isEmpty()) {
            System.out.println("⚠️ This student is not enrolled in any courses.");
            return;
        }

        System.out.println("\n📘 Enrolled Courses:");
        for (int i = 0; i < enrolledCourses.size(); i++) {
            Course course = enrolledCourses.get(i);
            System.out.printf("  %d. %s (Course ID: %d)\n", i + 1, course.getCourseName(), course.getCourseId());
        }

        System.out.print("\n🔢 Enter the course numbers to remove the student from (comma-separated): ");
        String[] input = scanner.nextLine().split(",");
        List<Integer> courseIndexes = new ArrayList<>();

        for (String str : input) {
            try {
                int index = Integer.parseInt(str.trim()) - 1;
                if (index >= 0 && index < enrolledCourses.size()) {
                    courseIndexes.add(index);
                } else {
                    System.out.println("❗ Invalid course number: " + (index + 1));
                }
            } catch (NumberFormatException e) {
                System.out.println("❗ Invalid input: " + str.trim());
            }
        }

        if (courseIndexes.isEmpty()) {
            System.out.println("❌ No valid course numbers selected.");
            return;
        }

        System.out.print("\n⚠️ Are you sure you want to remove the student from the selected courses? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (!confirm.equals("yes")) {
            System.out.println("❌ Operation cancelled.");
            return;
        }

        for (int index : courseIndexes) {
            Course course = enrolledCourses.get(index);

            // Optional instructor check
            if (loggedInInstructor != null && course.getInstructorId() != loggedInInstructor.getInstructorId()) {
                System.out.printf("❌ Unauthorized to remove from course: %s (ID: %d)\n", course.getCourseName(), course.getCourseId());
                continue;
            }

            boolean removed = studentDao.removeStudentFromCourse(studentId, course.getCourseId());
            if (removed) {
                System.out.printf("✅ Removed from: %s (Course ID: %d)\n", course.getCourseName(), course.getCourseId());
            } else {
                System.out.printf("❌ Failed to remove from: %s (Course ID: %d)\n", course.getCourseName(), course.getCourseId());
            }
        }

        System.out.println("🕒 Operation completed at " + LocalDateTime.now());
    }

    private static void enrollStudentInCourses() {
        System.out.print("🔎 Enter student ID: ");
        int studentId = Integer.parseInt(scanner.nextLine());

        Student student = studentDao.getStudentById(studentId);
        if (student == null) {
            System.out.println("❌ Student not found.");
            return;
        }

        System.out.printf("\n👤 Student: %s %s | 📧 %s\n", student.getFirstName(), student.getLastName(), student.getEmail());

        List<Course> allCourses = courseDao.getAllCourses();
        List<Course> enrolledCourses = courseDao.getCoursesByStudentId(studentId);

        if (allCourses == null || allCourses.isEmpty()) {
            System.out.println("⚠️ No available courses found.");
            return;
        }

        // Remove already enrolled courses
        List<Course> availableCourses = new ArrayList<>(allCourses);
        if (enrolledCourses != null) {
            availableCourses.removeAll(enrolledCourses);
        }

        if (availableCourses.isEmpty()) {
            System.out.println("⚠️ Student is already enrolled in all available courses.");
            return;
        }

        System.out.println("\n📚 Available Courses:");
        for (int i = 0; i < availableCourses.size(); i++) {
            Course course = availableCourses.get(i);
            System.out.printf("  %d. %s (Course ID: %d)\n", i + 1, course.getCourseName(), course.getCourseId());
        }

        System.out.print("\n🔢 Enter course numbers to enroll the student (comma-separated): ");
        String[] input = scanner.nextLine().split(",");
        List<Integer> selectedIndexes = new ArrayList<>();

        for (String str : input) {
            try {
                int index = Integer.parseInt(str.trim()) - 1;
                if (index >= 0 && index < availableCourses.size()) {
                    selectedIndexes.add(index);
                } else {
                    System.out.println("❗ Invalid course number: " + (index + 1));
                }
            } catch (NumberFormatException e) {
                System.out.println("❗ Invalid input: " + str.trim());
            }
        }

        if (selectedIndexes.isEmpty()) {
            System.out.println("❌ No valid courses selected.");
            return;
        }

        System.out.print("\n✅ Confirm enrollment in selected courses? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (!confirm.equals("yes")) {
            System.out.println("❌ Enrollment cancelled.");
            return;
        }

        for (int index : selectedIndexes) {
            Course course = availableCourses.get(index);
            boolean enrolled = studentDao.enrollStudentInCourse(studentId, course.getCourseId());
            if (enrolled) {
                System.out.printf("✅ Enrolled in: %s (Course ID: %d)\n", course.getCourseName(), course.getCourseId());
            } else {
                System.out.printf("❌ Failed to enroll in: %s (Course ID: %d)\n", course.getCourseName(), course.getCourseId());
            }
        }

        System.out.println("🕒 Enrollment process completed at " + LocalDateTime.now());
    }

    
    private static void viewOrUpdateProfile(Instructor instructor) {
        System.out.println("\n📋 Your Profile:");
        System.out.println("ID: " + instructor.getInstructorId());
        System.out.println("Name: " + instructor.getName());
        System.out.println("Email: " + instructor.getEmail());

        System.out.print("Update profile? (yes/no): ");
        String response = scanner.nextLine();
        if (response.equalsIgnoreCase("yes")) {
            System.out.print("New name: ");
            instructor.setName(scanner.nextLine());

            System.out.print("New email: ");
            instructor.setEmail(scanner.nextLine());

            instructorService.updateInstructor(instructor);
            System.out.println("✅ Profile updated.");
        }
    }
}
