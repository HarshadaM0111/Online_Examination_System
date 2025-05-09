package com.exam.manager;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import com.exam.entities.Course;
import com.exam.entities.Student;
import com.exam.service.CourseService;
import com.exam.service.StudentService;
import com.exam.service.impl.CourseServiceImpl;
import com.exam.service.impl.StudentServiceImpl;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentManager {
    static Scanner scanner = new Scanner(System.in);
    static StudentService studentService = new StudentServiceImpl();
    static CourseService courseService = new CourseServiceImpl();

    public static void manageStudents() {
        while (true) {
            System.out.println("\n==== Student Management ====");
            System.out.println("1. Add Student");
            System.out.println("2. View Student by ID");
            System.out.println("3. View All Students");
            System.out.println("4. Search Student by Name/Email");
            System.out.println("5. View Students by Course");
            System.out.println("6. Update Student");
            System.out.println("7. Delete Student");
            System.out.println("8. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewStudentById();
                case 3 -> viewAllStudents();
                case 4 -> searchStudent();
                case 5 -> viewStudentsByCourse();
                case 6 -> updateStudent();
                case 7 -> deleteStudent();
                case 8 -> {
                    return;
                }
                default -> System.out.println("❌ Invalid choice! Try again.");
            }
        }
    }

    private static void addStudent() {
        // Prompt user for student details
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        // Optional profile fields
        System.out.print("Enter Phone Number (Optional): ");
        String phone = scanner.nextLine();

        System.out.print("Enter Gender (Optional): ");
        String gender = scanner.nextLine();

        System.out.print("Enter Date of Birth (Optional) (YYYY-MM-DD): ");
        String dobStr = scanner.nextLine();
        LocalDate dob = null;
        if (!dobStr.isEmpty()) {
            try {
                dob = LocalDate.parse(dobStr);
            } catch (Exception e) {
                System.out.println("❌ Invalid Date format. Skipping Date of Birth.");
            }
        }

        // Load all available courses for selection
        List<Course> allCourses = courseService.getAllCourses();

        Set<Course> enrolledCoursesSet = new LinkedHashSet<>();
        if (!allCourses.isEmpty()) {
            System.out.println("\n📚 Available Courses:");
            System.out.printf("%-5s%-30s\n", "ID", "Course Name");
            System.out.println("--------------------------------------------------");

            // Display available courses
            for (Course course : allCourses) {
                System.out.printf("%-5d%-30s\n", course.getCourseId(), course.getCourseName());
            }
            System.out.println("Enter Course IDs to enroll (comma-separated): ");
            String[] courseIds = scanner.nextLine().split(",");

            // Enroll in selected courses
            for (String idStr : courseIds) {
                try {
                    int courseId = Integer.parseInt(idStr.trim());
                    Course course = courseService.getCourseById(courseId);
                    if (course != null) {
                        if (!enrolledCoursesSet.contains(course)) {
                            enrolledCoursesSet.add(course);
                        } else {
                            System.out.println("⚠️ Course already selected: " + course.getCourseName());
                        }
                    } else {
                        System.out.println("❌ Course ID " + courseId + " not found!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid course ID format: " + idStr.trim());
                }
            }
        } else {
            System.out.println("⚠️ No courses available to enroll at the moment.");
        }

        // Create and save the new student
        Student student = new Student(firstName, lastName, email, address, password, new ArrayList<>(enrolledCoursesSet));
        student.setPhone(phone);
        student.setGender(gender);
        student.setDob(dob);

        studentService.createStudent(student);
        System.out.printf("✅ Student \"%s %s\" added successfully!\n", firstName, lastName);

        // Display enrolled courses
        if (!enrolledCoursesSet.isEmpty()) {
            System.out.print("📚 Enrolled in: ");
            List<String> courseSummaries = new ArrayList<>();
            for (Course course : enrolledCoursesSet) {
                courseSummaries.add(course.getCourseName() + " (Course ID: " + course.getCourseId() + ")");
            }
            System.out.println(String.join(", ", courseSummaries));
        } else {
            System.out.println("📚 No courses enrolled.");
        }

        // After adding the student, ask for further actions
        System.out.println("\nWhat would you like to do next?");
        System.out.println("1. Add another student");
        System.out.println("2. Back to Student Menu");
        int choice = getIntInput();
        if (choice == 1) {
            addStudent(); // Recursively call the method to add another student
        }
    }


    
    private static void viewAllStudents() {
        List<Student> students = studentService.getAllStudents();

        if (students != null && !students.isEmpty()) {
            // Print the table header with wider spaces for 'Courses Enrolled' and 'Password'
            System.out.printf("%-10s%-20s%-30s%-20s%-40s%-20s\n", "ID", "Name", "Email", "Address", "Courses Enrolled", "Password");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------");

            // Print details for each student with wider spaces
            for (Student student : students) {
                System.out.printf("%-10d%-20s%-30s%-20s%-40s%-20s\n",
                        student.getStudentId(),
                        student.getFirstName() + " " + student.getLastName(),
                        student.getEmail(),
                        student.getAddress(),
                        getEnrolledCoursesString(student),
                        "*****"  // Hides the password for security
                );
            }
        } else {
            System.out.println("❌ No students found.");
        }
    }


    // Helper method to format enrolled courses
    private static String getEnrolledCoursesString(Student student) {
    	List<Course> enrolledCourses = new ArrayList<>(student.getEnrolledCourses());
    	if (enrolledCourses == null || enrolledCourses.isEmpty()) {
            return "No Courses";
        }
        Set<String> courseNames = new LinkedHashSet<>();
        for (Course course : enrolledCourses) {
            courseNames.add(course.getCourseName());
        }
        return String.join(", ", courseNames);
    }
    
    private static void viewStudentById() {
        System.out.print("Enter Student ID: ");
        int studentId = getIntInput();

        Student student = studentService.getStudentById(studentId);

        if (student != null) {
            System.out.println("\n==== Student Information ====");
            System.out.println("Student ID: " + student.getStudentId());
            System.out.println("Name: " + student.getFirstName() + " " + student.getLastName());
            System.out.println("Email: " + student.getEmail());
            System.out.println("Address: " + student.getAddress());
            System.out.println("Phone: " + (student.getPhone() != null && !student.getPhone().isEmpty() ? student.getPhone() : "N/A"));
            System.out.println("Gender: " + (student.getGender() != null && !student.getGender().isEmpty() ? student.getGender() : "N/A"));
            System.out.println("Date of Birth: " + (student.getDob() != null ? student.getDob().toString() : "N/A"));
            System.out.printf("%-15s: *****\n", "Password");
            
            System.out.println("\nEnrolled Courses:");
            List<Course> courses = new ArrayList<>(student.getEnrolledCourses());
            if (courses != null && !courses.isEmpty()) {
                for (Course course : courses) {
                    System.out.println("Course ID: " + course.getCourseId() + ", Course Name: " + course.getCourseName());
                }
            } else {
                System.out.println("No courses enrolled.");
            }

        } else {
            System.out.println("❌ Student with ID " + studentId + " not found.");
        }
    }


    private static void searchStudent() {
        System.out.println("\n🔎 Search Student By:");
        System.out.println("1. Name or Email");
        System.out.println("2. Gender");
        System.out.println("3. Course");
        System.out.print("Enter your choice: ");
        int choice = getIntInput();

        List<Student> allStudents = studentService.getAllStudents();
        List<Student> matched = new ArrayList<>();

        switch (choice) {
            case 1 -> {
                System.out.print("Enter name or email to search: ");
                String keyword = scanner.nextLine().toLowerCase();
                matched = allStudents.stream()
                    .filter(s -> (s.getFirstName() + " " + s.getLastName()).toLowerCase().contains(keyword)
                              || s.getEmail().toLowerCase().contains(keyword))
                    .toList();
            }
            case 2 -> {
                System.out.print("Enter gender to search (e.g., Male/Female): ");
                String gender = scanner.nextLine().toLowerCase();
                matched = allStudents.stream()
                    .filter(s -> s.getGender() != null && s.getGender().toLowerCase().equals(gender))
                    .toList();
            }
            case 3 -> {
                System.out.print("Enter course name keyword (e.g., Java): ");
                String keyword = scanner.nextLine().toLowerCase();
                matched = allStudents.stream()
                    .filter(s -> s.getEnrolledCourses() != null && s.getEnrolledCourses().stream()
                        .anyMatch(c -> c.getCourseName().toLowerCase().contains(keyword)))
                    .toList();
            }
            default -> {
                System.out.println("❌ Invalid choice.");
                return;
            }
        }

        if (matched.isEmpty()) {
            System.out.println("❌ No matching students found.");
        } else {
            System.out.printf("%-10s%-20s%-30s%-20s%-15s%-10s%-15s%-40s\n",
                    "ID", "Name", "Email", "Address", "Phone", "Gender", "DOB", "Courses");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------");

            for (Student s : matched) {
                String dobStr = (s.getDob() != null) ? s.getDob().toString() : "N/A";
                String courses = getEnrolledCoursesString(s);

                System.out.printf("%-10d%-20s%-30s%-20s%-15s%-10s%-15s%-40s\n",
                        s.getStudentId(),
                        s.getFirstName() + " " + s.getLastName(),
                        s.getEmail(),
                        s.getAddress(),
                        s.getPhone() != null ? s.getPhone() : "N/A",
                        s.getGender() != null ? s.getGender() : "N/A",
                        dobStr,
                        courses);
            }
        }
    }
    
    private static void viewStudentsByCourse() {
        List<Course> allCourses = courseService.getAllCourses();
        if (allCourses.isEmpty()) {
            System.out.println("❌ No courses available.");
            return;
        }

        System.out.println("==== Choose Course Search Method ====");
        System.out.println("1. Search by Course ID");
        System.out.println("2. Search by Course Name");
        System.out.print("Enter choice: ");
        int searchChoice = getIntInput();

        // selectedCourse will be set once and used later (effectively final after initialization)
        final Course selectedCourse;

        if (searchChoice == 1) {
            System.out.println("\n==== Courses Available ====");
            for (Course c : allCourses) {
                System.out.println(c.getCourseId() + ". " + c.getCourseName());
            }
            System.out.print("Enter Course ID: ");
            int courseId = getIntInput();
            selectedCourse = courseService.getCourseById(courseId);
        }
        else if (searchChoice == 2) {
            System.out.println("\n==== Courses Available ====");
            for (Course c : allCourses) {
                System.out.println(c.getCourseId() + ". " + c.getCourseName());
            }
            System.out.print("Enter Course Name: ");
            String courseName = scanner.nextLine().trim();
            selectedCourse = courseService.getCourseByName(courseName);
        } else {
            System.out.println("❌ Invalid choice.");
            return;
        }

        if (selectedCourse == null) {
            System.out.println("❌ Course not found.");
            return;
        }

        // Now we can filter students who are enrolled in the selected course
        List<Student> enrolledStudents = studentService.getAllStudents().stream()
            .filter(s -> s.getEnrolledCourses().stream()
                .anyMatch(c -> c.getCourseId() == selectedCourse.getCourseId()))  // selectedCourse used here
            .collect(Collectors.toList());

        if (enrolledStudents.isEmpty()) {
            System.out.println("❌ No students enrolled in this course.");
            return;
        }

        // Pagination logic
        final int pageSize = 3;
        int totalPages = (int) Math.ceil((double) enrolledStudents.size() / pageSize);
        int currentPage = 1;

        while (true) {
            int start = (currentPage - 1) * pageSize;
            int end = Math.min(start + pageSize, enrolledStudents.size());
            List<Student> page = enrolledStudents.subList(start, end);

            System.out.println("\n🎓 Students Enrolled in: " + selectedCourse.getCourseName());
            System.out.println("\nPage " + currentPage + " of " + totalPages + "\n");

            System.out.printf("%-10s%-20s%-30s%-20s%-15s%-10s%-15s%-40s\n",
                    "ID", "Name", "Email", "Address", "Phone", "Gender", "DOB", "Courses");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------");

            for (Student s : page) {
                String dobStr = (s.getDob() != null) ? s.getDob().toString() : "N/A";
                String courses = getEnrolledCoursesString(s);

                System.out.printf("%-10d%-20s%-30s%-20s%-15s%-10s%-15s%-40s\n",
                        s.getStudentId(),
                        s.getFirstName() + " " + s.getLastName(),
                        s.getEmail(),
                        s.getAddress(),
                        s.getPhone() != null ? s.getPhone() : "N/A",
                        s.getGender() != null ? s.getGender() : "N/A",
                        dobStr,
                        courses);
            }

            System.out.println("\n1. Next Page");
            System.out.println("2. Previous Page");
            System.out.println("3. Back to Course Menu");
            System.out.print("Enter choice: ");
            int navChoice = getIntInput();

            if (navChoice == 1) {
                if (currentPage < totalPages) {
                    currentPage++;
                } else {
                    System.out.println("⚠️ You are on the last page.");
                }
            } else if (navChoice == 2) {
                if (currentPage > 1) {
                    currentPage--;
                } else {
                    System.out.println("⚠️ You are on the first page.");
                }
            } else if (navChoice == 3) {
                break;
            } else {
                System.out.println("❌ Invalid choice.");
            }
        }
    }



    private static void updateStudent() {
        System.out.print("Enter Student ID to update: ");
        int studentId = getIntInput();
        
        // Fetch the student object by ID
        Student student = studentService.getStudentById(studentId);

        if (student != null) {
            // Display the current information for the student
            System.out.println("\n==== Current Student Information ====");
            System.out.println("Name: " + student.getFirstName() + " " + student.getLastName());
            System.out.println("Email: " + student.getEmail());
            System.out.println("Address: " + student.getAddress());
            System.out.println("Phone: " + (student.getPhone() != null ? student.getPhone() : "N/A"));
            System.out.println("Gender: " + (student.getGender() != null ? student.getGender() : "N/A"));
            System.out.println("Date of Birth: " + (student.getDob() != null ? student.getDob() : "N/A"));
            System.out.println("\nEnrolled Courses:");
            for (Course course : student.getEnrolledCourses()) {
                System.out.println("Course ID: " + course.getCourseId() + ", Course Name: " + course.getCourseName());
            }

            // Present options for update
            System.out.println("\nWhat would you like to update?");
            System.out.println("1. First Name");
            System.out.println("2. Last Name");
            System.out.println("3. Password");
            System.out.println("4. Address");
            System.out.println("5. Phone Number");
            System.out.println("6. Gender");
            System.out.println("7. Courses");
            System.out.println("8. Go back to Student Menu");

            int choice = getIntInput();
            
            switch (choice) {
                case 1 -> {
                    // Update first name
                    System.out.print("Enter new first name: ");
                    student.setFirstName(scanner.nextLine());
                    System.out.println("✅ First name updated successfully.");
                }
                case 2 -> {
                    // Update last name
                    System.out.print("Enter new last name: ");
                    student.setLastName(scanner.nextLine());
                    System.out.println("✅ Last name updated successfully.");
                }
                case 3 -> {
                    // Update password with confirmation
                    String newPassword;
                    String confirmPassword;

                    do {
                        System.out.print("Enter new password: ");
                        newPassword = scanner.nextLine();
                        System.out.print("Confirm new password: ");
                        confirmPassword = scanner.nextLine();

                        if (!newPassword.equals(confirmPassword)) {
                            System.out.println("❌ Passwords do not match. Please try again.");
                        }
                    } while (!newPassword.equals(confirmPassword));

                    student.setPassword(newPassword);
                    System.out.println("✅ Password updated successfully.");
                }
                case 4 -> {
                    // Update address
                    System.out.print("Enter new address: ");
                    student.setAddress(scanner.nextLine());
                    System.out.println("✅ Address updated successfully.");
                }
                case 5 -> {
                    // Update phone number
                    System.out.print("Enter new phone number: ");
                    student.setPhone(scanner.nextLine());
                    System.out.println("✅ Phone number updated successfully.");
                }
                case 6 -> {
                    // Update gender
                    System.out.print("Enter new gender: ");
                    student.setGender(scanner.nextLine());
                    System.out.println("✅ Gender updated successfully.");
                }
                case 7 -> {
                    // Update courses
                    List<Course> updatedCourses = manageCourses(courseService, student);
                    student.setEnrolledCourses(updatedCourses); // Update enrolled courses
                    System.out.println("✅ Courses updated successfully.");
                }
                case 8 -> {
                    // Go back to the student menu
                    return;
                }
                default -> System.out.println("❌ Invalid choice! Try again.");
            }

            // Update the student in the system/database
            studentService.updateStudent(student);
            System.out.println("✅ Student information updated successfully!");
        } else {
            System.out.println("❌ Student with ID " + studentId + " not found.");
        }
    }

    
    /**
     * This method handles the updating of optional fields such as Phone, Gender, or Date of Birth.
     * It checks if the user provided a value and returns the new value.
     */
    private static String updateOptionalField(String fieldName, String currentValue) {
        System.out.print("Enter New " + fieldName + " (Leave empty to keep current): ");
        String newValue = scanner.nextLine().trim();
        return newValue.isEmpty() ? currentValue : newValue;
    }

    /**
     * This method is used to handle course enrollment and course updates.
     * It allows adding/removing courses based on the student's current enrollment.
     */
    private static List<Course> manageCourses(CourseService courseService, Student student) {
        List<Course> availableCourses = courseService.getAllCourses();
        Set<Course> updatedCoursesSet = new LinkedHashSet<>(student.getEnrolledCourses()); // Retain existing courses initially

        // Display available courses
        System.out.println("\n📚 Available Courses:");
        System.out.printf("%-5s%-30s\n", "ID", "Course Name");
        System.out.println("--------------------------------------------------");

        for (Course course : availableCourses) {
            System.out.printf("%-5d%-30s\n", course.getCourseId(), course.getCourseName());
        }

        // Ask for new course selection
        System.out.println("Enter new Course IDs to enroll (comma-separated): ");
        String[] newCourseIds = scanner.nextLine().split(",");

        // Process course IDs and update enrolled courses
        for (String courseIdStr : newCourseIds) {
            try {
                int courseId = Integer.parseInt(courseIdStr.trim());
                Course course = courseService.getCourseById(courseId);
                if (course != null) {
                    updatedCoursesSet.add(course);  // Add new course to the list
                } else {
                    System.out.println("❌ Course ID " + courseId + " not found.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid course ID format: " + courseIdStr.trim());
            }
        }

        return new ArrayList<>(updatedCoursesSet); // Return updated list of courses
    }


    private static void deleteStudent() {
        System.out.print("Enter Student ID to delete: ");
        int studentId = getIntInput();
        
        // Fetch the student object by ID to display details for confirmation
        Student student = studentService.getStudentById(studentId);

        if (student != null) {
            // Display student details for confirmation
            System.out.println("\n⚠️ Are you sure you want to delete the student \"" + student.getFirstName() + " " + student.getLastName() + "\" (ID: " + studentId + ")? This action is irreversible.");
            System.out.println("1. Yes, delete the student");
            System.out.println("2. No, cancel");
            
            // Get user choice
            int choice = getIntInput();
            
            if (choice == 1) {
                // Delete the student if user confirms
                studentService.deleteStudent(studentId);
                System.out.println("✅ Student \"" + student.getFirstName() + " " + student.getLastName() + "\" has been deleted successfully.");
            } else {
                // Cancel deletion if user does not confirm
                System.out.println("❌ Deletion cancelled.");
            }
        } else {
            // If student with provided ID is not found
            System.out.println("❌ Student not found.");
        }
    }


   
    private static List<Course> selectCourses(CourseService courseService) {
        Set<Course> selectedCourses = new LinkedHashSet<>(); // Prevent duplicates
        List<Course> allCourses = courseService.getAllCourses();

        if (!allCourses.isEmpty()) {
            System.out.println("\n📚 Available Courses:");
            System.out.printf("%-5s%-30s\n", "ID", "Course Name");
            System.out.println("--------------------------------------------------");

            // Display all courses
            for (Course course : allCourses) {
                System.out.printf("%-5d%-30s\n", course.getCourseId(), course.getCourseName());
            }

            // Get user input
            System.out.print("Enter Course IDs to select (comma-separated): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("⚠️ No course IDs entered.");
                return new ArrayList<>(selectedCourses); // Return an empty list
            }

            // Process each course ID entered by the user
            String[] ids = input.split(",");
            for (String idStr : ids) {
                try {
                    int id = Integer.parseInt(idStr.trim());
                    Course course = courseService.getCourseById(id);
                    if (course != null) {
                        if (!selectedCourses.contains(course)) {
                            selectedCourses.add(course);
                        } else {
                            System.out.println("⚠️ Course already selected: " + course.getCourseName());
                        }
                    } else {
                        System.out.println("⚠️ Course ID " + id + " not found.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid input: " + idStr);
                }
            }
        } else {
            System.out.println("⚠️ No available courses found.");
        }

        // Return the selected courses as a List (converting from Set to List)
        return new ArrayList<>(selectedCourses);
    }

    
    public static int getIntInput() {
        while (true) {
            try {
                int input = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                return input;
            } catch (Exception e) {
                scanner.nextLine(); // Clear invalid input
                System.out.print("❌ Invalid input! Enter a valid number: ");
            }
        }
    }
}
