package com.exam.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Load the Hibernate configuration file
            return new Configuration().configure("com/exam/hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("❌ SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession() {
        // Open a new session
        return sessionFactory.openSession();
    }

    public static void shutdown() {
        // Close the SessionFactory
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
