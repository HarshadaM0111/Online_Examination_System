package com.exam.manager;

import com.exam.entities.Course;
import com.exam.entities.Instructor;
import com.exam.entities.Topic;
import com.exam.service.CourseService;
import com.exam.service.InstructorService;
import com.exam.service.impl.CourseServiceImpl;
import com.exam.service.impl.InstructorServiceImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CourseManager {
    static Scanner scanner = new Scanner(System.in);
    static CourseService courseService = new CourseServiceImpl();
    static InstructorService instructorService = new InstructorServiceImpl();

    public static void manageCourses() {
        while (true) {
            System.out.println("\n==== Course Management ====");
            System.out.println("1. Add Course");
            System.out.println("2. View Course by ID");
            System.out.println("3. View All Courses");
            System.out.println("4. Update Course");
            System.out.println("5. Delete Course");
            System.out.println("6. Search Courses by Name");
            System.out.println("7. View Courses by Instructor");
            System.out.println("8. Assign Topic to Course");
            System.out.println("9. Remove Topic from Course");
            System.out.println("10. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> addCourse();
                case 2 -> viewCourse();
                case 3 -> viewAllCourses();
                case 4 -> updateCourse();
                case 5 -> deleteCourse();
                case 6 -> searchCoursesByName();
                case 7 -> viewCoursesByInstructor();
                case 8 -> assignTopicToCourse();
                case 9 -> removeTopicFromCourse();
                case 10 -> { return; }
                default -> System.out.println("❌ Invalid choice! Please try again.");
            }
        }
    }

    private static void addCourse() {
        System.out.println("\n📘 Add New Course");
        System.out.println("------------------------------------------");

        System.out.print("Enter Course Name         : ");
        String courseName = scanner.nextLine().trim();
        if (courseName.isEmpty()) {
            System.out.println("⚠️ Course name cannot be empty.");
            return;
        }

        System.out.print("Enter Course Description  : ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) {
            System.out.println("⚠️ Course description cannot be empty.");
            return;
        }

        System.out.print("Enter Instructor ID       : ");
        int instructorId = getIntInput();

        Instructor instructor = instructorService.getInstructorById(instructorId);
        if (instructor == null) {
            System.out.println("❌ Instructor not found. Please enter a valid Instructor ID.");
            return;
        }

        Course course = new Course(0, courseName, description, instructor, new ArrayList<>());
        courseService.createCourse(course);

        // Assuming course ID is set during creation; retrieve latest for display if needed
        System.out.println("\n✅ Course added successfully!");
        System.out.println("\n📄 New Course Details:");
        System.out.println("--------------------------------------------------------------");
        System.out.printf("Course ID       : %d%n", course.getCourseId());
        System.out.printf("Course Name     : %s%n", course.getCourseName());
        System.out.printf("Description     : %s%n", course.getDescription());
        System.out.printf("Instructor      : %s (Instructor ID: %d, Department: %s)%n",
                instructor.getName(),
                instructor.getInstructorId(),
                instructor.getDepartment() != null ? instructor.getDepartment().getDeptName() : "N/A");
        System.out.println("Topics          : None");
        System.out.println("--------------------------------------------------------------");
    }


    private static void viewCourse() {
        System.out.print("Enter Course ID: ");
        int courseId = getIntInput();  // Get user input for the Course ID
        Course course = courseService.getCourseById(courseId);  // Fetch course by ID
        
        if (course == null) {
            System.out.println("❌ Course not found.");
        } else {
            // Display the course details if found
            System.out.println("\n📚 Course Details:");
            System.out.println("---------------------------------------------------------------");
            System.out.printf("Course ID        : %-5d\n", course.getCourseId());
            System.out.printf("Course Name      : %-30s\n", course.getCourseName());
            System.out.printf("Description      : %-30s\n", course.getDescription());
            
            // Get instructor details (assuming course.getInstructor() returns an instructor object)
            String instructorName = course.getInstructor() != null ? course.getInstructor().getName() : "Not assigned";
            String instructorEmail = course.getInstructor() != null ? course.getInstructor().getEmail() : "Not assigned";
            System.out.printf("Instructor       : %-25s | %-25s\n", instructorName, instructorEmail);
            
            // Topics: Join them into a single string, or display "None" if no topics
            String topics = course.getTopics().stream()
                    .map(Topic::getTopicName)  // Assuming getTopicName() returns the topic's name
                    .collect(Collectors.joining(", ", "", ""));
            
            if (topics.isEmpty()) {
                topics = "None";
            }
            System.out.printf("Topics           : %-40s\n", topics);
            
            System.out.println("---------------------------------------------------------------");
        }
    }


    private static void viewAllCourses() {
        // Get all courses
        List<Course> courses = courseService.getAllCourses();
        
        // Check if courses are found
        if (courses.isEmpty()) {
            System.out.println("⚠️ No courses found.");
        } else {
            // Print the header
            System.out.println("📋 Course List:");
            System.out.println("---------------------------------------------------------------");
            System.out.printf("| %-5s | %-30s | %-25s | %-40s |\n", "ID", "Course Name", "Instructor", "Topics");
            System.out.println("---------------------------------------------------------------");

            // Iterate through courses and print details
            for (Course course : courses) {
                // Safely retrieve course details (handle potential nulls)
                String courseName = course.getCourseName() != null ? course.getCourseName() : "Unknown";
                String instructorName = course.getInstructor() != null ? course.getInstructor().getName() : "None";
                
                // Convert list of topics to a comma-separated string
                String topics = course.getTopics().stream()
                        .map(Topic::getTopicName)  // Get the topic name
                        .collect(Collectors.joining(", ", "", ""));  // Join them with a comma

                // If no topics, display "None"
                if (topics.isEmpty()) {
                    topics = "None";
                }

                // Print course details in a formatted way
                System.out.printf("| %-5d | %-30s | %-25s | %-40s |\n",
                        course.getCourseId(),
                        courseName,
                        instructorName,
                        topics);
            }

            // Print the footer
            System.out.println("---------------------------------------------------------------");
        }
    }
    
    private static void updateCourse() {
        System.out.print("Enter Course ID to update: ");
        int courseId = getIntInput();
        Course course = courseService.getCourseById(courseId);

        if (course == null) {
            System.out.println("❌ Course not found.");
            return;
        }

        System.out.println("\n✏️ Leave fields empty to keep current values.");

        // Update course name
        System.out.printf("Current Name        : %s%n", course.getCourseName());
        System.out.print("Enter New Course Name: ");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty()) {
            course.setCourseName(newName);
        }

        // Update description
        System.out.printf("Current Description : %s%n", course.getDescription());
        System.out.print("Enter New Description: ");
        String newDesc = scanner.nextLine().trim();
        if (!newDesc.isEmpty()) {
            course.setDescription(newDesc);
        }

        // Update instructor
        Instructor currentInstructor = course.getInstructor();
        System.out.printf("Current Instructor  : %s (ID: %d)%n",
                currentInstructor.getName(), currentInstructor.getInstructorId());
        System.out.print("Enter New Instructor ID (or leave blank): ");
        String instructorInput = scanner.nextLine().trim();
        if (!instructorInput.isEmpty()) {
            try {
                int newInstructorId = Integer.parseInt(instructorInput);
                Instructor newInstructor = instructorService.getInstructorById(newInstructorId);
                if (newInstructor != null) {
                    course.setInstructor(newInstructor);
                } else {
                    System.out.println("⚠️ Instructor not found. Keeping current instructor.");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid ID entered. Keeping current instructor.");
            }
        }

        courseService.updateCourse(course);

        System.out.println("\n✅ Course updated successfully!");
        System.out.println("\n📄 Updated Course Details:");
        System.out.println("--------------------------------------------------------------");
        System.out.printf("Course ID       : %d%n", course.getCourseId());
        System.out.printf("Course Name     : %s%n", course.getCourseName());
        System.out.printf("Description     : %s%n", course.getDescription());
        Instructor instructor = course.getInstructor();
        System.out.printf("Instructor      : %s (ID: %d, Dept: %s)%n",
                instructor.getName(),
                instructor.getInstructorId(),
                instructor.getDepartment() != null ? instructor.getDepartment().getDeptName() : "N/A");

        List<Topic> topics = course.getTopics();
        if (topics == null || topics.isEmpty()) {
            System.out.println("Topics          : None");
        } else {
            System.out.println("Topics          :");
            topics.forEach(t -> System.out.printf("  - %s%n", t.getTopicName()));
        }
        System.out.println("--------------------------------------------------------------");
    }

    private static void deleteCourse() {
        System.out.print("Enter Course ID to delete: ");
        int courseId = getIntInput();
        Course course = courseService.getCourseById(courseId);

        if (course == null) {
            System.out.println("❌ Course not found.");
            return;
        }

        System.out.println("\n⚠️ You are about to delete the following course:");
        System.out.println("--------------------------------------------------------------");
        System.out.printf("Course ID       : %d%n", course.getCourseId());
        System.out.printf("Course Name     : %s%n", course.getCourseName());
        System.out.printf("Description     : %s%n", course.getDescription());
        Instructor instructor = course.getInstructor();
        System.out.printf("Instructor      : %s (ID: %d, Dept: %s)%n",
                instructor.getName(),
                instructor.getInstructorId(),
                instructor.getDepartment() != null ? instructor.getDepartment().getDeptName() : "N/A");

        List<Topic> topics = course.getTopics();
        if (topics == null || topics.isEmpty()) {
            System.out.println("Topics          : None");
        } else {
            System.out.println("Topics          :");
            topics.forEach(t -> System.out.printf("  - %s%n", t.getTopicName()));
        }
        System.out.println("--------------------------------------------------------------");

        System.out.print("Are you sure you want to delete this course? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("yes")) {
            courseService.deleteCourse(courseId);
            System.out.println("✅ Course deleted successfully!");
        } else {
            System.out.println("❌ Deletion cancelled.");
        }
    }

    private static void searchCoursesByName() {
        System.out.print("Enter course name keyword: ");
        String keyword = scanner.nextLine().trim();

        List<Course> matchedCourses = courseService.searchCoursesByName(keyword);

        if (matchedCourses.isEmpty()) {
            System.out.println("❌ No courses found with that name.");
        } else {
            System.out.println("🔍 Matching Courses:");
            matchedCourses.forEach(CourseManager::printCourseSummary);
        }
    }

    private static void viewCoursesByInstructor() {
        System.out.print("Enter Instructor ID: ");
        int instructorId = getIntInput();

        List<Course> courses = courseService.getCoursesByInstructorId(instructorId);

        if (courses.isEmpty()) {
            System.out.println("❌ No courses found for this instructor.");
        } else {
            System.out.printf("📘 Courses taught by Instructor ID %d:%n", instructorId);
            courses.forEach(CourseManager::printCourseSummary);
        }
    }

    private static void assignTopicToCourse() {
        System.out.print("Enter Course ID: ");
        int courseId = getIntInput();
        Course course = courseService.getCourseById(courseId);

        if (course == null) {
            System.out.println("❌ Course not found.");
            return;
        }

        System.out.print("Enter Topic Name to Add: ");
        String topicName = scanner.nextLine().trim();

        // Ensure the topic name is not empty
        while (topicName.isEmpty()) {
            System.out.println("⚠️ Topic name cannot be empty. Please enter a valid topic name.");
            topicName = scanner.nextLine().trim();
        }

        Topic topic = new Topic();  // Assuming a simple POJO
        topic.setTopicName(topicName);

        if (course.getTopics() == null) {
            course.setTopics(new ArrayList<>());
        }
        course.getTopics().add(topic);

        courseService.updateCourse(course);
        System.out.println("✅ Topic added to course.");
    }

    private static void removeTopicFromCourse() {
        System.out.print("Enter Course ID: ");
        int courseId = getIntInput();
        Course course = courseService.getCourseById(courseId);

        if (course == null) {
            System.out.println("❌ Course not found.");
            return;
        }

        List<Topic> topics = course.getTopics();
        if (topics == null || topics.isEmpty()) {
            System.out.println("⚠️ No topics assigned to this course.");
            return;
        }

        // Display current topics
        System.out.println("\nCurrent Topics:");
        for (int i = 0; i < topics.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, topics.get(i).getTopicName());
        }

        System.out.print("Enter topic number to remove: ");
        int topicIndex = getIntInput() - 1;

        if (topicIndex < 0 || topicIndex >= topics.size()) {
            System.out.println("❌ Invalid topic number.");
            return;
        }

        String topicToRemove = topics.get(topicIndex).getTopicName();

        System.out.print("Do you want to remove all occurrences of the topic? (yes/no): ");
        String removeAll = scanner.nextLine().trim().toLowerCase();

        boolean removed = false;

        if (removeAll.equals("yes")) {
            // Remove all
            removed = topics.removeIf(topic -> topic.getTopicName().equalsIgnoreCase(topicToRemove));
        } else {
            // Remove only first occurrence
            for (Iterator<Topic> it = topics.iterator(); it.hasNext(); ) {
                if (it.next().getTopicName().equalsIgnoreCase(topicToRemove)) {
                    it.remove();
                    removed = true;
                    break;
                }
            }
        }

        if (removed) {
            courseService.updateCourse(course);
            System.out.printf("✅ Topic '%s' removed from course.%n", topicToRemove);
        } else {
            System.out.println("❌ Failed to remove the topic.");
        }
    }



    private static void printCourseSummary(Course course) {
        System.out.println("--------------------------------------------------------------");
        System.out.printf("Course ID       : %d%n", course.getCourseId());
        System.out.printf("Course Name     : %s%n", course.getCourseName());
        System.out.printf("Description     : %s%n", course.getDescription());
        Instructor instructor = course.getInstructor();
        System.out.printf("Instructor      : %s (ID: %d, Dept: %s)%n",
                instructor.getName(),
                instructor.getInstructorId(),
                instructor.getDepartment() != null ? instructor.getDepartment().getDeptName() : "N/A");
        List<Topic> topics = course.getTopics();
        if (topics == null || topics.isEmpty()) {
            System.out.println("Topics          : None");
        } else {
            System.out.println("Topics          : " + topics.stream()
                    .map(Topic::getTopicName)
                    .collect(Collectors.joining(", ")));
        }
        System.out.println("--------------------------------------------------------------");
    }



    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("❌ Invalid input! Please enter a valid number: ");
            }
        }
    }
}