// package service;

// import dao.UserDao;
// import model.User;
// import model.LoginResult;
// import org.mindrot.jbcrypt.BCrypt;

// import java.util.Optional;

// import model.User;

// import java.sql.SQLException;
// import java.util.List;

// public class UserService {
//     private UserDao userDao = new UserDao();

//     public boolean register(User user, String rawPassword) {
//         Optional<User> existing = userDao.findByEmail(user.getEmail());
//         if (existing.isPresent()) {
//             return false;
//         }
//         String hashed = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
//         user.setPasswordHash(hashed);
//         user.setRole("customer");
//         user.setIsBlocked(false);
//         return userDao.save(user);
//     }

//     public LoginResult login(String email, String rawPassword) {
//         LoginResult result = new LoginResult();

//         Optional<User> userOpt = userDao.findByEmail(email);
//         if (!userOpt.isPresent()) {
//             result.setStatus(LoginResult.LoginStatus.INVALID);
//             return result;
//         }

//         User user = userOpt.get();

//         // kiểm tra block
//         if (userDao.isUserBlocked(email)) {
//             result.setStatus(LoginResult.LoginStatus.BLOCKED);
//             result.setBlockedUntil(user.getBlockedUntil()); // nếu có cột blocked_until
//             return result;
//         }

//         // kiểm tra mật khẩu
//         if (!BCrypt.checkpw(rawPassword, user.getPasswordHash())) {
//             result.setStatus(LoginResult.LoginStatus.INVALID);
//             return result;
//         }

//         // login thành công
//         result.setUser(user);
//         result.setStatus(LoginResult.LoginStatus.SUCCESS);
//         return result;
//     }

//     private UserDao userDAO = new UserDao();

//     public List<User> getAllUsers() throws SQLException {
//         return userDAO.getAllUsers();
//     }

//     public List<User> getAdmins() throws SQLException {
//         return userDAO.getAdmins();
//     }

//     public List<User> getCustomers() throws SQLException {
//         return userDAO.getCustomers();
//     }

//     public List<User> getBlockedUsers() throws SQLException {
//         return userDAO.getBlockedUsers();
//     }

//     public List<User> searchUsers(String query) throws SQLException {
//         return userDAO.searchUsers(query);
//     }

//     public User getUserById(long id) throws SQLException {
//         return userDAO.getUserById(id);
//     }

//     public void deleteUser(long id) throws SQLException {
//         userDAO.deleteUser(id);
//     }

//     public void blockUser(long id) throws SQLException {
//         userDAO.blockUser(id);
//     }

//     public void unblockUser(long id) throws SQLException {
//         userDAO.unblockUser(id);
//     }

//     public void updateUser(User user) throws SQLException {
//         userDAO.updateUser(user);
//     }

//     public void createAdmin(User user) throws SQLException {
//         userDAO.createAdmin(user);
//     }

//     public void createUser(User user) throws SQLException {
//         userDAO.createUser(user);
//     }
// }