package service;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

import dao.UserDao;
import jakarta.mail.MessagingException;
import model.LoginResult;
import model.User;

public class UserService {
    private UserDao userDao = new UserDao();
    private static final int PAGE_SIZE = 20;

    /**
     * Đăng ký hoặc trả về null nếu email đã tồn tại
     */
    public String register(User user, String rawPassword)
            throws MessagingException, UnsupportedEncodingException {
        Optional<User> existing = userDao.findByEmail(user.getEmail());
        if (existing.isPresent())
            return null;

        user.setPasswordHash(BCrypt.hashpw(rawPassword, BCrypt.gensalt()));
        user.setIsVerified(false);
        user.setRole("customer");

        String token = UUID.randomUUID().toString();
        user.setVerifyToken(token);
        user.setVerifyExpire(Timestamp.from(Instant.now().plus(15, ChronoUnit.MINUTES)));

        userDao.save(user);
        return token; // dùng để gửi mail xác thực
    }

    /**
     * Login, trả về LoginResult
     */
    public LoginResult login(String email, String rawPassword)
            throws MessagingException, UnsupportedEncodingException {
        LoginResult result = new LoginResult();

        Optional<User> userOpt = userDao.findByEmail(email);
        if (!userOpt.isPresent()) {
            result.setStatus(LoginResult.LoginStatus.INVALID);
            return result;
        }

        User user = userOpt.get();
        if (!BCrypt.checkpw(rawPassword, user.getPasswordHash())) {
            result.setStatus(LoginResult.LoginStatus.INVALID);
            return result;
        }

        if (!Boolean.TRUE.equals(user.getIsVerified())) {
            boolean tokenExpired = (user.getVerifyExpire() == null)
                    || user.getVerifyExpire().before(new java.util.Date());
            if (tokenExpired) {
                // Tạo token mới nếu token cũ hết hạn
                String token = UUID.randomUUID().toString();
                user.setVerifyToken(token);
                user.setVerifyExpire(Timestamp.from(Instant.now().plus(15, ChronoUnit.MINUTES)));
                userDao.updateVerifyToken(user);
            }

            result.setStatus(LoginResult.LoginStatus.UNVERIFIED);
            result.setUser(user);
            return result;
        }

        if (Boolean.TRUE.equals(user.getIsBlocked())) {
            result.setStatus(LoginResult.LoginStatus.BLOCKED);
            result.setBlockedUntil(user.getBlockedUntil());
            return result;
        }

        result.setUser(user);
        result.setStatus(LoginResult.LoginStatus.SUCCESS);
        return result;
    }

    /**
     * Xác thực token email
     */
    public boolean verifyUser(String token) {
        User user = userDao.findByVerifyToken(token);
        if (user == null)
            return false;
        if (user.getVerifyExpire().before(new java.util.Date()))
            return false;

        user.setIsVerified(true);
        userDao.markVerified(user.getId());
        return true;
    }

    public List<User> getAllUsers(int page) throws SQLException {
        return userDao.getAllUsers(page);
    }

    public List<User> getAdmins(int page) throws SQLException {
        return userDao.getAdmins(page);
    }

    public List<User> getCustomers(int page) throws SQLException {
        return userDao.getCustomers(page);
    }

    public List<User> getBlockedUsers(int page) throws SQLException {
        return userDao.getBlockedUsers(page);
    }

    public List<User> searchUsers(String query, int page) throws SQLException {
        return userDao.searchUsers(query, page);
    }

    public long getTotalUsers(String queryType, String query) throws SQLException {
        return userDao.countUsers(queryType, query);
    }

    public User getUserById(long id) throws SQLException {
        return userDao.getUserById(id);
    }

    public User getUserByEmail(String email) throws SQLException {
        return userDao.getUserByEmail(email);
    }

    public void deleteUser(long id) throws SQLException {
        userDao.deleteUser(id);
    }

    public void blockUser(long id) throws SQLException {
        userDao.blockUser(id);
    }

    public void unblockUser(long id) throws SQLException {
        userDao.unblockUser(id);
    }

    public void updateUser(User user) throws SQLException {
        userDao.updateUser(user);
    }

    public void updateUserPasswordHash(User user) throws SQLException {
        userDao.updateUserPasswordHash(user);
    }

    public void createAdmin(User user) throws SQLException {
        userDao.createAdmin(user);
    }

    public void createUser(User user) throws SQLException {
        userDao.createUser(user);
    }

    public int getTotalPages(String queryType, String query) throws SQLException {
        long totalUsers = getTotalUsers(queryType, query);
        return (int) Math.ceil((double) totalUsers / PAGE_SIZE);
    }
}
