package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.Promotion;
import util.JPAUtil;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class PromotionDAO {

    private final EntityManagerFactory emf;

    public PromotionDAO() {
        this.emf = JPAUtil.getEntityManagerFactory();
    }

    //  ADMIN: list all promotions
    public List<Promotion> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Promotion p ORDER BY p.expireAt DESC", Promotion.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    //  ADMIN: find by id
    public Promotion findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Promotion.class, id);
        } finally {
            em.close();
        }
    }

    //  ADMIN: check code exists (optionally excluding an id)
    public boolean existsByCode(String code, Long excludeId) {
        if (code == null) return false;
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT COUNT(p) FROM Promotion p WHERE LOWER(p.code) = LOWER(:code)" +
                    (excludeId != null ? " AND p.id <> :excludeId" : "");
            var q = em.createQuery(jpql, Long.class)
                    .setParameter("code", code.trim());
            if (excludeId != null) q.setParameter("excludeId", excludeId);
            Long cnt = q.getSingleResult();
            return cnt != null && cnt > 0;
        } finally {
            em.close();
        }
    }

    //  Find promotion by code (for client apply)
    public Promotion getPromotionByCode(String code) {
        EntityManager em = emf.createEntityManager();
        Promotion promotion = null;

        try {
            String jpql = "SELECT p FROM Promotion p WHERE LOWER(p.code) = LOWER(:code) AND p.expireAt >= :now";
            TypedQuery<Promotion> query = em.createQuery(jpql, Promotion.class);
            query.setParameter("code", code.trim());
            query.setParameter("now", OffsetDateTime.now());

            promotion = query.getSingleResult();
        } catch (NoResultException e) {
            // ignore
        } finally {
            em.close();
        }

        return promotion;
    }

    //  Get all valid promotions (for client apply)
    public List<Promotion> getAllValidPromotions() {
        EntityManager em = emf.createEntityManager();
        List<Promotion> promotions = new ArrayList<>();

        try {
            String jpql = "SELECT p FROM Promotion p WHERE p.expireAt >= :now ORDER BY p.expireAt ASC";
            TypedQuery<Promotion> query = em.createQuery(jpql, Promotion.class);
            query.setParameter("now", OffsetDateTime.now());
            promotions = query.getResultList();
        } finally {
            em.close();
        }

        return promotions;
    }

    //  ADMIN: create promotion
    public Long create(String code, double discount, OffsetDateTime expireAt) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Promotion p = new Promotion();
            p.setCode(code.trim());
            p.setDiscount(discount);
            p.setExpireAt(expireAt);
            em.persist(p);
            em.flush();
            em.getTransaction().commit();
            return p.getId();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    //  ADMIN: update promotion
    public void update(Long id, String code, double discount, OffsetDateTime expireAt) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Promotion existing = em.find(Promotion.class, id);
            if (existing == null) {
                em.getTransaction().rollback();
                throw new IllegalArgumentException("Promotion not found: id=" + id);
            }
            existing.setCode(code.trim());
            existing.setDiscount(discount);
            existing.setExpireAt(expireAt);
            em.merge(existing);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    //  ADMIN: delete promotion
    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Promotion p = em.find(Promotion.class, id);
            if (p != null) {
                em.remove(p);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}