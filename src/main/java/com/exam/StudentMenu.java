package com.exam;

import com.exam.dao.impl.StudentDaoImpl;
import com.exam.dao.CourseDao;
import com.exam.dao.impl.CourseDaoImpl;
import com.exam.dao.impl.ExamDaoImpl;
import com.exam.dao.impl.ResultDaoImpl;
import com.exam.entities.*;
import com.exam.service.CourseService;
import com.exam.service.impl.CourseServiceImpl;
import com.exam.util.HibernateUtil;

import java.util.Timer;
import java.util.TimerTask;

import org.hibernate.Hibernate;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class StudentMenu {

    private static final StudentDaoImpl studentDao = new StudentDaoImpl();
    private static final ExamDaoImpl examDao = new ExamDaoImpl();
    private static final ResultDaoImpl resultDao = new ResultDaoImpl();
    CourseDao courseDao = new CourseDaoImpl(); 

    private static Scanner sc = new Scanner(System.in);
    private static Student loggedInStudent;

    public static void main(String[] args) {
        loginMenu();
    }

    private static void loginMenu() {
        System.out.print("Enter your Email (or type 'exit' to quit): ");
        String email = sc.nextLine();
        if (email.equalsIgnoreCase("exit")) {
            System.out.println("👋 Goodbye!");
            return;
        }

        System.out.print("Enter your Password: ");
        String password = sc.nextLine();

        loggedInStudent = studentDao.getStudentByEmailAndPassword(email, password);
        if (loggedInStudent != null) {
            System.out.println("✅ Login successful! Welcome " + loggedInStudent.getFirstName());
            showMenu();
        } else {
            System.out.println("❌ Invalid credentials. Try again.\n");
            loginMenu(); // retry
        }
    }

    private static void showMenu() {
        int choice;
        do {
            System.out.println("\n📘 STUDENT MENU");
            System.out.println("1. View Enrolled Courses");
            System.out.println("2. View Available Exams");
            System.out.println("3. Take Exam");
            System.out.println("4. View My Results");
            System.out.println("5. Update Profile");
            System.out.println("6. Change Password");
            System.out.println("7. View Course Grades");
            System.out.println("8. View Instructor Contact");
            System.out.println("9. View Exam Schedule");
            System.out.println("10. Logout");

            choice = getValidChoice();  // Use the new method for valid choice input

            switch (choice) {
                case 1 -> viewCourses();
                case 2 -> viewExams();
                case 3 -> takeExam();
                case 4 -> viewResults();
                case 5 -> updateProfile();
                case 6 -> changePassword();
                case 7 -> viewCourseGrades();
                case 8 -> viewInstructorContact();
                case 9 -> viewExamSchedule();
                case 10 -> System.out.println("👋 Logged out.");
                default -> System.out.println("❌ Invalid choice.");
            }

        } while (choice != 10);
    }

    private static int getValidChoice() {
        int choice = -1;
        while (choice < 1 || choice > 11) {
            System.out.print("Enter your choice: ");
            String input = sc.nextLine();

            try {
                choice = Integer.parseInt(input);
                if (choice < 1 || choice > 11) {
                    System.out.println("❌ Please choose a valid option between 1 and 8.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a valid number.");
            }
        }
        return choice;
    }

    private static void viewCourses() {
        System.out.println("\n📚 Your Enrolled Courses:");
        Set<Course> enrolledCourses = loggedInStudent.getEnrolledCourses();

        if (enrolledCourses == null || enrolledCourses.isEmpty()) {
            System.out.println("⚠️ You are not enrolled in any courses yet.");
        } else {
            int index = 1;
            for (Course course : enrolledCourses) {
                System.out.println(index++ + ". 🆔 Course ID: " + course.getCourseId() +
                        " | 📘 Course Name: " + course.getCourseName());
            }
        }

        System.out.print("\nDo you want to enroll in new courses? (Y/N): ");
        String choice = sc.nextLine().trim();

        if (choice.equalsIgnoreCase("Y")) {
            // Create an instance of CourseServiceImpl (assuming you have this class)
            CourseService courseService = new CourseServiceImpl();  // Instantiate the service

            // Get all available courses
            List<Course> allCourses = courseService.getAllCourses();  // Call the method on the instance
            List<Course> availableCourses = new ArrayList<>();

            for (Course c : allCourses) {
                if (!enrolledCourses.contains(c)) {
                    availableCourses.add(c);
                }
            }

            if (availableCourses.isEmpty()) {
                System.out.println("✅ You are already enrolled in all available courses.");
                return;
            }

            System.out.println("\n🎓 Available Courses for Enrollment:");
            for (int i = 0; i < availableCourses.size(); i++) {
                Course c = availableCourses.get(i);
                System.out.println((i + 1) + ". 🆔 " + c.getCourseId() + " | " + c.getCourseName());
            }

            System.out.print("Enter course numbers to enroll (e.g., 1,3): ");
            String input = sc.nextLine().trim();
            String[] selections = input.split(",");

            List<Course> newlyEnrolled = new ArrayList<>();

            for (String sel : selections) {
                try {
                    int index = Integer.parseInt(sel.trim()) - 1;
                    if (index >= 0 && index < availableCourses.size()) {
                        Course selected = availableCourses.get(index);
                        if (!enrolledCourses.contains(selected)) {
                            enrolledCourses.add(selected);
                            newlyEnrolled.add(selected);
                        }
                    } else {
                        System.out.println("⚠️ Invalid selection: " + sel.trim());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Invalid number format: " + sel.trim());
                }
            }

            if (!newlyEnrolled.isEmpty()) {
                studentDao.updateStudent(loggedInStudent); // Save updates
                System.out.println("\n🎉 Successfully enrolled in:");
                for (Course c : newlyEnrolled) {
                    System.out.println(" - " + c.getCourseName());
                }
            } else {
                System.out.println("❌ No valid courses selected for enrollment.");
            }
        }
    }
    
    private static void viewExams() {
        List<Exam> allExams = examDao.getAllExams();
        Set<Course> enrolledCourses = loggedInStudent.getEnrolledCourses();

        System.out.println("\n═════════════════════════════════════");
        System.out.println("📝  ALL AVAILABLE EXAMS");
        System.out.println("═════════════════════════════════════");

        if (allExams == null || allExams.isEmpty()) {
            System.out.println("⚠️  No exams available at the moment.");
        } else {
            System.out.printf("%-5s %-35s %-25s%n", "ID", "Exam Title", "Course");
            System.out.println("-------------------------------------------------------------");
            for (Exam exam : allExams) {
                String courseName = exam.getCourse(); // Assume getCourse() returns a String
                System.out.printf("%-5d %-35s %-25s%n", exam.getExamId(), exam.getTitle(), courseName);
            }
        }

        System.out.println("\n═════════════════════════════════════");
        System.out.println("🎯  EXAMS FOR YOUR ENROLLED COURSES");
        System.out.println("═════════════════════════════════════");

        if (enrolledCourses == null || enrolledCourses.isEmpty()) {
            System.out.println("⚠️  You are not enrolled in any courses.");
        } else {
            boolean found = false;
            System.out.printf("%-5s %-35s %-25s%n", "ID", "Exam Title", "Course");
            System.out.println("-------------------------------------------------------------");
            for (Exam exam : allExams) {
                String examCourseName = exam.getCourse(); // This is a String
                for (Course enrolled : enrolledCourses) {
                    if (enrolled.getCourseName().equalsIgnoreCase(examCourseName)) {
                        System.out.printf("%-5d %-35s %-25s%n",
                            exam.getExamId(), exam.getTitle(), examCourseName);
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                System.out.println("❌  No exams available for your enrolled courses.");
            }
        }

        System.out.println("\nPress Enter to return to the main menu...");
        sc.nextLine();
    }

//    private static void takeExam() {
//        // 1. Show Student Info
//        System.out.println("\n📋 ==== Student Information ====");
//        System.out.println("Student ID     : " + loggedInStudent.getStudentId());
//        System.out.println("Name           : " + loggedInStudent.getName());
//        System.out.println("Email          : " + loggedInStudent.getEmail());
//        System.out.println("Address        : " + loggedInStudent.getAddress());
//        System.out.println("Phone          : " + loggedInStudent.getPhone());
//        System.out.println("Gender         : " + loggedInStudent.getGender());
//        System.out.println("Date of Birth  : " + loggedInStudent.getDob());
//        System.out.println("Password       : *****");
//
//        System.out.println("\nEnrolled Courses:");
//        loggedInStudent.getEnrolledCourses().forEach(course -> 
//            System.out.println(" - Course ID: " + course.getCourseId() + ", Course Name: " + course.getCourseName())
//        );
//
//        // 2. Show Available Exams
//        List<Exam> allExams = examDao.getAllExams();
//        Set<Course> enrolledCourses = loggedInStudent.getEnrolledCourses();
//        List<Exam> eligibleExams = new ArrayList<>();
//
//        System.out.println("\n📝 Available Exams:");
//        int count = 1;
//        for (Exam exam : allExams) {
//            for (Course course : enrolledCourses) {
//                if (exam.getCourse() != null && exam.getCourse().equals(course.getCourseName())) {
//                    eligibleExams.add(exam);
//                    System.out.println(count++ + ". " + exam.getTitle() + " (" + course.getCourseName() + ")");
//                    break;
//                }
//            }
//        }
//
//        if (eligibleExams.isEmpty()) {
//            System.out.println("❌ No exams available for your enrolled courses.");
//            return;
//        }
//
//        // 3. Choose Exam by Number
//        int examChoice = -1;
//        while (examChoice < 1 || examChoice > eligibleExams.size()) {
//            System.out.print("Enter the number of the exam to take: ");
//            try {
//                examChoice = Integer.parseInt(sc.nextLine());
//            } catch (NumberFormatException e) {
//                System.out.println("⚠ Invalid number.");
//            }
//        }
//
//        Exam exam = examDao.getExamByIdWithQuestions(eligibleExams.get(examChoice - 1).getExamId());
//        if (exam == null) {
//            System.out.println("❌ Exam not found.");
//            return;
//        }
//
//        // 4. Check if already taken
//        List<Result> previousResults = resultDao.getResultsByStudentId(loggedInStudent.getStudentId());
//        boolean alreadyTaken = previousResults.stream()
//            .anyMatch(r -> r.getExam().getExamId() == exam.getExamId());
//
//        if (alreadyTaken) {
//            System.out.println("⚠ You have already taken this exam.");
//            System.out.println("Would you like to:\n1. View your previous result\n2. Take another exam\n3. Return to menu");
//            String choice = sc.nextLine();
//            switch (choice) {
//                case "1":
//                    viewMyResults();
//                    break;
//                case "2":
//                    takeExam();
//                    break;
//                default:
//                    return;
//            }
//            return;
//        }
//
//        // 5. Display Exam Summary
//        System.out.println("\n📘 ==== Exam Summary ====");
//        System.out.println("Title           : " + exam.getTitle());
//        System.out.println("Course          : " + exam.getCourse());
//        System.out.println("Duration        : " + exam.getDuration() + " minutes");
//        System.out.println("Questions       : " + exam.getQuestions().size());
//        System.out.println("Admin           : " + exam.getAdmin().getName());
//        System.out.println("Exam Type       : " + exam.getExamType());
//        System.out.println("Mode            : " + exam.getMode());
//        System.out.println("Scheduled Time  : " + exam.getScheduledTime());
//        System.out.println("Proctoring      : " + exam.getProctoring());
//        System.out.println("Passing Marks   : " + exam.getPassingMarks() + "%");
//        System.out.println("Auto-Grading    : " + (Boolean.TRUE.equals(exam.getAutoGrading()) ? "Enabled" : "Disabled"));
//        System.out.println("Instructions    :\n- Total Questions: " + exam.getQuestions().size() +
//                           "\n- Time Limit: " + exam.getDuration() + " minutes\n- This is an Online Exam.");
//
//        System.out.print("\nDo you want to start the exam now? (Y/N): ");
//        if (!sc.nextLine().equalsIgnoreCase("Y")) return;
//
//        // 6. Setup Timer
//        final long examDurationMs = exam.getDuration() * 60 * 1000;
//        long startTime = System.currentTimeMillis();
//        long endTime = startTime + examDurationMs;
//        
//        if (exam.getPassingMarks() == null) {
//            System.out.println("⚠ Error: This exam has no passing marks set. Please contact the admin.");
//            return;
//        }
//        
//        if (exam.getQuestions().isEmpty()) {
//            System.out.println("⚠ Error: This exam has no questions. Please contact the admin.");
//            return;
//        }
//
//        System.out.println("\n⏳ Exam Started! You have " + exam.getDuration() + " minutes.");
//
//        List<Question> questions = exam.getQuestions();
//        List<String> studentAnswers = new ArrayList<>();
//        double score = 0.0;
//
//        // 7. Ask Questions
//        for (int i = 0; i < questions.size(); i++) {
//            long timeLeft = endTime - System.currentTimeMillis();
//            if (timeLeft <= 0) {
//                System.out.println("\n⏰ Time's up! Auto-submitting your exam...");
//                break;
//            }
//
//            Question q = questions.get(i);
//            System.out.println("\nQ" + (i + 1) + ": " + q.getQuestionText());
//            System.out.println("1. " + q.getOptionA());
//            System.out.println("2. " + q.getOptionB());
//            System.out.println("3. " + q.getOptionC());
//            System.out.println("4. " + q.getOptionD());
//
//            int answerIndex = -1;
//            while (true) {
//                System.out.print("Enter your answer (1-4): ");
//                try {
//                    answerIndex = Integer.parseInt(sc.nextLine()) - 1;
//                    if (answerIndex >= 0 && answerIndex < 4) break;
//                    else System.out.println("⚠ Invalid choice. Choose 1 to 4.");
//                } catch (NumberFormatException e) {
//                    System.out.println("⚠ Enter a valid number.");
//                }
//            }
//
//            String selectedAnswer = List.of(
//                q.getOptionA(), q.getOptionB(), q.getOptionC(), q.getOptionD()
//            ).get(answerIndex);
//            studentAnswers.add(selectedAnswer);
//
//            if (selectedAnswer.equalsIgnoreCase(q.getCorrectAnswerText())) {
//                System.out.println("✅ Correct!");
//                score += 1;
//            } else {
//                System.out.println("❌ Incorrect.");
//            }
//
//            double progress = (double)(i + 1) / questions.size() * 100;
//            System.out.printf("📊 Progress: %.2f%% completed.\n", progress);
//            System.out.printf("🕒 Time left: %.1f minutes\n", timeLeft / 60000.0);
//        }
//
//        // 8. Optional Review
//        System.out.print("\nDo you want to review your answers before submission? (Y/N): ");
//        if (sc.nextLine().equalsIgnoreCase("Y")) {
//            for (int i = 0; i < questions.size(); i++) {
//                Question q = questions.get(i);
//                System.out.println("\nQ" + (i + 1) + ": " + q.getQuestionText());
//                System.out.println("1. " + q.getOptionA());
//                System.out.println("2. " + q.getOptionB());
//                System.out.println("3. " + q.getOptionC());
//                System.out.println("4. " + q.getOptionD());
//                System.out.println("Your previous answer: " + studentAnswers.get(i));
//                System.out.print("Change answer? (Y/N): ");
//                if (sc.nextLine().equalsIgnoreCase("Y")) {
//                    int newAns = -1;
//                    while (true) {
//                        System.out.print("Enter new answer (1-4): ");
//                        try {
//                            newAns = Integer.parseInt(sc.nextLine()) - 1;
//                            if (newAns >= 0 && newAns < 4) break;
//                        } catch (Exception e) {
//                            System.out.println("⚠ Invalid.");
//                        }
//                    }
//                    String newAnswer = List.of(
//                        q.getOptionA(), q.getOptionB(), q.getOptionC(), q.getOptionD()
//                    ).get(newAns);
//                    studentAnswers.set(i, newAnswer);
//                }
//            }
//        }
//
//        // 9. Final Review
//        System.out.println("\n🎯 Final Review Before Submission");
//        for (int i = 0; i < questions.size(); i++) {
//            System.out.println("Q" + (i + 1) + ": " + questions.get(i).getQuestionText());
//            System.out.println("Your Answer: " + studentAnswers.get(i));
//        }
//
//        System.out.print("\nWould you like to submit your exam now? (Y/N): ");
//        if (!sc.nextLine().equalsIgnoreCase("Y")) {
//            System.out.println("You may review and submit later.");
//            return;
//        }
//
//        // Show correct answers
//        System.out.println("\n📋 Answer Summary:");
//        for (int i = 0; i < questions.size(); i++) {
//            Question q = questions.get(i);
//            System.out.println("Q" + (i + 1) + ": " + q.getQuestionText());
//            System.out.println("Your Answer   : " + studentAnswers.get(i));
//            System.out.println("Correct Answer: " + q.getCorrectAnswerText());
//            System.out.println(studentAnswers.get(i).equalsIgnoreCase(q.getCorrectAnswerText()) ? "✅ Correct" : "❌ Incorrect");
//        }
//
//        // 10. Submit Exam (this will trigger grading and result saving)
//        submitExam(questions, score, questions.size(), exam);
//    }
//

    private static void takeExam() {
        // 1. Show Student Info
        System.out.println("\n📋 ==== Student Information ====");
        System.out.println("Student ID     : " + loggedInStudent.getStudentId());
        System.out.println("Name           : " + loggedInStudent.getName());
        System.out.println("Email          : " + loggedInStudent.getEmail());
        System.out.println("Address        : " + loggedInStudent.getAddress());
        System.out.println("Phone          : " + loggedInStudent.getPhone());
        System.out.println("Gender         : " + loggedInStudent.getGender());
        System.out.println("Date of Birth  : " + loggedInStudent.getDob());
        System.out.println("Password       : *****");

        System.out.println("\nEnrolled Courses:");
        loggedInStudent.getEnrolledCourses().forEach(course -> 
            System.out.println(" - Course ID: " + course.getCourseId() + ", Course Name: " + course.getCourseName())
        );

        // 2. Show Available Exams
        List<Exam> allExams = examDao.getAllExams();
        Set<Course> enrolledCourses = loggedInStudent.getEnrolledCourses();
        List<Exam> eligibleExams = new ArrayList<>();

        System.out.println("\n📝 Available Exams:");
        int count = 1;
        for (Exam exam : allExams) {
            for (Course course : enrolledCourses) {
                if (exam.getCourse() != null && exam.getCourse().equals(course.getCourseName())) {
                    eligibleExams.add(exam);
                    System.out.println(count++ + ". " + exam.getTitle() + " (" + course.getCourseName() + ")");
                    break;
                }
            }
        }

        if (eligibleExams.isEmpty()) {
            System.out.println("❌ No exams available for your enrolled courses.");
            return;
        }

        // 3. Choose Exam by Number
        int examChoice = -1;
        while (examChoice < 1 || examChoice > eligibleExams.size()) {
            System.out.print("Enter the number of the exam to take: ");
            try {
                examChoice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("⚠ Invalid number.");
            }
        }

        Exam exam = examDao.getExamByIdWithQuestions(eligibleExams.get(examChoice - 1).getExamId());
        if (exam == null) {
            System.out.println("❌ Exam not found.");
            return;
        }

        // 4. Check if already taken
        List<Result> previousResults = resultDao.getResultsByStudentId(loggedInStudent.getStudentId());
        boolean alreadyTaken = previousResults.stream()
            .anyMatch(r -> r.getExam().getExamId() == exam.getExamId());

        if (alreadyTaken) {
            System.out.println("⚠ You have already taken this exam.");
            System.out.println("Would you like to:\n1. View your previous result\n2. Retake this exam\n3. Return to menu");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    viewMyResults();
                    break;
                case "2":
                    // Reset answers and score for retake
                    retakeExam(exam);
                    return;
                default:
                    return;
            }
            return;
        }

        // 5. Display Exam Summary
        System.out.println("\n📘 ==== Exam Summary ====");
        System.out.println("Title           : " + exam.getTitle());
        System.out.println("Course          : " + exam.getCourse());
        System.out.println("Duration        : " + exam.getDuration() + " minutes");
        System.out.println("Questions       : " + exam.getQuestions().size());
        System.out.println("Admin           : " + exam.getAdmin().getName());
        System.out.println("Exam Type       : " + exam.getExamType());
        System.out.println("Mode            : " + exam.getMode());
        System.out.println("Scheduled Time  : " + exam.getScheduledTime());
        System.out.println("Proctoring      : " + exam.getProctoring());
        System.out.println("Passing Marks   : " + exam.getPassingMarks() + "%");
        System.out.println("Auto-Grading    : " + (Boolean.TRUE.equals(exam.getAutoGrading()) ? "Enabled" : "Disabled"));
        System.out.println("Instructions    :\n- Total Questions: " + exam.getQuestions().size() +
                           "\n- Time Limit: " + exam.getDuration() + " minutes\n- This is an Online Exam.");

        System.out.print("\nDo you want to start the exam now? (Y/N): ");
        if (!sc.nextLine().equalsIgnoreCase("Y")) return;

        // 6. Setup Timer
        final long examDurationMs = exam.getDuration() * 60 * 1000;
        long startTime = System.currentTimeMillis();
        long endTime = startTime + examDurationMs;
        
        if (exam.getPassingMarks() == null) {
            System.out.println("⚠ Error: This exam has no passing marks set. Please contact the admin.");
            return;
        }
        
        if (exam.getQuestions().isEmpty()) {
            System.out.println("⚠ Error: This exam has no questions. Please contact the admin.");
            return;
        }

        System.out.println("\n⏳ Exam Started! You have " + exam.getDuration() + " minutes.");

        List<Question> questions = exam.getQuestions();
        List<String> studentAnswers = new ArrayList<>();
        double score = 0.0;

        // 7. Ask Questions
        for (int i = 0; i < questions.size(); i++) {
            long timeLeft = endTime - System.currentTimeMillis();
            if (timeLeft <= 0) {
                System.out.println("\n⏰ Time's up! Auto-submitting your exam...");
                break;
            }

            Question q = questions.get(i);
            System.out.println("\nQ" + (i + 1) + ": " + q.getQuestionText());
            System.out.println("1. " + q.getOptionA());
            System.out.println("2. " + q.getOptionB());
            System.out.println("3. " + q.getOptionC());
            System.out.println("4. " + q.getOptionD());

            int answerIndex = -1;
            while (true) {
                System.out.print("Enter your answer (1-4): ");
                try {
                    answerIndex = Integer.parseInt(sc.nextLine()) - 1;
                    if (answerIndex >= 0 && answerIndex < 4) break;
                    else System.out.println("⚠ Invalid choice. Choose 1 to 4.");
                } catch (NumberFormatException e) {
                    System.out.println("⚠ Enter a valid number.");
                }
            }

            String selectedAnswer = List.of(
                q.getOptionA(), q.getOptionB(), q.getOptionC(), q.getOptionD()
            ).get(answerIndex);
            studentAnswers.add(selectedAnswer);

            if (selectedAnswer.equalsIgnoreCase(q.getCorrectAnswerText())) {
                System.out.println("✅ Correct!");
                score += 1;
            } else {
                System.out.println("❌ Incorrect.");
            }

            double progress = (double)(i + 1) / questions.size() * 100;
            System.out.printf("📊 Progress: %.2f%% completed.\n", progress);
            System.out.printf("🕒 Time left: %.1f minutes\n", timeLeft / 60000.0);
        }

        // 8. Final Review and Submit Logic as already written...

        // 10. Submit Exam (this will trigger grading and result saving)
        submitExam(questions, score, questions.size(), exam);
    }

    private static void retakeExam(Exam exam) {
        // 1. Reset the answers and score for retake
        List<Question> questions = exam.getQuestions();
        List<String> studentAnswers = new ArrayList<>();
        double score = 0.0;

        // 2. Start retaking the exam
        System.out.println("\n🔁 Retaking the exam...");

        // 3. Ask the questions again
        long startTime = System.currentTimeMillis();
        long endTime = startTime + exam.getDuration() * 60 * 1000;

        for (int i = 0; i < questions.size(); i++) {
            long timeLeft = endTime - System.currentTimeMillis();
            if (timeLeft <= 0) {
                System.out.println("\n⏰ Time's up! Auto-submitting your exam...");
                break;
            }

            Question q = questions.get(i);
            System.out.println("\nQ" + (i + 1) + ": " + q.getQuestionText());
            System.out.println("1. " + q.getOptionA());
            System.out.println("2. " + q.getOptionB());
            System.out.println("3. " + q.getOptionC());
            System.out.println("4. " + q.getOptionD());

            int answerIndex = -1;
            while (true) {
                System.out.print("Enter your answer (1-4): ");
                try {
                    answerIndex = Integer.parseInt(sc.nextLine()) - 1;
                    if (answerIndex >= 0 && answerIndex < 4) break;
                    else System.out.println("⚠ Invalid choice. Choose 1 to 4.");
                } catch (NumberFormatException e) {
                    System.out.println("⚠ Enter a valid number.");
                }
            }

            String selectedAnswer = List.of(
                q.getOptionA(), q.getOptionB(), q.getOptionC(), q.getOptionD()
            ).get(answerIndex);
            studentAnswers.add(selectedAnswer);

            if (selectedAnswer.equalsIgnoreCase(q.getCorrectAnswerText())) {
                System.out.println("✅ Correct!");
                score += 1;
            } else {
                System.out.println("❌ Incorrect.");
            }

            double progress = (double)(i + 1) / questions.size() * 100;
            System.out.printf("📊 Progress: %.2f%% completed.\n", progress);
            System.out.printf("🕒 Time left: %.1f minutes\n", timeLeft / 60000.0);
        }

        // 4. Final Review Before Submission
        System.out.println("\n🎯 Final Review Before Submission");
        for (int i = 0; i < questions.size(); i++) {
            System.out.println("Q" + (i + 1) + ": " + questions.get(i).getQuestionText());
            System.out.println("Your Answer: " + studentAnswers.get(i));
        }

        System.out.print("\nWould you like to submit your exam now? (Y/N): ");
        if (!sc.nextLine().equalsIgnoreCase("Y")) {
            System.out.println("You may review and submit later.");
            return;
        }

        // 5. Show correct answers and final score
        System.out.println("\n📋 Answer Summary:");
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            System.out.println("Q" + (i + 1) + ": " + q.getQuestionText());
            System.out.println("Your Answer   : " + studentAnswers.get(i));
            System.out.println("Correct Answer: " + q.getCorrectAnswerText());
            System.out.println(studentAnswers.get(i).equalsIgnoreCase(q.getCorrectAnswerText()) ? "✅ Correct" : "❌ Incorrect");
        }

        // 6. Submit Exam (this will trigger grading and result saving)
        submitExam(questions, score, questions.size(), exam);
    }

    
 // Auto-grade method (add this method in the StudentMenu class)
    private static double autoGradeExam(List<Question> questions, List<String> studentAnswers) {
        double score = 0.0;

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            String correctAnswer = question.getCorrectAnswerText();
            String studentAnswer = studentAnswers.get(i);

            if (studentAnswer != null && studentAnswer.equalsIgnoreCase(correctAnswer)) {
                score++;  // Increment score for each correct answer
            }
        }

        return score;
    }

    private static void submitExam(List<Question> questions, double score, int totalQuestions, Exam exam) {
        if (totalQuestions == 0) {
            System.out.println("⚠ Error: This exam has no questions. Please contact the admin.");
            return;
        }

        int totalMarks = totalQuestions;

        if (score > totalMarks) {
            score = totalMarks;
        }

        double percentage = (score / totalMarks) * 100;

        Integer passingMarks = exam.getPassingMarks();
        if (passingMarks == null) {
            System.out.println("⚠ Error: This exam has no passing marks set. Please contact the admin.");
            return;
        }

        boolean passed = percentage >= passingMarks;

        Result result = new Result();
        result.setStudent(loggedInStudent);
        result.setExam(exam);
        result.setScore(score);
        result.setAttemptDate(LocalDateTime.now());
        result.setPassed(passed);
        result.setPercentage(percentage);
        result.setTotalMarks(totalMarks);

        resultDao.saveResult(result);

        System.out.println("\n📋 Exam Submitted Successfully!");
        System.out.printf("🏆 Your Score: %.1f/%d\n", score, totalMarks);
        System.out.printf("📊 Percentage: %.2f%%\n", percentage);
        System.out.println(passed ? "🎉 Status: PASSED" : "❌ Status: FAILED");
    }



    private static void viewMyResults() {
        List<Result> results = resultDao.getResultsByStudentId(loggedInStudent.getStudentId());

        if (results == null || results.isEmpty()) {
            System.out.println("📭 You haven't taken any exams yet.");
            return;
        }

        System.out.println("\n📋 ==== Your Exam Results ====");
        for (Result r : results) {
            Integer totalMarks = r.getTotalMarks();
            if (totalMarks == null || totalMarks == 0) {
                totalMarks = 1;  // Default to 1 if totalMarks is null or zero
            }

            double percentage = r.getPercentage();  // Accurate percentage

            System.out.println("-----------------------------");
            System.out.println("Exam Title     : " + r.getExam().getTitle());
            System.out.println("Course         : " + r.getExam().getCourse());
            System.out.printf("Score          : %.1f/%d\n", r.getScore(), totalMarks);
            System.out.printf("Percentage     : %.2f%%\n", percentage);
            System.out.println("Status         : " + (r.isPassed() ? "Passed" : "Failed"));
            System.out.println("Date Attempted : " + r.getAttemptDate());
        }
        System.out.println("-----------------------------");
    }
    
    private static void viewResults() {
        List<Result> results = resultDao.getResultsByStudentId(loggedInStudent.getStudentId());

        if (results == null || results.isEmpty()) {
            System.out.println("\n⚠ You have not taken any exams yet.");
            return;
        }

        System.out.println("\n📋 ==== Your Exam Results ====");
        for (Result r : results) {
            Exam exam = r.getExam();
            String courseName = exam.getCourse();
            System.out.println("-----------------------------");
            System.out.println("Exam Title     : " + exam.getTitle());
            System.out.println("Course         : " + courseName);
            System.out.println("Score          : " + r.getScore() + "/" + r.getTotalMarks());
            System.out.printf("Percentage     : %.2f%%\n", r.getPercentage());
            System.out.println("Status         : " + (r.isPassed() ? "✅ Passed" : "❌ Failed"));
            System.out.println("Date Attempted : " + r.getAttemptDate());
        }
        System.out.println("-----------------------------");
    }

    private static void updateProfile() {
        System.out.println("\n🔧 Update Profile (press Enter to skip any field):");

        System.out.print("First Name (" + loggedInStudent.getFirstName() + "): ");
        String firstName = sc.nextLine();
        if (!firstName.isBlank()) loggedInStudent.setFirstName(firstName);

        System.out.print("Last Name (" + loggedInStudent.getLastName() + "): ");
        String lastName = sc.nextLine();
        if (!lastName.isBlank()) loggedInStudent.setLastName(lastName);

        System.out.print("Address (" + loggedInStudent.getAddress() + "): ");
        String address = sc.nextLine();
        if (!address.isBlank()) loggedInStudent.setAddress(address);

        System.out.print("Phone (" + loggedInStudent.getPhone() + "): ");
        String phone = sc.nextLine();
        if (!phone.isBlank()) loggedInStudent.setPhone(phone);

        System.out.print("Gender (" + loggedInStudent.getGender() + "): ");
        String gender = sc.nextLine();
        if (!gender.isBlank()) loggedInStudent.setGender(gender);

        System.out.print("Date of Birth (" + loggedInStudent.getDob() + ") [yyyy-MM-dd]: ");
        String dobInput = sc.nextLine();
        if (!dobInput.isBlank()) {
            try {
                LocalDate dob = LocalDate.parse(dobInput);
                loggedInStudent.setDob(dob);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Invalid date format. DOB not updated.");
            }
        }

        // Persist changes
        studentDao.updateStudent(loggedInStudent);

        System.out.println("✅ Profile updated successfully!");

        // Return to menu after profile update
        showMenu();
    }

    private static void changePassword() {
        System.out.print("🔑 Enter current password: ");
        String current = sc.nextLine();

        if (!loggedInStudent.getPassword().equals(current)) {
            System.out.println("❌ Incorrect current password.");
            return;
        }

        System.out.print("🔐 Enter new password: ");
        String newPass = sc.nextLine();

        System.out.print("🔁 Confirm new password: ");
        String confirm = sc.nextLine();

        if (!newPass.equals(confirm)) {
            System.out.println("❌ Passwords do not match.");
            return;
        }

        loggedInStudent.setPassword(newPass);
        studentDao.updateStudent(loggedInStudent);
        System.out.println("✅ Password changed successfully!");

        // Return to menu after password change
        showMenu();
    }

    private static void viewCourseGrades() {
        System.out.println("\n📊 ==== Course Grades ====");

        CourseDao courseDao = new CourseDaoImpl();
        List<Course> enrolledCourses = courseDao.getCoursesByStudentId(loggedInStudent.getStudentId());

        if (enrolledCourses == null || enrolledCourses.isEmpty()) {
            System.out.println("❌ You are not enrolled in any courses.");
            return;
        }

        for (Course course : enrolledCourses) {
            System.out.println("\n📘 Course: " + course.getCourseName());

            List<Exam> exams = examDao.getExamsByCourseId(course.getCourseName());  // Make sure this method accepts courseName
            if (exams == null || exams.isEmpty()) {
                System.out.println("  ⚠ No exams found for this course.");
                continue;
            }

            boolean hasGrades = false;
            double totalScore = 0;
            int gradedCount = 0;

            for (Exam exam : exams) {
                Result result = resultDao.getResultByStudentAndExam(loggedInStudent.getStudentId(), exam.getExamId());

                if (result != null) {
                    hasGrades = true;
                    double score = result.getScore();
                    totalScore += score;
                    gradedCount++;
                    System.out.printf("  - %s: %.2f%%\n", exam.getTitle(), score);
                }
            }

            if (!hasGrades) {
                System.out.println("  ⚠ No grades available yet for this course.");
            } else {
                double avgScore = totalScore / gradedCount;
                System.out.printf("  📌 Average Grade: %.2f%%\n", avgScore);
            }
        }
    }

 
    private static void viewInstructorContact() {
        System.out.println("👨‍🏫 ==== Instructor Contact ====");
        
        // Sample data for courses
       List<Course> courses = new ArrayList<>(loggedInStudent.getEnrolledCourses());
        for (Course course : courses) {
            System.out.println("Course: " + course.getCourseName());
            System.out.println(" - Instructor: " + course.getInstructor().getName());
            System.out.println(" - Email: " + course.getInstructor().getEmail());
          }
    }

    private static void viewExamSchedule() {
        System.out.println("📅 ==== Exam Schedule ====");

        // Ensure the session is open and fetch exams explicitly if necessary
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Student student = session.get(Student.class, loggedInStudent.getStudentId());
            
            // If exams are not already loaded, initialize them
            Hibernate.initialize(student.getExams());  // Force initialization of the exams collection

            List<Exam> exams = student.getExams();  // Now the exams should be available
            
            if (exams == null || exams.isEmpty()) {
                System.out.println("No upcoming exams found.");
                return;
            }

            // Display the upcoming exams
            for (int i = 0; i < exams.size(); i++) {
                Exam exam = exams.get(i);
                System.out.println((i + 1) + ". " + exam.getCourse() + " " + exam.getTitle() + " - " + exam.getScheduledTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}