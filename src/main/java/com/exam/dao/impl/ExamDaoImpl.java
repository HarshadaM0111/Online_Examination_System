package com.exam.dao.impl;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.exam.dao.ExamDao;
import com.exam.entities.Exam;
import com.exam.entities.Question;
import com.exam.util.HibernateUtil;

import java.util.Collections;
import java.util.List;

public class ExamDaoImpl implements ExamDao {
	
	 private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();  // Using the HibernateUtil class
	
    @Override
    public Exam createExam(Exam exam) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.save(exam);
            session.getTransaction().commit();
            return exam;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Exam getExamById(int examId) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Exam.class, examId);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    public Exam getExamByIdWithQuestions(int examId) {
        Exam exam = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // First, fetch the Exam with questions (without options)
            exam = session.createQuery(
                    "SELECT DISTINCT e FROM Exam e " +
                    "LEFT JOIN FETCH e.questions q " +
                    "WHERE e.examId = :examId", Exam.class)
                .setParameter("examId", examId)
                .uniqueResult();

            // Now fetch the options for each question (lazy load the options)
            if (exam != null && exam.getQuestions() != null) {
                for (Question question : exam.getQuestions()) {
                    // Initialize the options lazily (Hibernate will fetch them when needed)
                    Hibernate.initialize(question.getOptions());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exam;
    }



    @Override
    public List<Exam> getAllExams() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM Exam", Exam.class).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateExam(Exam exam) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.update(exam);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteExam(int examId) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Exam exam = session.get(Exam.class, examId);
            if (exam != null) {
                session.delete(exam);
            }
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
   
    @Override
    public Exam getExamByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            System.out.println("❌ Exam title cannot be null or empty.");
            return null;
        }

        Session session = sessionFactory.openSession();
        var tx = session.beginTransaction();
        Exam exam = null;
        try {
            // Use LOWER() to ignore case and match exam title properly
            String hql = "FROM Exam WHERE LOWER(title) = LOWER(:title)";
            Query<Exam> query = session.createQuery(hql, Exam.class);
            query.setParameter("title", title.trim().toLowerCase());  // Ensure input title is trimmed and converted to lowercase
            exam = query.uniqueResult();
            
            if (exam == null) {
                System.out.println("❌ Exam with the title '" + title + "' not found.");
            } else {
                System.out.println("✅ Exam found: " + exam.getTitle());
            }
            
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
    public List<Exam> getExamsByProctoring(String proctoringType) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "FROM Exam WHERE LOWER(proctoring) = LOWER(:type)";
            Query<Exam> query = session.createQuery(hql, Exam.class);
            query.setParameter("type", proctoringType.toLowerCase());
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Exam> getExamsByExamType(String examType) {
        Session session = sessionFactory.openSession();
        var tx = session.beginTransaction();
        List<Exam> exams = null;
        try {
            String hql = "FROM Exam WHERE examType = :examType";
            Query<Exam> query = session.createQuery(hql, Exam.class);
            query.setParameter("examType", examType);
            exams = query.getResultList();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return exams;
    }

    public List<Exam> getExamsByAdminId(int adminId) {
        Session session = sessionFactory.openSession();
        var tx = session.beginTransaction();
        List<Exam> exams = null;
        try {
            String hql = "FROM Exam WHERE admin.id = :adminId";  // Assuming Exam has an Admin reference
            Query<Exam> query = session.createQuery(hql, Exam.class);
            query.setParameter("adminId", adminId);
            exams = query.getResultList();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return exams;
    }

    @Override
    public List<Exam> getExamsByCourse(int courseId) {
        List<Exam> exams = null;
        try (Session session = HibernateUtil.getSession()) {
            // Assuming 'course' is a String field, and courseId is a string-based course code like "CS101"
            String hql = "FROM Exam e WHERE e.course = :courseId";
            Query<Exam> query = session.createQuery(hql, Exam.class);
            query.setParameter("courseId", String.valueOf(courseId)); // Convert the int courseId to String
            exams = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exams;
    }

    @Override
    public List<Exam> getExamsByCourseName(String courseName) {
        List<Exam> exams = null;
        try (Session session = HibernateUtil.getSession()) {
            String hql = "FROM Exam e WHERE e.course = :courseName";
            Query<Exam> query = session.createQuery(hql, Exam.class);
            query.setParameter("courseName", courseName);
            exams = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exams;
    }
    
    public List<Exam> getExamsByCourseId(String courseCode) {
        try (Session session = sessionFactory.openSession()) {
            Query<Exam> query = session.createQuery(
                "FROM Exam e WHERE e.course = :courseCode", Exam.class);
            query.setParameter("courseCode", courseCode);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }



}
