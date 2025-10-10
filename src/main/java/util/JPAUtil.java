package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory emf = buildEntityManagerFactory();

    static {
        // Đóng EntityManagerFactory khi server tắt
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (emf != null && emf.isOpen()) {
                emf.close();
                System.out.println("EMF closed on server shutdown");
            }
        }));
    }

    private static EntityManagerFactory buildEntityManagerFactory() {
        try {
            // "bookstore" trùng tên persistence-unit trong persistence.xml
            return Persistence.createEntityManagerFactory("bookstore");
        } catch (Throwable ex) {
            System.err.println("Initial EntityManagerFactory creation failed. " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /** Lấy EntityManager mới mỗi lần gọi (thread-safe ở mức EMF) */
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
