package com.exam.service.impl;

import com.exam.dao.ExamDao;
import com.exam.dao.QuestionDao;
import com.exam.dao.StudentAnswerDao;
import com.exam.dao.AdminDao;
import com.exam.entities.Admin;
import com.exam.entities.Exam;
import com.exam.entities.Question;
import com.exam.entities.Student;
import com.exam.entities.StudentAnswer;
import com.exam.service.ExamService;
import com.exam.util.HibernateUtil;
import com.exam.dao.impl.AdminDaoImpl;
import com.exam.dao.impl.ExamDaoImpl;
import com.exam.dao.impl.QuestionDaoImpl;
import com.exam.dao.impl.StudentAnswerDaoImpl;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class ExamServiceImpl implements ExamService {
    private ExamDao examDao = new ExamDaoImpl();
    private AdminDao adminDao = new AdminDaoImpl();
    private SessionFactory sessionFactory;  // Hibernate Session Factory
    
    private QuestionDao questionDao;
    private StudentAnswerDao studentAnswerDao;
    
    public ExamServiceImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
        this.questionDao = new QuestionDaoImpl(sessionFactory);
        this.studentAnswerDao = new StudentAnswerDaoImpl(sessionFactory);
    }

    // Constructor with session factory injection
    public ExamServiceImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Exam createExam(Exam exam) {
        return examDao.createExam(exam);
    }

    @Override
    public Exam getExamById(int examId) {
        return examDao.getExamById(examId);
    }

    @Override
    public List<Exam> getAllExams() {
        return examDao.getAllExams();
    }

    @Override
    public void updateExam(Exam exam) {
        examDao.updateExam(exam);
    }

    @Override
    public void deleteExam(int examId) {
        examDao.deleteExam(examId);
    }

    @Override
    public Admin getAdminById(int adminId) {
        return adminDao.getAdminById(adminId);
    }

    
    public Exam getExamByTitle(String title) {
        Session session = sessionFactory.openSession();
        var tx = session.beginTransaction();
        Exam exam = null;
        try {
            // Use LOWER() to ignore case
            String hql = "FROM Exam WHERE LOWER(title) = LOWER(:title)";
            Query<Exam> query = session.createQuery(hql, Exam.class);
            query.setParameter("title", title.toLowerCase()); // Convert input title to lowercase
            exam = query.uniqueResult();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return exam;
    }
    

    @Override
    public void takeExam(Student student, int examId) {
        Scanner sc = new Scanner(System.in);

        System.out.println("\nStarting Exam...");

        // Step 1: Get the Exam Object (Assume you already have exam entity by examId)
        Exam exam = new Exam();
        exam.setExamId(examId); // only ID is enough here

        // Step 2: Fetch All Questions of this Exam
        List<Question> questions = questionDao.getQuestionsByExam(exam);
        
        if (questions.isEmpty()) {
            System.out.println("No questions available for this exam.");
            return;
        }

        int correct = 0;
        int total = questions.size();

        // Step 3: For each Question
        for (Question question : questions) {
            System.out.println("\nQuestion: " + question.getQuestionText());
            System.out.println("A. " + question.getOptionA());
            System.out.println("B. " + question.getOptionB());
            System.out.println("C. " + question.getOptionC());
            System.out.println("D. " + question.getOptionD());
            System.out.print("Your answer (A/B/C/D): ");
            String answerText = sc.nextLine().trim().toUpperCase(); // Take student input

            // Step 4: Save student's answer
            StudentAnswer studentAnswer = new StudentAnswer(answerText, question, student);
            studentAnswerDao.saveStudentAnswer(studentAnswer);

            // Step 5: Check Correctness
            if (answerText.equalsIgnoreCase(question.getCorrectAnswerText())) {
                correct++;
            }
        }

        // Step 6: Show Result
        System.out.println("\nExam Completed!");
        System.out.println("Total Questions: " + total);
        System.out.println("Correct Answers: " + correct);
        double percentage = ((double) correct / total) * 100;
        System.out.printf("Percentage: %.2f%%\n", percentage);
    }

    public List<Exam> getExamsByProctoring(String type) {
        return ((ExamDaoImpl) examDao).getExamsByProctoring(type);
    }

    @Override
    public List<Exam> getExamsByAdminId(int adminId) {
        // Assuming you have a method in ExamDaoImpl to get exams by admin ID.
        return examDao.getExamsByAdminId(adminId);
    }

    @Override
    public List<Exam> getExamsByExamType(String examType) {
        // Assuming you have a method in ExamDaoImpl to get exams by exam type.
        return examDao.getExamsByExamType(examType);
    }

    @Override
    public List<Exam> getExamsByCourse(int courseId) {
        return examDao.getExamsByCourse(courseId);
    }
   
    public List<Exam> getExamsByCourseName(String courseName) {
        return examDao.getExamsByCourseName(courseName);
    }
}
