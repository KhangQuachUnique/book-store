package util;

import jakarta.persistence.*;

public class JPAUtil {
    private static final EntityManagerFactory emf = buildEntityManagerFactory();

    static {
        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (emf.isOpen()) {
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
            System.err.println("Initial EntityManagerFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static void close() {
        emf.close();
    }
}
