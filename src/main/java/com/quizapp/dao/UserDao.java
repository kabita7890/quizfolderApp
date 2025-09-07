package com.quizapp.dao;

import com.quizapp.db.Database;
import com.quizapp.model.User;
import com.quizapp.security.PasswordHasher;

import java.sql.*;

public class UserDao {
    public User findByUsername(String username) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT id, username, password_hash FROM users WHERE username = ?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt(1), rs.getString(2), rs.getString(3));
                }
                return null;
            }
        }
    }

    public User register(String username, String plainPassword) throws SQLException {
        String hashed = PasswordHasher.hash(plainPassword);
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO users(username, password_hash) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, hashed);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return new User(rs.getInt(1), username, hashed);
            }
        }
        throw new SQLException("Registration failed");
    }

    public User login(String username, String plainPassword) throws SQLException {
        User u = findByUsername(username);
        if (u != null && com.quizapp.security.PasswordHasher.verify(plainPassword, u.passwordHash)) {
            return u;
        }
        return null;
    }
}
