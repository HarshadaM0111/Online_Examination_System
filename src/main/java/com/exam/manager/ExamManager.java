package com.exam.manager;

import com.exam.entities.Admin;
import com.exam.entities.Course;
import com.exam.entities.Exam;
import com.exam.service.ExamService;
import com.exam.service.impl.ExamServiceImpl;
import com.exam.service.CourseService;
import com.exam.service.impl.CourseServiceImpl;

import com.exam.util.HibernateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class ExamManager {
    static Scanner scanner = new Scanner(System.in);
    static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    static ExamService examService = new ExamServiceImpl(sessionFactory);
    static CourseService courseService = new CourseServiceImpl();
  
    
    public static void manageExams() {
        while (true) {
            System.out.println("\n==== Exam Management ====");
            System.out.println("1. Add Exam");
            System.out.println("2. View Exam by ID");
            System.out.println("3. View All Exams");
            System.out.println("4. Update Exam");
            System.out.println("5. Delete Exam");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> addExam();
                case 2 -> viewExam();
                case 3 -> viewAllExams();
                case 4 -> updateExam();
                case 5 -> deleteExam();
                case 6 -> { return; }
                default -> System.out.println("❌ Invalid choice! Please try again.");
            }
        }
    }
    
    private static void addExam() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n==== Adding New Exam ====");

        System.out.print("Enter Exam Title: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("❌ Error: Exam title cannot be empty.");
            return;
        }

        // ✅ Fetch course list from DB
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("❌ No courses found in the database.");
            return;
        }

        // Display courses
        System.out.println("\nSelect Course:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getCourseName());
        }
        System.out.print("Enter your choice: ");
        int courseChoice = getIntInput();

        if (courseChoice < 1 || courseChoice > courses.size()) {
            System.out.println("❌ Invalid course selection.");
            return;
        }

        String courseName = courses.get(courseChoice - 1).getCourseName();

        System.out.print("Enter Duration (in minutes): ");
        int duration = getIntInput();
        if (duration <= 0) {
            System.out.println("❌ Error: Duration must be a positive number.");
            return;
        }

        System.out.print("Enter Number of Questions: ");
        int numQuestions = getIntInput();
        if (numQuestions <= 0) {
            System.out.println("❌ Error: Number of questions must be a positive number.");
            return;
        }

        System.out.print("Enter Admin ID: ");
        int adminId = getIntInput();
        Admin admin = examService.getAdminById(adminId);
        if (admin == null) {
            System.out.println("❌ Error: Admin not found.");
            return;
        }

        System.out.println("\nSelect Exam Type:");
        System.out.println("1. Online Exam");
        System.out.println("2. Offline Exam");
        System.out.print("Enter your choice: ");
        int examTypeChoice = getIntInput();
        String examType = (examTypeChoice == 1) ? "Online Exam" :
                          (examTypeChoice == 2) ? "Offline Exam" : null;
        if (examType == null) {
            System.out.println("❌ Error: Invalid exam type.");
            return;
        }

        String scheduledTime = null;
        if ("Online Exam".equals(examType)) {
            scheduledTime = getValidDateInput(scanner);
        }

        System.out.println("\nSelect Proctoring Option:");
        System.out.println("1. No Proctoring");
        System.out.println("2. Auto-Proctoring (via Webcam)");
        System.out.print("Enter your choice: ");
        int proctorChoice = getIntInput();
        String proctoring = (proctorChoice == 2) ? "Auto-Proctoring" : "No Proctoring";

        System.out.println("\nSelect Exam Mode:");
        System.out.println("1. Closed Book");
        System.out.println("2. Open Book");
        System.out.print("Enter your choice: ");
        int modeChoice = getIntInput();
        String mode = (modeChoice == 2) ? "Open Book" : "Closed Book";

        System.out.print("Enter Minimum Passing Marks (0-100): ");
        int passingMarks = getIntInput();

        System.out.print("Enable Auto-Grading? (yes/no): ");
        boolean autoGrading = scanner.nextLine().trim().equalsIgnoreCase("yes");

        System.out.println("\nEnter Exam Instructions (optional, press Enter to skip):");
        String instructions = scanner.nextLine().trim();
        if (instructions.isEmpty()) {
            instructions = "- Total Questions: " + numQuestions +
                           "\n- Time Limit: " + duration + " minutes" +
                           (examType.equals("Online Exam") ? "\n- This is an Online Exam." : "");
        }

        // ✅ Construct and save Exam
        Exam exam = new Exam(title, duration, numQuestions, examType, scheduledTime, proctoring, instructions);
        exam.setAdmin(admin);
        exam.setCourse(courseName);
        exam.setMode(mode);
        exam.setPassingMarks(passingMarks);
        exam.setAutoGrading(autoGrading);

        examService.createExam(exam);

        System.out.println("\n✅ Exam added successfully!");
        System.out.println("\n📘 Exam Summary:");
        System.out.println("----------------------------------------");
        System.out.println("Title           : " + exam.getTitle());
        System.out.println("Course          : " + courseName);
        System.out.println("Duration        : " + duration + " minutes");
        System.out.println("Questions       : " + numQuestions);
        System.out.println("Admin           : " + admin.getName());
        System.out.println("Exam Type       : " + examType);
        System.out.println("Mode            : " + mode);
        if (scheduledTime != null && !scheduledTime.isEmpty()) {
            System.out.println("Scheduled Time  : " + scheduledTime);
        }
        System.out.println("Proctoring      : " + proctoring);
        System.out.println("Passing Marks   : " + passingMarks + "%");
        System.out.println("Auto-Grading    : " + (autoGrading ? "Enabled" : "Disabled"));
        System.out.println("Instructions    :\n" + instructions);
        System.out.println("----------------------------------------\n");
    }

    private static void viewExam() {
        System.out.print("Enter Exam ID: ");
        int examId = getIntInput();

        // Fetch the exam by ID
        Exam exam = examService.getExamById(examId);

        // Check if the exam exists and display the information
        if (exam != null) {
            System.out.println("\n📘 Exam Details:");
            System.out.println("----------------------------------------");

            // Handle null or missing values for each field
            System.out.println("Exam Title      : " + (exam.getTitle() != null ? exam.getTitle() : "N/A"));
            System.out.println("Course          : " + (exam.getCourse() != null ? exam.getCourse() : "N/A"));
            System.out.println("Duration        : " + (exam.getDuration() > 0 ? exam.getDuration() + " minutes" : "N/A"));
            System.out.println("Number of Questions: " + (exam.getNumQuestions() > 0 ? exam.getNumQuestions() : "N/A"));
            System.out.println("Admin           : " + (exam.getAdmin() != null ? exam.getAdmin().getName() : "N/A"));
            
            // Handle null or missing values for examType and mode
            System.out.println("Exam Type       : " + (exam.getExamType() != null ? exam.getExamType() : "N/A"));
            System.out.println("Mode            : " + (exam.getMode() != null ? exam.getMode() : "N/A"));

            // Handle null or missing values for scheduledTime
            System.out.println("Scheduled Time  : " + (exam.getScheduledTime() != null && !exam.getScheduledTime().isEmpty() ? exam.getScheduledTime() : "N/A"));
            
            // Handle null or missing values for proctoring
            System.out.println("Proctoring      : " + (exam.getProctoring() != null ? exam.getProctoring() : "N/A"));
            
            // Handle null or missing values for passingMarks
            System.out.println("Passing Marks   : " + (exam.getPassingMarks() != null ? exam.getPassingMarks() + "%" : "N/A"));
            
            // Handle null or missing values for autoGrading
            String autoGrading = (exam.getAutoGrading() != null && exam.getAutoGrading()) ? "Enabled" : "Disabled";
            System.out.println("Auto-Grading    : " + autoGrading);

            // Handle null or missing values for instructions
            System.out.println("Instructions    :\n" + (exam.getInstructions() != null && !exam.getInstructions().isEmpty() ? exam.getInstructions() : "N/A"));

            System.out.println("----------------------------------------\n");
        } else {
            // Exam not found
            System.out.println("❌ Error: Exam not found.");
        }
    }

    private static void viewAllExams() {
        List<Exam> exams = examService.getAllExams();
        
        if (exams.isEmpty()) {
            System.out.println("No exams found.");
            return;
        }

        // Header for the exams display
        System.out.println("📘 All Exams:");
        System.out.println("----------------------------------------");
        
        for (Exam exam : exams) {
            System.out.println("Exam Title      : " + (exam.getTitle() != null ? exam.getTitle() : "N/A"));
            System.out.println("Course          : " + (exam.getCourse() != null ? exam.getCourse() : "N/A"));
            System.out.println("Duration        : " + (exam.getDuration() != 0 ? exam.getDuration() + " minutes" : "N/A"));
            System.out.println("Number of Questions: " + (exam.getNumQuestions() != 0 ? exam.getNumQuestions() : "N/A"));
            System.out.println("Admin           : " + (exam.getAdmin() != null ? exam.getAdmin().getName() : "N/A"));
            
            // Handle null for examType
            String examType = (exam.getExamType() != null) ? exam.getExamType() : "N/A";
            System.out.println("Exam Type       : " + examType);

            // Handle null for mode
            String mode = (exam.getMode() != null) ? exam.getMode() : "N/A";
            System.out.println("Mode            : " + mode);

            // Handle null for proctoring
            String proctoring = (exam.getProctoring() != null) ? exam.getProctoring() : "N/A";
            System.out.println("Proctoring      : " + proctoring);

            // Handle null for passingMarks and safely print its value
            String passingMarks = (exam.getPassingMarks() != null) ? exam.getPassingMarks() + "%" : "N/A";
            System.out.println("Passing Marks   : " + passingMarks);
            
            // Handle null for autoGrading
            String autoGrading = (exam.getAutoGrading() != null && exam.getAutoGrading()) ? "Enabled" : "Disabled";
            System.out.println("Auto-Grading    : " + autoGrading);

            System.out.println("----------------------------------------");
        }
    }

    private static void updateExam() {
        System.out.print("Enter Exam ID to update: ");
        int examId = getIntInput();
        Exam exam = examService.getExamById(examId);

        if (exam != null) {
            System.out.println("Exam Found. You can update the following fields:\n");
            boolean continueUpdating = true;

            while (continueUpdating) {
                System.out.println("Choose the field to update:");
                System.out.println("1. Update Exam Title");
                System.out.println("2. Update Duration");
                System.out.println("3. Update Number of Questions");
                System.out.println("4. Update Exam Type");
                System.out.println("5. Update Proctoring");
                System.out.println("6. Update Exam Mode");
                System.out.println("7. Exit and Save Changes");
                System.out.print("Enter your choice: ");
                int choice = getIntInput();

                switch (choice) {
                    case 1:
                        System.out.print("Enter New Exam Title (leave empty to keep current): ");
                        String newTitle = scanner.nextLine();
                        if (!newTitle.isEmpty()) {
                            exam.setTitle(newTitle);
                            System.out.println("✅ Exam Title updated.");
                        }
                        break;

                    case 2:
                        System.out.print("Enter New Duration (minutes): ");
                        exam.setDuration(getIntInput());
                        System.out.println("✅ Duration updated.");
                        break;

                    case 3:
                        System.out.print("Enter New Number of Questions: ");
                        exam.setNumQuestions(getIntInput());
                        System.out.println("✅ Number of Questions updated.");
                        break;

                    case 4:
                        System.out.print("Enter New Exam Type (leave empty to keep current): ");
                        String newExamType = scanner.nextLine();
                        if (!newExamType.isEmpty()) {
                            exam.setExamType(newExamType);
                            System.out.println("✅ Exam Type updated.");
                        }
                        break;

                    case 5:
                        System.out.print("Enter New Proctoring Option (leave empty to keep current): ");
                        String newProctoring = scanner.nextLine();
                        if (!newProctoring.isEmpty()) {
                            exam.setProctoring(newProctoring);
                            System.out.println("✅ Proctoring updated.");
                        }
                        break;

                    case 6:
                        System.out.print("Enter New Exam Mode (leave empty to keep current): ");
                        String newMode = scanner.nextLine();
                        if (!newMode.isEmpty()) {
                            exam.setMode(newMode);
                            System.out.println("✅ Exam Mode updated.");
                        }
                        break;

                    case 7:
                        // Exit and save changes
                        examService.updateExam(exam);
                        System.out.println("✅ Exam updated successfully!");
                        continueUpdating = false;
                        break;

                    default:
                        System.out.println("❌ Invalid choice, please try again.");
                }
            }

            // Display updated exam summary
            System.out.println("\n📘 Updated Exam Summary:");
            System.out.println("----------------------------------------");
            System.out.println("Title           : " + (exam.getTitle() != null ? exam.getTitle() : "N/A"));
            System.out.println("Course          : " + (exam.getCourse() != null ? exam.getCourse() : "N/A"));
            System.out.println("Duration        : " + (exam.getDuration() > 0 ? exam.getDuration() + " minutes" : "N/A"));
            System.out.println("Questions       : " + (exam.getNumQuestions() > 0 ? exam.getNumQuestions() : "N/A"));
            System.out.println("Admin           : " + (exam.getAdmin() != null ? exam.getAdmin().getName() : "N/A"));
            System.out.println("Exam Type       : " + (exam.getExamType() != null ? exam.getExamType() : "N/A"));
            System.out.println("Mode            : " + (exam.getMode() != null ? exam.getMode() : "N/A"));
            System.out.println("Proctoring      : " + (exam.getProctoring() != null ? exam.getProctoring() : "N/A"));
            System.out.println("Passing Marks   : " + (exam.getPassingMarks() != null ? exam.getPassingMarks() + "%" : "N/A"));
            
            // Handling Auto-Grading Null Check
            String autoGrading = (exam.getAutoGrading() != null && exam.getAutoGrading()) ? "Enabled" : "Disabled";
            System.out.println("Auto-Grading    : " + autoGrading);

            System.out.println("Instructions    :\n" + (exam.getInstructions() != null && !exam.getInstructions().isEmpty() ? exam.getInstructions() : "N/A"));
            System.out.println("----------------------------------------\n");

        } else {
            System.out.println("❌ Error: Exam not found.");
        }
    }


    private static void deleteExam() {
        System.out.print("Enter Exam ID to delete: ");
        int examId = getIntInput();
        
        try {
            // Fetch the exam to display its details before deletion
            Exam exam = examService.getExamById(examId);
            
            if (exam != null) {
                // Display exam details before deletion
                System.out.println("\nExam Details to be Deleted:");
                System.out.println("----------------------------------------");
                System.out.println("Title           : " + exam.getTitle());
                System.out.println("Course          : " + exam.getCourse());
                System.out.println("Duration        : " + exam.getDuration() + " minutes");
                System.out.println("Questions       : " + exam.getNumQuestions());
                System.out.println("Admin           : " + exam.getAdmin().getName());
                System.out.println("Exam Type       : " + exam.getExamType());
                System.out.println("Mode            : " + exam.getMode());
                System.out.println("Proctoring      : " + exam.getProctoring());
                System.out.println("Scheduled Time  : " + exam.getScheduledTime());
                System.out.println("----------------------------------------");

                // Deleting the exam
                examService.deleteExam(examId);
                System.out.println("✅ Exam deleted successfully!");
            } else {
                System.out.println("❌ Error: Exam with ID " + examId + " not found.");
            }
            
        } catch (Exception e) {
            // Handle any unexpected errors that may occur during deletion
            System.out.println("❌ Error: An unexpected error occurred while deleting the exam. Please try again later.");
        }
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
      
      public Exam getExamByTitle(String title) {
    	    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    	    Exam exam = null;
    	    try {
    	        session.beginTransaction();  // 🔥 Start transaction
    	        
    	        Query<Exam> query = session.createQuery("FROM Exam WHERE title = :title", Exam.class);
    	        query.setParameter("title", title);
    	        exam = query.uniqueResult();
    	        
    	        session.getTransaction().commit(); // 🔥 Commit transaction
    	    } catch (Exception e) {
    	        if (session.getTransaction() != null) {
    	            session.getTransaction().rollback(); // 🔥 Rollback if error
    	        }
    	        e.printStackTrace();
    	    }
    	    return exam;
    	}
      
      private static String getValidDateInput(Scanner scanner) {
    	    String dateStr = "";
    	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	    sdf.setLenient(false); // Disable lenient parsing

    	    while (true) {
    	        System.out.print("Enter Exam Date and Time (yyyy-MM-dd HH:mm): ");
    	        dateStr = scanner.nextLine().trim();

    	        // Try to parse the date
    	        try {
    	            Date parsedDate = sdf.parse(dateStr);
    	            if (parsedDate.before(new Date())) {
    	                System.out.println("❌ Error: The exam date must be in the future. Please try again.");
    	            } else {
    	                return dateStr;
    	            }
    	        } catch (ParseException e) {
    	            System.out.println("❌ Error: Invalid date format. Please enter in the format yyyy-MM-dd HH:mm.");
    	        }
    	    }
    	}

      private static int getIntInputInRange(int min, int max) {
    	    int choice;
    	    while (true) {
    	        choice = getIntInput();
    	        if (choice >= min && choice <= max) {
    	            return choice;
    	        } else {
    	            System.out.printf("❌ Error: Please enter a valid number between %d and %d: ", min, max);
    	        }
    	    }
    	}


    	private static boolean isValidDate(String dateStr) {
    	    // You can use SimpleDateFormat to validate the date
    	    try {
    	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	        dateFormat.setLenient(false);
    	        dateFormat.parse(dateStr);
    	        return true;
    	    } catch (ParseException e) {
    	        return false;
    	    }
    	}
}

