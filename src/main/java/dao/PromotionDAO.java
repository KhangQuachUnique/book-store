package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.Promotion;
import util.JPAUtil;

import java.time.OffsetDateTime;

public class PromotionDAO {

    public Promotion getPromotionByCode(String code) {
        EntityManager em = JPAUtil.getEntityManager();
        Promotion promotion = null;

        try {
            System.out.println("[DEBUG] Finding promotion with code=" + code);

            // ✅ Truy vấn chính xác + kiểm tra hạn sử dụng
            String jpql = "SELECT p FROM Promotion p WHERE LOWER(p.code) = LOWER(:code) AND p.expireAt >= :now";
            TypedQuery<Promotion> query = em.createQuery(jpql, Promotion.class);
            query.setParameter("code", code.trim());
            query.setParameter("now", OffsetDateTime.now());

            promotion = query.getSingleResult();
            System.out.println("[DEBUG] Found promotion: " + promotion);

        } catch (NoResultException e) {
            System.out.println("[DEBUG] No promotion found for code: " + code);
        } catch (Exception e) {
            System.err.println("[ERROR] Exception when finding promotion: ");
            e.printStackTrace();
        } finally {
            em.close();
        }

        return promotion;
    }
}
