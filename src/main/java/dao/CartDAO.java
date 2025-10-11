package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.Book;
import model.Cart;
import model.CartItem;
import model.User;
import util.JPAUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartDAO {
    private static final Logger log = Logger.getLogger(CartDAO.class.getName());

    public static void updateCartQuantity(int cartId, int quantity) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            CartItem cartItem = em.find(CartItem.class, cartId);
            if (cartItem != null) {
                cartItem.setQuantity(quantity);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.log(Level.SEVERE, "Error updating cart quantity", e);
            throw new RuntimeException("Error updating cart quantity", e);
        } finally {
            em.close();
        }
    }

    public static void addToCart(int userId, int bookId, int quantity) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            
            // Find or create user's cart
            Cart cart = null;
            try {
                TypedQuery<Cart> cartQuery = em.createQuery(
                    "SELECT c FROM Cart c WHERE c.user.id = :userId", Cart.class);
                cartQuery.setParameter("userId", (long) userId);
                cart = cartQuery.getSingleResult();
            } catch (NoResultException e) {
                // Create new cart for user
                cart = new Cart();
                User user = em.getReference(User.class, (long) userId);
                cart.setUser(user);
                em.persist(cart);
                em.flush(); // Ensure cart ID is generated
            }
            
            // Check if item already exists in cart
            TypedQuery<CartItem> itemQuery = em.createQuery(
                "SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.book.id = :bookId", CartItem.class);
            itemQuery.setParameter("cartId", cart.getId());
            itemQuery.setParameter("bookId", bookId);
            
            CartItem existingItem = null;
            try {
                existingItem = itemQuery.getSingleResult();
            } catch (NoResultException e) {
                // Item doesn't exist, will create new
            }
            
            if (existingItem != null) {
                // Update existing quantity
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
            } else {
                // Create new cart item
                CartItem newItem = new CartItem();
                Book book = em.getReference(Book.class, bookId);
                newItem.setCart(cart);
                newItem.setBook(book);
                newItem.setQuantity(quantity);
                em.persist(newItem);
            }
            
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.log(Level.SEVERE, "Error adding to cart", e);
            throw new RuntimeException("Error adding to cart", e);
        } finally {
            em.close();
        }
    }

    public static List<CartItem> getCartByUser(int userId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<CartItem> query = em.createQuery(
                "SELECT ci FROM CartItem ci LEFT JOIN FETCH ci.book WHERE ci.cart.user.id = :userId", CartItem.class);
            query.setParameter("userId", (long) userId);
            return query.getResultList();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error getting cart by user", e);
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    public static void removeFromCart(int cartId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            CartItem cartItem = em.find(CartItem.class, cartId);
            if (cartItem != null) {
                em.remove(cartItem);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.log(Level.SEVERE, "Error removing from cart", e);
            throw new RuntimeException("Error removing from cart", e);
        } finally {
            em.close();
        }
    }

    public static void clearCart(int userId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            int deleted = em.createQuery("DELETE FROM CartItem ci WHERE ci.cart.user.id = :userId")
                    .setParameter("userId", (long) userId)
                    .executeUpdate();
            em.getTransaction().commit();
            log.info("Cleared " + deleted + " items from cart for user " + userId);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.log(Level.SEVERE, "Error clearing cart", e);
            throw new RuntimeException("Error clearing cart", e);
        } finally {
            em.close();
        }
    }
}
