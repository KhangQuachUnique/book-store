package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.Promotion;
import util.JPAUtil;

import java.sql.Timestamp;

public class PromotionDAO {

    public Promotion getPromotionByCode(String code) {
        EntityManager em = JPAUtil.getEntityManager();
        Promotion promotion = null;

        try {
            String jpql = "SELECT p FROM Promotion p WHERE p.code = :code AND p.expiryDate >= :now";
            TypedQuery<Promotion> query = em.createQuery(jpql, Promotion.class);
            query.setParameter("code", code);
            query.setParameter("now", new Timestamp(System.currentTimeMillis()));

            promotion = query.getSingleResult();
        } catch (NoResultException e) {
            // Không tìm thấy => promotion = null
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return promotion;
    }
}
