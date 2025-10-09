package util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class TestJPA {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bookstore");
        System.out.println("EMF created: " + emf);
        emf.close();
    }
}