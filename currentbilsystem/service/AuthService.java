package service;

import dao.UserDAO;
import model.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class AuthService {
    private UserDAO userDAO;

    public AuthService(Connection conn) {
        this.userDAO = new UserDAO(conn);
    }

    public boolean register(String username, String password, String role) throws SQLException {
        if (userDAO.getUserByUsername(username).isPresent()) {
            return false; // Username already exists
        }
        User user = new User(0, username, password, role);
        return userDAO.register(user);
    }

    public Optional<User> login(String username, String password) throws SQLException {
        return userDAO.login(username, password);
    }
} 