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

    // ✅ Tìm khuyến mãi theo mã code (dành cho người dùng nhập)
    public Promotion getPromotionByCode(String code) {
        EntityManager em = emf.createEntityManager();
        Promotion promotion = null;

        try {
            System.out.println("[DEBUG] Finding promotion with code=" + code);

            // Truy vấn chính xác + kiểm tra hạn sử dụng
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

    // ✅ Lấy tất cả các promotion còn hiệu lực (dùng cho <datalist>)
    public List<Promotion> getAllValidPromotions() {
        EntityManager em = emf.createEntityManager();
        List<Promotion> promotions = new ArrayList<>();

        try {
            System.out.println("[DEBUG] Fetching all valid promotions...");

            String jpql = "SELECT p FROM Promotion p WHERE p.expireAt >= :now ORDER BY p.expireAt ASC";
            TypedQuery<Promotion> query = em.createQuery(jpql, Promotion.class);
            query.setParameter("now", OffsetDateTime.now());
            promotions = query.getResultList();

            System.out.println("[DEBUG] Found " + promotions.size() + " valid promotions");
        } catch (Exception e) {
            System.err.println("[ERROR] Exception when fetching valid promotions: ");
            e.printStackTrace();
        } finally {
            em.close();
        }

        return promotions;
    }
}