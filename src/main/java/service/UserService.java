package service;

import dao.UserDao;
import model.User;
import model.LoginResult;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class UserService {
    private UserDao userDao = new UserDao();

    public boolean register(User user, String rawPassword) {
        Optional<User> existing = userDao.findByEmail(user.getEmail());
        if (existing.isPresent()) {
            return false;
        }
        String hashed = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        user.setPasswordHash(hashed);
        user.setRole("customer");
        user.setIsBlocked(false);
        return userDao.save(user);
    }

    public LoginResult login(String email, String rawPassword) {
        LoginResult result = new LoginResult();

        Optional<User> userOpt = userDao.findByEmail(email);
        if (!userOpt.isPresent()) {
            result.setStatus(LoginResult.LoginStatus.INVALID);
            return result;
        }

        User user = userOpt.get();

        // kiểm tra block
        if (userDao.isUserBlocked(email)) {
            result.setStatus(LoginResult.LoginStatus.BLOCKED);
            result.setBlockedUntil(user.getBlockedUntil()); // nếu có cột blocked_until
            return result;
        }

        // kiểm tra mật khẩu
        if (!BCrypt.checkpw(rawPassword, user.getPasswordHash())) {
            result.setStatus(LoginResult.LoginStatus.INVALID);
            return result;
        }

        // login thành công
        result.setUser(user);
        result.setStatus(LoginResult.LoginStatus.SUCCESS);
        return result;
    }
}
