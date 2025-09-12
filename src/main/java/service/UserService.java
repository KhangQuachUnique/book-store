package service;

import dao.UserDAO;
import model.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
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
}