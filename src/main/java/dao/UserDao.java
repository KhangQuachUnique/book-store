package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.User;
import util.JPAUtil;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao {
    private static final Logger log = Logger.getLogger(UserDao.class.getName());
    private static final int pageSize = 20;

    private EntityManager getEntityManager() {
        return JPAUtil.getEntityManagerFactory().createEntityManager();
    }

    // Tìm user bằng verify_token
    public User findByVerifyToken(String token) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.verifyToken = :token", User.class);
            query.setParameter("token", token);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error finding user by verify token", e);
            return null;
        } finally {
            em.close();
        }
    }

    public void markVerified(long userId) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, userId);
            if (user != null) {
                user.setIsVerified(true);
                user.setVerifyToken(null);
                user.setVerifyExpire(null);
                em.getTransaction().commit();
            } else {
                em.getTransaction().rollback();
                throw new SQLException("User not found");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SQLException("Error marking user verified", e);
        } finally {
            em.close();
        }
    }

    public Optional<User> findByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u LEFT JOIN FETCH u.addresses WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            User user = query.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error finding user by email: " + email, e);
            throw new RuntimeException("Database error occurred", e);
        } finally {
            em.close();
        }
    }

    // Lưu user mới
    public boolean save(User user) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.log(Level.SEVERE, "Error saving user: " + user.getEmail(), e);
            throw new SQLException("Database error occurred", e);
        } finally {
            em.close();
        }
    }

    // Cập nhật token và thời hạn xác thực mới
    public void updateVerifyToken(User user) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            User managedUser = em.find(User.class, user.getId());
            if (managedUser != null) {
                managedUser.setVerifyToken(user.getVerifyToken());
                managedUser.setVerifyExpire(user.getVerifyExpire());
                em.getTransaction().commit();
            } else {
                em.getTransaction().rollback();
                throw new SQLException("User not found");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SQLException("Error updating verify token", e);
        } finally {
            em.close();
        }
    }

    // Update password and clear verification token (for password reset)
    public void updatePasswordAndClearToken(Long userId, String hashedPassword) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, userId);
            if (user != null) {
                user.setPasswordHash(hashedPassword);
                user.setVerifyToken(null);
                user.setVerifyExpire(null);
                em.getTransaction().commit();
            } else {
                em.getTransaction().rollback();
                throw new SQLException("User not found");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.log(Level.SEVERE, "Error updating password for user: " + userId, e);
            throw new SQLException("Database error occurred", e);
        } finally {
            em.close();
        }
    }

    // Trả về true/false
    public boolean isUserBlocked(String email) {
        return getBlockInfo(email) != null;
    }

    // Trả về thời gian unblock (nếu có), null nếu không bị block
    public Timestamp getBlockInfo(String email) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT u.isBlocked, u.blockedUntil FROM User u WHERE u.email = :email", Object[].class);
            query.setParameter("email", email);
            Object[] result = query.getSingleResult();
            Boolean isBlocked = (Boolean) result[0];
            Timestamp until = (Timestamp) result[1];
            if (isBlocked != null && isBlocked) {
                return until;
            }
            if (until != null && until.after(new Timestamp(System.currentTimeMillis()))) {
                return until;
            }
            return null;
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error getting block info for: " + email, e);
            throw new RuntimeException("Database error occurred", e);
        } finally {
            em.close();
        }
    }

    public List<User> getAllUsers(int page) {
        EntityManager em = getEntityManager();
        try {
            int firstResult = (page - 1) * pageSize;
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u ORDER BY u.id", User.class);
            query.setFirstResult(firstResult);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error getting all users", e);
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    public List<User> getAdmins(int page) {
        EntityManager em = getEntityManager();
        try {
            int firstResult = (page - 1) * pageSize;
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.role = 'ADMIN' ORDER BY u.id", User.class);
            query.setFirstResult(firstResult);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error getting admins", e);
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    public List<User> getCustomers(int page) {
        EntityManager em = getEntityManager();
        try {
            int firstResult = (page - 1) * pageSize;
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.role = 'USER' ORDER BY u.id", User.class);
            query.setFirstResult(firstResult);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error getting customers", e);
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    public List<User> getBlockedUsers(int page) {
        EntityManager em = getEntityManager();
        try {
            int firstResult = (page - 1) * pageSize;
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.isBlocked = true ORDER BY u.id", User.class);
            query.setFirstResult(firstResult);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error getting blocked users", e);
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    public List<User> searchUsers(String query, int page) {
        EntityManager em = getEntityManager();
        try {
            int firstResult = (page - 1) * pageSize;
            String searchTerm = "%" + query.toLowerCase() + "%";
            TypedQuery<User> typedQuery = em.createQuery(
                    "SELECT u FROM User u WHERE LOWER(u.name) LIKE :searchTerm OR LOWER(u.email) LIKE :searchTerm OR LOWER(u.phoneNumber) LIKE :searchTerm ORDER BY u.id", User.class);
            typedQuery.setParameter("searchTerm", searchTerm);
            typedQuery.setFirstResult(firstResult);
            typedQuery.setMaxResults(pageSize);
            return typedQuery.getResultList();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error searching users", e);
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    public long countUsers(String queryType, String query) {
        EntityManager em = getEntityManager();
        try {
            String jpql;
            if ("search".equals(queryType)) {
                String searchTerm = "%" + query.toLowerCase() + "%";
                jpql = "SELECT COUNT(u) FROM User u WHERE LOWER(u.name) LIKE :searchTerm OR LOWER(u.email) LIKE :searchTerm OR LOWER(u.phoneNumber) LIKE :searchTerm";
                TypedQuery<Long> typedQuery = em.createQuery(jpql, Long.class);
                typedQuery.setParameter("searchTerm", searchTerm);
                return typedQuery.getSingleResult();
            } else if ("listAdmins".equals(queryType)) {
                jpql = "SELECT COUNT(u) FROM User u WHERE u.role = 'ADMIN'";
            } else if ("listUsers".equals(queryType)) {
                jpql = "SELECT COUNT(u) FROM User u WHERE u.role = 'USER'";
            } else if ("listBlocked".equals(queryType)) {
                jpql = "SELECT COUNT(u) FROM User u WHERE u.isBlocked = true";
            } else {
                jpql = "SELECT COUNT(u) FROM User u";
            }
            TypedQuery<Long> typedQuery = em.createQuery(jpql, Long.class);
            return typedQuery.getSingleResult();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error counting users", e);
            return 0;
        } finally {
            em.close();
        }
    }

    public User getUserById(long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error getting user by id: " + id, e);
            return null;
        } finally {
            em.close();
        }
    }

    public User getUserByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error getting user by email: " + email, e);
            return null;
        } finally {
            em.close();
        }
    }

    public void deleteUser(long id) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            // Find the user
            User user = em.find(User.class, id);
            if (user != null) {
                // Delete dependent reviews
                em.createQuery("DELETE FROM Review r WHERE r.user.id = :userId")
                        .setParameter("userId", id)
                        .executeUpdate();
                // Delete the user
                em.remove(user);
                em.getTransaction().commit();
            } else {
                em.getTransaction().rollback();
                throw new SQLException("User not found");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.log(Level.SEVERE, "Error deleting user with id: " + id, e);
            throw new SQLException("Error deleting user", e);
        } finally {
            em.close();
        }
    }

    public void blockUser(long id) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user != null) {
                user.setIsBlocked(true);
                user.setBlockedUntil(Timestamp.valueOf(LocalDateTime.now().plusMonths(1)));
                em.getTransaction().commit();
            } else {
                em.getTransaction().rollback();
                throw new SQLException("User not found");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SQLException("Error blocking user", e);
        } finally {
            em.close();
        }
    }


    public void unblockUser(long id) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user != null) {
                user.setIsBlocked(false);
                user.setBlockedUntil(null);
                em.getTransaction().commit();
            } else {
                em.getTransaction().rollback();
                throw new SQLException("User not found");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SQLException("Error unblocking user", e);
        } finally {
            em.close();
        }
    }

    public void updateUser(User user) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            User managedUser = em.find(User.class, user.getId());
            if (managedUser != null) {
                managedUser.setName(user.getName());
                managedUser.setEmail(user.getEmail());
                managedUser.setPhoneNumber(user.getPhoneNumber());
                managedUser.setRole(user.getRole());
                em.getTransaction().commit();
            } else {
                em.getTransaction().rollback();
                throw new SQLException("User not found");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SQLException("Error updating user", e);
        } finally {
            em.close();
        }
    }

    public void updateUserPasswordHash(User user) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            User managedUser = em.find(User.class, user.getId());
            if (managedUser != null) {
                managedUser.setPasswordHash(user.getPasswordHash());
                em.getTransaction().commit();
            } else {
                em.getTransaction().rollback();
                throw new SQLException("User not found");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SQLException("Error updating user password hash", e);
        } finally {
            em.close();
        }
    }

    public void createAdmin(User user) throws SQLException {
        user.setRole(model.Role.ADMIN);
        save(user);
    }

    public void createUser(User user) throws SQLException {
        user.setRole(model.Role.USER);
        save(user);
    }
}