package dao;

import java.sql.Timestamp;

import jakarta.persistence.*;
import model.Book;
import model.User;
import model.WishList;
import model.WishListItem;
import util.JPAUtil;

public class WishListDao {
    private final EntityManagerFactory emf;

    public WishListDao() {
        this.emf = JPAUtil.getEntityManagerFactory();
    }

    /**
     * Retrieves a paginated list of books in the specified user's wishlist.
     *
     * @param userId the ID of the user
     * @return a list of {@link Book} objects in the user's wishlist
     */
    public WishList getWishListByUser(Long userId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<WishList> query = em.createQuery("""
                SELECT wl FROM WishList wl
                LEFT JOIN FETCH wl.items wli
                LEFT JOIN FETCH wli.book
                WHERE wl.user.id = :userId
                ORDER BY wli.addedAt DESC""",
                WishList.class
            );
            query.setParameter("userId", userId);

            // Chỉ lấy 1 wishlist (mỗi user chỉ có 1)
            return query.getResultStream().findFirst().orElse(null);

        } finally {
            em.close();
        }
    }

    /**
     * Returns the list of books in the user's wishlist.
     * @param userId the user's ID
     * @param bookId the book's ID to add
     * @return "success" if the book was added, "exists" if it was already in the wishlist, "error" if there was an error
     */
    public boolean addBookToWishlist(Long userId, Long bookId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            WishList wishList = em.createQuery(
                            "SELECT wl FROM WishList wl WHERE wl.user.id = :userId", WishList.class)
                    .setParameter("userId", userId)
                    .getResultStream()
                    .findFirst()
                    .orElseGet(() -> {
                        WishList wl = new WishList();
                        wl.setUser(em.find(User.class, userId));
                        em.persist(wl);
                        return wl;
                    });

            Book book = em.find(Book.class, bookId);
            if (book == null) return false;

            WishListItem item = new WishListItem();
            item.setWishList(wishList);
            item.setBook(book);
            item.setAddedAt(new Timestamp(System.currentTimeMillis()));
            em.persist(item);

            tx.commit();
            return true;
        } catch (PersistenceException e) {
            // Nếu vi phạm unique constraint thì rollback và return false
            if (tx.isActive()) tx.rollback();
            return false;
        } finally {
            em.close();
        }
    }

    /**
     * Deletes a book from the user's wishlist.
     * @param userId the user's ID
     * @param bookId the book's ID to delete
     * @return true if the book was deleted, false otherwise
     */
    public boolean removeBookFromWishlist(Long userId, Long bookId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            int deleted = em.createQuery(
                            "DELETE FROM WishListItem wli " +
                                    "WHERE wli.wishList.user.id = :userId " +
                                    "AND wli.book.id = :bookId"
                    )
                    .setParameter("userId", userId)
                    .setParameter("bookId", bookId)
                    .executeUpdate();

            tx.commit();
            return deleted > 0; // true nếu có ít nhất 1 bản ghi bị xóa

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;

        } finally {
            em.close();
        }
    }

}
