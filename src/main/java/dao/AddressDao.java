package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.Address;
import model.User;
import util.JPAUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddressDao {
    private static final Logger log = Logger.getLogger(AddressDao.class.getName());

    private EntityManager getEntityManager() {
        return JPAUtil.getEntityManagerFactory().createEntityManager();
    }

    public List<Address> getAddressesByUserId(long userId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a WHERE a.user.id = :userId ORDER BY a.id", Address.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error getting addresses by userId: " + userId, e);
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    public long createAddress(long userId, String addressText, boolean isDefaultAddress) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, userId);
            if (user == null) {
                em.getTransaction().rollback();
                throw new SQLException("User not found");
            }
            Address address = new Address();
            address.setAddress(addressText);
            address.setDefaultAddress(isDefaultAddress);
            address.setUser(user);
            user.getAddresses().add(address);
            em.persist(address);
            em.flush(); // Ensure ID is generated
            em.getTransaction().commit();
            return address.getId();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SQLException("Error creating address", e);
        } finally {
            em.close();
        }
    }

    public void updateAddress(long userId, long addressId, String addressText, boolean isDefaultAddress) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            int updatedRows = em.createQuery(
                            "UPDATE Address a SET a.address = :addressText, a.isDefault = :isDefaultAddress WHERE a.id = :addressId AND a.user.id = :userId")
                    .setParameter("addressText", addressText)
                    .setParameter("isDefaultAddress", isDefaultAddress)
                    .setParameter("addressId", addressId)
                    .setParameter("userId", userId)
                    .executeUpdate();
            em.getTransaction().commit();
            if (updatedRows == 0) {
                throw new SQLException("Address ID " + addressId + " not found or does not belong to user " + userId);
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SQLException("Error updating address", e);
        } finally {
            em.close();
        }
    }

    public void setDefaultAddress(long addressId, long userId) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            em.createQuery("UPDATE Address a SET a.isDefault = false WHERE a.user.id = :userId")
                    .setParameter("userId", userId)
                    .executeUpdate();

            int updatedRows = em.createQuery("UPDATE Address a SET a.isDefault = true WHERE a.id = :addressId AND a.user.id = :userId")
                    .setParameter("addressId", addressId)
                    .setParameter("userId", userId)
                    .executeUpdate();
            em.getTransaction().commit();
            if (updatedRows == 0) {
                throw new SQLException("Address ID " + addressId + " not found or does not belong to user " + userId);
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SQLException("Error setting default address", e);
        } finally {
            em.close();
        }
    }

    public Address findByIdAndUserId(long addressId, long userId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a WHERE a.id = :addressId AND a.user.id = :userId", Address.class);
            query.setParameter("addressId", addressId);
            query.setParameter("userId", userId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error finding address by id and userId", e);
            return null;
        } finally {
            em.close();
        }
    }

    public void deleteAddress(long addressId, long userId) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            int deletedRows = em.createQuery("DELETE FROM Address a WHERE a.id = :addressId AND a.user.id = :userId")
                    .setParameter("addressId", addressId)
                    .setParameter("userId", userId)
                    .executeUpdate();
            em.getTransaction().commit();
            if (deletedRows == 0) {
                throw new SQLException("Address ID " + addressId + " not found or does not belong to user " + userId);
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SQLException("Error deleting address", e);
        } finally {
            em.close();
        }
    }
}