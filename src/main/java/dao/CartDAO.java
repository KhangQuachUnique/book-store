package dao;

import jakarta.persistence.*;
import model.Book;
import model.Cart;
import model.CartItem;
import model.User;
import util.JPAUtil;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartDAO {

    private static final Logger log = Logger.getLogger(CartDAO.class.getName());

    private final EntityManagerFactory emf;

    public CartDAO() {
        this.emf = JPAUtil.getEntityManagerFactory();
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    private Cart findCart(EntityManager em, Long userId) {
        return em.createQuery("SELECT c FROM Cart c WHERE c.user.id = :userId", Cart.class)
                .setParameter("userId", userId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public void createCartForUser(Long userId) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User user = em.getReference(User.class, userId);
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setItems(new ArrayList<>());
            em.persist(cart);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            log.log(Level.SEVERE, "Error creating cart for user", e);
            throw e;
        } finally {
            em.close();
        }
    }

    public Cart getCartWithItems(Long userId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Cart> query = em.createQuery("""
                    SELECT DISTINCT c FROM Cart c
                    LEFT JOIN FETCH c.items ci
                    LEFT JOIN FETCH ci.book b
                    WHERE c.user.id = :userId
                    """, Cart.class);
            query.setParameter("userId", userId);

            Cart cart = query.getResultStream().findFirst().orElse(null);
            if (cart != null && cart.getItems() != null) {
                cart.getItems().size();
            }
            return cart;
        } finally {
            em.close();
        }
    }

    public void addToCart(Long userId, Integer bookId, int quantity) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Cart cart = findCart(em, userId);
            if (cart == null) {
                cart = new Cart();
                cart.setUser(em.getReference(User.class, userId));
                cart.setItems(new ArrayList<>());
                em.persist(cart);
            } else {
                cart.getItems().size();
            }

            Book book = em.find(Book.class, bookId);
            if (book == null) {
                throw new IllegalArgumentException("Book not found with id=" + bookId);
            }

            CartItem item = cart.getItems().stream()
                    .filter(ci -> ci.getBook().getId().equals(bookId))
                    .findFirst()
                    .orElse(null);

            if (item == null) {
                item = new CartItem();
                item.setBook(book);
                item.setCart(cart);
                item.setQuantity(Math.max(1, quantity));
                item.setUnitPrice(book.getPrice());
                cart.addItem(item);
                em.persist(item);
            } else {
                item.increaseQuantity(Math.max(1, quantity));
                item.setUnitPrice(book.getPrice());
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            log.log(Level.SEVERE, "Error adding to cart", e);
            throw e;
        } finally {
            em.close();
        }
    }

    public void updateCartQuantity(Long userId, Long cartItemId, int quantity) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            CartItem cartItem = em.createQuery("""
                            SELECT ci FROM CartItem ci
                            JOIN FETCH ci.book
                            WHERE ci.id = :itemId AND ci.cart.user.id = :userId
                    """, CartItem.class)
                    .setParameter("itemId", cartItemId)
                    .setParameter("userId", userId)
                    .getSingleResult();

            if (quantity <= 0) {
                cartItem.getCart().removeItem(cartItem);
                em.remove(cartItem);
            } else {
                cartItem.setQuantitySafely(quantity);
                cartItem.setUnitPrice(cartItem.getBook().getPrice());
            }

            tx.commit();
        } catch (NoResultException ignored) {
            if (tx.isActive()) {
                tx.rollback();
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            log.log(Level.SEVERE, "Error updating cart quantity", e);
            throw e;
        } finally {
            em.close();
        }
    }

    public void removeFromCart(Long userId, Long cartItemId) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            CartItem cartItem = em.createQuery("""
                            SELECT ci FROM CartItem ci
                            WHERE ci.id = :itemId AND ci.cart.user.id = :userId
                    """, CartItem.class)
                    .setParameter("itemId", cartItemId)
                    .setParameter("userId", userId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (cartItem != null) {
                cartItem.getCart().removeItem(cartItem);
                em.remove(cartItem);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            log.log(Level.SEVERE, "Error removing cart item", e);
            throw e;
        } finally {
            em.close();
        }
    }

    public void clearCart(Long userId) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Cart cart = findCart(em, userId);
            if (cart != null) {
                cart.clearItems();
                em.createQuery("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId")
                        .setParameter("cartId", cart.getId())
                        .executeUpdate();
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            log.log(Level.SEVERE, "Error clearing cart", e);
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Đếm tổng số lượng items trong giỏ hàng
     *
     * @param userId ID của user
     * @return Tổng số lượng items (sum của quantity)
     */
    public int countItems(Long userId) {
        EntityManager em = getEntityManager();
        try {
            Long count = em.createQuery("""
                    SELECT COUNT(ci.id)
                    FROM CartItem ci
                    WHERE ci.cart.user.id = :userId
                    """, Long.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error counting cart items", e);
            return 0;
        } finally {
            em.close();
        }
    }
}
