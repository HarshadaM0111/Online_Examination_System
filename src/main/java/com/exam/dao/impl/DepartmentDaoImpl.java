package com.exam.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import com.exam.dao.DepartmentDao;
import com.exam.entities.Department;
import com.exam.entities.Instructor;
import com.exam.util.HibernateUtil;

import java.util.List;

public class DepartmentDaoImpl implements DepartmentDao {

    @Override
    public Department createDepartment(Department department) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.save(department);
            session.getTransaction().commit();
            return department;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Department getDepartmentById(int departmentId) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Department.class, departmentId);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Department> getAllDepartments() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM Department", Department.class).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateDepartment(Department department) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.update(department);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDepartment(int departmentId) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Department department = session.get(Department.class, departmentId);
            if (department != null) {
                session.delete(department);
            }
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean departmentExistsByName(String deptName) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "SELECT COUNT(d) FROM Department d WHERE LOWER(d.deptName) = LOWER(:deptName)";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("deptName", deptName.trim());
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public List<Department> searchDepartmentsByName(String keyword) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "FROM Department WHERE deptName LIKE :keyword";
            Query<Department> query = session.createQuery(hql, Department.class);
            query.setParameter("keyword", "%" + keyword + "%");
            return query.getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }
    
 // Add the actual implementation to DepartmentDaoImpl
    @Override
    public Department getDepartmentByName(String deptName) {
        try (Session session = HibernateUtil.getSession()) {
            String hql = "FROM Department WHERE deptName = :deptName";
            Query<Department> query = session.createQuery(hql, Department.class);
            query.setParameter("deptName", deptName);
            return query.uniqueResult();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }
    
//    @Override
//    public Department getDepartmentByName(String deptName) {
//        return departmentDao.getDepartmentByName(deptName); // Assuming you have this method in DepartmentDao
//    }
    
    @Override
    public boolean assignInstructorToDepartment(int deptId, int instructorId) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Department department = session.get(Department.class, deptId);
            Instructor instructor = session.get(Instructor.class, instructorId);

            if (department != null && instructor != null) {
                department.getInstructors().add(instructor);
                session.update(department);
                session.getTransaction().commit();
                return true;
            }
            session.getTransaction().rollback();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeInstructorFromDepartment(int deptId, int instructorId) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Department department = session.get(Department.class, deptId);
            Instructor instructor = session.get(Instructor.class, instructorId);

            if (department != null && instructor != null && department.getInstructors().remove(instructor)) {
                session.update(department);
                session.getTransaction().commit();
                return true;
            }
            session.getTransaction().rollback();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return false;
    }


}
