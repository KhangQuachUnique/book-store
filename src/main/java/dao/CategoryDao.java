package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Category;
import util.JPAUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CategoryDao {
    private static final Logger LOGGER = Logger.getLogger(CategoryDao.class.getName());

    public List<Category> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Category> query = em.createQuery("SELECT c FROM Category c ORDER BY c.id DESC", Category.class);
            List<Category> result = query.getResultList();
            LOGGER.info("Fetched " + result.size() + " categories");
            return result.isEmpty() ? new ArrayList<>() : result;
        } finally {
            em.close();
        }
    }

    public Category findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            Category category = em.find(Category.class, id);
            LOGGER.info("Fetched category with ID " + id);
            return category;
        } finally {
            em.close();
        }
    }

    public void create(Category category) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            category.setCreatedAt(Timestamp.valueOf(java.time.LocalDateTime.now()));
            em.persist(category);
            em.getTransaction().commit();
            LOGGER.info("Created category: " + category.getName());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOGGER.severe("Error creating category: " + e.getMessage());
            throw new RuntimeException("Error creating category", e);
        } finally {
            em.close();
        }
    }

    public void update(Category category) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(category);
            em.getTransaction().commit();
            LOGGER.info("Updated category: " + category.getName());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOGGER.severe("Error updating category: " + e.getMessage());
            throw new RuntimeException("Error updating category", e);
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            Category category = em.find(Category.class, id);
            if (category != null) {
                em.remove(category);
                em.getTransaction().commit();
                LOGGER.info("Deleted category with ID: " + id);
            } else {
                LOGGER.warning("Category not found for deletion: ID " + id);
                throw new RuntimeException("Category not found");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOGGER.severe("Error deleting category: " + e.getMessage());
            throw new RuntimeException("Error deleting category", e);
        } finally {
            em.close();
        }
    }

    public boolean isCategoryNameExists(String name) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(c) FROM Category c WHERE LOWER(c.name) = :name", Long.class);
            query.setParameter("name", name.toLowerCase());
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
}