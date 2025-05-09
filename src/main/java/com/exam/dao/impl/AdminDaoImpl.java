package com.exam.dao.impl;

import com.exam.dao.AdminDao;
import com.exam.entities.Admin;
import com.exam.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public class AdminDaoImpl implements AdminDao {

    @Override
    public Admin createAdmin(Admin admin) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.save(admin);
            session.getTransaction().commit();
            return admin;
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Admin getAdminById(int adminId) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Admin.class, adminId);
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Admin> getAllAdmins() {
        try (Session session = HibernateUtil.getSession()) {
            // Fetching all admins except those with "Deleted" status
            String hql = "FROM Admin WHERE status != 'Deleted'";
            return session.createQuery(hql, Admin.class).list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateAdmin(Admin admin) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.update(admin);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAdmin(int adminId) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Admin admin = session.get(Admin.class, adminId);
            if (admin != null) {
                admin.setStatus("Deleted");  // Soft delete
                session.update(admin);
            }
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

   
    @Override
    public List<Admin> searchAdmin(String keyword) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "FROM Admin WHERE status != 'Deleted' AND (email LIKE :keyword OR name LIKE :keyword OR role LIKE :keyword)";
            return session.createQuery(hql, Admin.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void restoreAdmin(int adminId) {
        // Restores a soft-deleted admin by setting their status back to "Active"
        updateAdminStatus(adminId, "Active");
    }

    
    @Override
    public void updateAdminStatus(int adminId, String status) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Admin admin = session.get(Admin.class, adminId);
            if (admin != null) {
                admin.setStatus(status);
                
                if ("Locked".equalsIgnoreCase(status)) {
                    admin.setLockedAt(LocalDateTime.now());
                } else if ("Active".equalsIgnoreCase(status)) {
                    admin.setUnlockedAt(LocalDateTime.now());
                }
                
                session.update(admin);
            }
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Admin getAdminByEmail(String email) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "FROM Admin WHERE email = :email";
            return session.createQuery(hql, Admin.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }


@Override
public void updateLastLogin(int adminId) {
    try (Session session = HibernateUtil.getSession()) {
        session.beginTransaction();
        Admin admin = session.get(Admin.class, adminId);
        if (admin != null) {
            // Set the current time as last login
            admin.setLastLogin(LocalDateTime.now()); // Set current date and time
            session.update(admin);
        }
        session.getTransaction().commit();
    } catch (HibernateException e) {
        e.printStackTrace();
    }
}

    // Retrieve admins who are marked as deleted (for restoration purposes)
    @Override
    public List<Admin> getDeletedAdmins() {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "FROM Admin WHERE status = 'Deleted'";
            return session.createQuery(hql, Admin.class).list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<Admin> searchAdminByRole(String role) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "FROM Admin WHERE status != 'Deleted' AND LOWER(role) = :role";
            return session.createQuery(hql, Admin.class)
                    .setParameter("role", role.toLowerCase())
                    .list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Admin> searchAdminByStatus(String status) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "FROM Admin WHERE LOWER(status) = :status";
            return session.createQuery(hql, Admin.class)
                    .setParameter("status", status.toLowerCase())
                    .list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Admin> searchAdminByKeyword(String keyword) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "FROM Admin WHERE status != 'Deleted' AND " +
                         "(LOWER(email) LIKE :keyword OR LOWER(name) LIKE :keyword OR LOWER(role) LIKE :keyword)";
            return session.createQuery(hql, Admin.class)
                    .setParameter("keyword", "%" + keyword.toLowerCase() + "%")
                    .list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

	
}
