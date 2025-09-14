package service;

import dao.UserDao;
import model.LoginResult;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserService {
    private UserDao userDAO = new UserDao();
    private static final int PAGE_SIZE = 20;

    public boolean register(User user, String rawPassword) {
        Optional<User> existing = userDAO.findByEmail(user.getEmail());
        if (existing.isPresent()) {
            return false;
        }

        if (!util.ValidatorUtil.isValidEmail(user.getEmail())) {
            return false;
        }
        if (!util.ValidatorUtil.isValidPhoneNumber(user.getPhone())) {
            return false;
        }

        String hashed = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        user.setPasswordHash(hashed);
        user.setRole("customer");
        user.setIsBlocked(false);
        return userDAO.save(user);
    }

    public LoginResult login(String email, String rawPassword) {
        LoginResult result = new LoginResult();

        Optional<User> userOpt = userDAO.findByEmail(email);
        if (!userOpt.isPresent()) {
            result.setStatus(LoginResult.LoginStatus.INVALID);
            return result;
        }

        User user = userOpt.get();

        if (user.getIsBlocked() != null && user.getIsBlocked()) {
            result.setStatus(LoginResult.LoginStatus.BLOCKED);
            result.setBlockedUntil(user.getBlockedUntil());
            return result;
        }
        if (user.getBlockedUntil() != null && user.getBlockedUntil().after(new java.sql.Timestamp(System.currentTimeMillis()))) {
            result.setStatus(LoginResult.LoginStatus.BLOCKED);
            result.setBlockedUntil(user.getBlockedUntil());
            return result;
        }

        if (!BCrypt.checkpw(rawPassword, user.getPasswordHash())) {
            result.setStatus(LoginResult.LoginStatus.INVALID);
            return result;
        }

        result.setUser(user);
        result.setStatus(LoginResult.LoginStatus.SUCCESS);
        return result;
    }

    public List<User> getAllUsers(int page) throws SQLException {
        return userDAO.getAllUsers(page);
    }

    public List<User> getAdmins(int page) throws SQLException {
        return userDAO.getAdmins(page);
    }

    public List<User> getCustomers(int page) throws SQLException {
        return userDAO.getCustomers(page);
    }

    public List<User> getBlockedUsers(int page) throws SQLException {
        return userDAO.getBlockedUsers(page);
    }

    public List<User> searchUsers(String query, int page) throws SQLException {
        return userDAO.searchUsers(query, page);
    }

    public long getTotalUsers(String queryType, String query) throws SQLException {
        return userDAO.countUsers(queryType, query);
    }

    public User getUserById(long id) throws SQLException {
        return userDAO.getUserById(id);
    }

    public void deleteUser(long id) throws SQLException {
        userDAO.deleteUser(id);
    }

    public void blockUser(long id) throws SQLException {
        userDAO.blockUser(id);
    }

    public void unblockUser(long id) throws SQLException {
        userDAO.unblockUser(id);
    }

    public void updateUser(User user) throws SQLException {
        userDAO.updateUser(user);
    }

    public void createAdmin(User user) throws SQLException {
        userDAO.createAdmin(user);
    }

    public void createUser(User user) throws SQLException {
        userDAO.createUser(user);
    }

    public int getTotalPages(String queryType, String query) throws SQLException {
        long totalUsers = getTotalUsers(queryType, query);
        return (int) Math.ceil((double) totalUsers / PAGE_SIZE);
    }
}