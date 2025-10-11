package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Category;
import util.JPAUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryDao {
    private static final Logger log = Logger.getLogger(CategoryDao.class.getName());

    private EntityManager getEntityManager() {
        return JPAUtil.getEntityManagerFactory().createEntityManager();
    }

    public List<Category> findAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Category> query = em.createQuery("SELECT c FROM Category c ORDER BY c.id DESC", Category.class);
            return query.getResultList();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error finding all categories", e);
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    public Category findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Category.class, id);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error finding category by id: " + id, e);
            return null;
        } finally {
            em.close();
        }
    }

    public String create(Category c) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
            return null; // Success
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.log(Level.SEVERE, "Error creating category", e);
            return e.getMessage();
        } finally {
            em.close();
        }
    }

    public String update(Category c) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Category managedCategory = em.find(Category.class, c.getId());
            if (managedCategory != null) {
                managedCategory.setName(c.getName());
                managedCategory.setParent(c.getParent());
                managedCategory.setLeaf(c.isLeaf());
                em.getTransaction().commit();
                return null; // Success
            } else {
                em.getTransaction().rollback();
                return "Category not found";
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.log(Level.SEVERE, "Error updating category", e);
            return e.getMessage();
        } finally {
            em.close();
        }
    }

    public String delete(Long id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Category category = em.find(Category.class, id);
            if (category != null) {
                em.remove(category);
                em.getTransaction().commit();
                return null; // Success
            } else {
                em.getTransaction().rollback();
                return "Category not found";
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.log(Level.SEVERE, "Error deleting category", e);
            return e.getMessage();
        } finally {
            em.close();
        }
    }

    public boolean isCategoryNameExists(String name) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(c) FROM Category c WHERE LOWER(c.name) = LOWER(:name)", Long.class);
            query.setParameter("name", name);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error checking category name existence", e);
            return false;
        } finally {
            em.close();
        }
    }
}
