package com.exam.manager;

import com.exam.entities.Course;
import com.exam.entities.Topic;
import com.exam.service.CourseService;
import com.exam.service.TopicService;
import com.exam.service.impl.CourseServiceImpl;
import com.exam.service.impl.TopicServiceImpl;

import java.util.List;
import java.util.Scanner;

public class TopicManager {
    static Scanner scanner = new Scanner(System.in);
    static TopicService topicService = new TopicServiceImpl();
    static CourseService courseService = new CourseServiceImpl();

    public static void manageTopics() {
        while (true) {
            System.out.println("\n==== Topic Management ====");
            System.out.println("1. Add Topic");
            System.out.println("2. View Topic by ID");
            System.out.println("3. View All Topics");
            System.out.println("4. Update Topic");
            System.out.println("5. Delete Topic");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = StudentManager.getIntInput();
            switch (choice) {
                case 1 -> addTopic();
                case 2 -> viewTopic();
                case 3 -> viewAllTopics();
                case 4 -> updateTopic();
                case 5 -> deleteTopic();
                case 6 -> { return; }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void addTopic() {
        System.out.print("Enter Topic Name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("❌ Topic name cannot be empty.");
            return;
        }

        // Display list of available courses to choose from
        System.out.println("Select Course for the Topic:");
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("❌ No courses available. Please add courses first.");
            return;
        }

        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getCourseName());
        }

        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine().trim();

        // Find the selected course by name
        Course selectedCourse = null;
        for (Course course : courses) {
            if (course.getCourseName().equalsIgnoreCase(courseName)) {
                selectedCourse = course;
                break;
            }
        }

        if (selectedCourse == null) {
            System.out.println("❌ Invalid course name. Topic not added.");
            return;
        }

        // Create and add the topic
        Topic topic = new Topic(name, selectedCourse);
        try {
            topicService.createTopic(topic);
            System.out.println("✅ Topic added successfully!");
            System.out.println("Topic: " + topic.getTopicName() + " added to Course: " + selectedCourse.getCourseName());
        } catch (Exception e) {
            System.out.println("❌ Error adding topic: " + e.getMessage());
        }
    }

    private static void viewTopic() {
        System.out.print("Enter Topic ID: ");
        int topicId = StudentManager.getIntInput();

        // Retrieve the topic by ID
        Topic topic = topicService.getTopicById(topicId);

        if (topic == null) {
            // Topic not found
            System.out.println("❌ Sorry, no topic found with ID " + topicId + ".");
        } else {
            // Topic found, display details in a readable format
            System.out.println("\n=== Topic Information ===");
            System.out.println("-------------------------------");
            System.out.println("Topic ID: " + topic.getTopicId());
            System.out.println("Topic Name: " + topic.getTopicName());
            
            // Get the associated course name
            String courseName = (topic.getCourse() != null) ? topic.getCourse().getCourseName() : "No course assigned";
            System.out.println("Course: " + courseName);

            System.out.println("-------------------------------");
            System.out.println("✅ Topic details displayed successfully.");
        }
    }


    private static void viewAllTopics() {
        List<Topic> topics = topicService.getAllTopics();

        if (topics.isEmpty()) {
            System.out.println("❌ No topics found.");
            return;
        }

        // Print header
        System.out.println("\n==== Topic List ====");
        System.out.println("-------------------------------------------------------------");
        System.out.printf("| %-10s | %-25s | %-30s |\n", "Topic ID", "Topic Name", "Course Name");
        System.out.println("-------------------------------------------------------------");

        // Print each topic in a formatted row
        for (Topic topic : topics) {
            String courseName = (topic.getCourse() != null) ? topic.getCourse().getCourseName() : "N/A";
            System.out.printf("| %-10d | %-25s | %-30s |\n", topic.getTopicId(), topic.getTopicName(), courseName);
        }

        System.out.println("-------------------------------------------------------------");
    }

    private static void updateTopic() {
        System.out.println("\n==== Update Topic ====");
        System.out.print("Would you like to update by: \n1. Topic ID\n2. Topic Name\nEnter your choice: ");
        int choice = getIntInput();

        Topic topic = null;

        if (choice == 1) {
            System.out.print("Enter Topic ID to update: ");
            int topicId = getIntInput();
            topic = topicService.getTopicById(topicId);
        } else if (choice == 2) {
            System.out.print("Enter Topic Name to update: ");
            String topicName = scanner.nextLine().trim();
            topic = topicService.getTopicByName(topicName);
        } else {
            System.out.println("❌ Invalid choice.");
            return;
        }

        if (topic == null) {
            System.out.println("❌ Topic not found.");
            return;
        }

        // Topic found
        System.out.println("\n✅ Topic found:");
        System.out.println("📌 Topic ID   : " + topic.getTopicId());
        System.out.println("📘 Topic Name : " + topic.getTopicName());
        System.out.println("🏷️ Course     : " + 
            (topic.getCourse() != null ? topic.getCourse().getCourseName() : "No course assigned"));

        // Ask what to update
        System.out.println("\n🔹 What would you like to update?");
        System.out.println("1. Topic Name");
        System.out.println("2. Course");
        System.out.print("Enter your choice: ");
        int updateChoice = getIntInput();

        switch (updateChoice) {
            case 1:
                System.out.print("Enter New Topic Name: ");
                String newTopicName = scanner.nextLine().trim();
                topic.setTopicName(newTopicName);
                System.out.println("✅ Topic name updated successfully!");
                break;

            case 2:
                System.out.println("Select new Course for the Topic:");
                List<Course> courses = courseService.getAllCourses();

                for (int i = 0; i < courses.size(); i++) {
                    System.out.println((i + 1) + ". " + courses.get(i).getCourseName());
                }

                System.out.print("Enter course number: ");
                int courseChoice = getIntInput();

                if (courseChoice < 1 || courseChoice > courses.size()) {
                    System.out.println("❌ Invalid course selection.");
                    return;
                }

                Course selectedCourse = courses.get(courseChoice - 1);
                topic.setCourse(selectedCourse);
                System.out.println("✅ Topic course updated successfully!");
                break;

            default:
                System.out.println("❌ Invalid choice.");
                return;
        }

        // Save update
        topicService.updateTopic(topic);

        // Display updated topic
        System.out.println("\n🔄 Updated Topic Details");
        System.out.println("-------------------------------");
        System.out.println("📌 Topic ID      : " + topic.getTopicId());
        System.out.println("📘 Topic Name    : " + topic.getTopicName());
        System.out.println("🏷️ Course Name   : " + topic.getCourse().getCourseName());
        System.out.println("-------------------------------");
    }

    private static void deleteTopic() {
        System.out.println("\n==== 🗑️ Delete Topic ====");
        System.out.print("Would you like to delete by: \n1. Topic ID\n2. Topic Name\nEnter your choice: ");
        int choice = getIntInput();

        Topic topic = null;

        switch (choice) {
            case 1:
                System.out.print("Enter Topic ID to delete: ");
                int topicId = getIntInput();
                topic = topicService.getTopicById(topicId);
                break;
            case 2:
                System.out.print("Enter Topic Name to delete: ");
                String topicName = scanner.nextLine().trim();
                topic = topicService.getTopicByName(topicName);
                break;
            default:
                System.out.println("❌ Invalid choice. Please enter 1 or 2.");
                return;
        }

        if (topic == null) {
            System.out.println("❌ Topic not found.");
            return;
        }

        // Display topic details before deletion
        System.out.println("\n🔍 Topic found:");
        System.out.println("----------------------------------------");
        System.out.println("📌 Topic ID    : " + topic.getTopicId());
        System.out.println("📘 Topic Name  : " + topic.getTopicName());
        System.out.println("🏷️ Course Name : " + topic.getCourse().getCourseName());
        System.out.println("----------------------------------------");

        // Confirm deletion
        System.out.print("⚠️  Are you sure you want to delete this topic? (Y/N): ");
        String confirmation = scanner.nextLine().trim().toUpperCase();

        if (confirmation.equals("Y")) {
            topicService.deleteTopic(topic.getTopicId());
            System.out.println("✅ Topic deleted successfully!");
        } else {
            System.out.println("❌ Deletion cancelled.");
        }
    }

    private static int getIntInput() {
        Scanner scanner = new Scanner(System.in);
        int input = -1;
        while (input == -1) {
            try {
                input = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("❌ Invalid input. Please enter a valid number: ");
            }
        }
        return input;
    }

}
