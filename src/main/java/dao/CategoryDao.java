package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import model.Category;
import util.JPAUtil;

import java.sql.Timestamp;
import java.util.List;

public class CategoryDao {

    // Queries use entity field names, not column names

    public List<Category> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Category> q = em.createQuery("SELECT c FROM Category c ORDER BY c.id", Category.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Category findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    public List<Category> findByParentId(Long parentId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            if (parentId == null) {
                return em.createQuery("SELECT c FROM Category c WHERE c.parent IS NULL ORDER BY c.id DESC", Category.class)
                        .getResultList();
            }
            return em.createQuery("SELECT c FROM Category c WHERE c.parent.id = :pid ORDER BY c.id DESC", Category.class)
                    .setParameter("pid", parentId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public boolean existsByName(String name, Long excludeId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT COUNT(c) FROM Category c WHERE LOWER(c.name) = LOWER(:name)"
                    + (excludeId != null ? " AND c.id <> :excludeId" : "");
            TypedQuery<Long> q = em.createQuery(jpql, Long.class)
                    .setParameter("name", name);
            if (excludeId != null) q.setParameter("excludeId", excludeId);
            Long count = q.getSingleResult();
            return count != null && count > 0;
        } finally {
            em.close();
        }
    }

    public Category saveCategory(Category c) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            if (c.getId() == 0) {
                em.persist(c);
            } else {
                c = em.merge(c);
            }

            tx.commit();
            return c;
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Lỗi khi lưu Category: " + ex.getMessage(), ex);
        } finally {
            em.close();
        }
    }

    public Category createWithParent(String name, Long parentId, Boolean isLeaf) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Category c = new Category();
            c.setName(name);
            c.setLeaf(isLeaf != null ? isLeaf : false);
            c.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            if (parentId != null) {
                Category parent = em.find(Category.class, parentId);
                if (parent == null) {
                    throw new RuntimeException("Parent category không tồn tại");
                }
                c.setParent(parent);
            }

            em.persist(c);
            tx.commit();
            return c;
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Lỗi khi tạo Category: " + ex.getMessage(), ex);
        } finally {
            em.close();
        }
    }

    public Category updateCategory(Long id, String name, Long parentId, Boolean isLeaf) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Category existing = em.find(Category.class, id);
            if (existing == null) {
                throw new RuntimeException("Category không tồn tại");
            }

            if (name != null) {
                existing.setName(name);
            }

            if (isLeaf != null) {
                existing.setLeaf(isLeaf);
            }

            if (parentId != null) {
                if (id.equals(parentId)) {
                    throw new RuntimeException("Không thể đặt parent bằng chính nó");
                }
                Category parent = em.find(Category.class, parentId);
                if (parent == null) {
                    throw new RuntimeException("Parent category không tồn tại");
                }
                existing.setParent(parent);
            } else {
                existing.setParent(null);
            }

            em.merge(existing);
            tx.commit();
            return existing;
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Lỗi khi cập nhật Category: " + ex.getMessage(), ex);
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Category existing = em.find(Category.class, id);
            if (existing != null) {
                // Khởi tạo các collection để tránh lỗi LazyInitializationException
                if (existing.getChildren() != null) {
                    existing.getChildren().size(); // Kích hoạt lazy loading
                }
                if (existing.getBooks() != null) {
                    existing.getBooks().size(); // Kích hoạt lazy loading
                }

                // Kiểm tra xem có danh mục con hoặc sách không trước khi xóa
                if (!existing.getChildren().isEmpty()) {
                    throw new RuntimeException("Không thể xóa danh mục có danh mục con");
                }

                if (!existing.getBooks().isEmpty()) {
                    throw new RuntimeException("Không thể xóa danh mục có sản phẩm");
                }

                em.remove(existing);
            }
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Lỗi khi xóa Category: " + ex.getMessage(), ex);
        } finally {
            em.close();
        }
    }
}
