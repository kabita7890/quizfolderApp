package com.quizapp.dao;
import com.quizapp.db.Database;
import com.quizapp.model.Attempt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttemptDao {
    public void recordAttempt(int userId, int quizId, int score, int total) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO attempts(user_id, quiz_id, score, total) VALUES(?,?,?,?)")) {
            ps.setInt(1, userId);
            ps.setInt(2, quizId);
            ps.setInt(3, score);
            ps.setInt(4, total);
            ps.executeUpdate();
        }
    }

    public java.util.List<Attempt> historyForUser(int userId) throws SQLException {
        java.util.List<Attempt> list = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT id, user_id, quiz_id, score, total, taken_at FROM attempts WHERE user_id = ? ORDER BY taken_at DESC")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Attempt(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6)));
                }
            }
        }
        return list;
    }

    public java.util.List<Attempt> leaderboard() throws SQLException {
        java.util.List<Attempt> list = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT id, user_id, quiz_id, score, total, taken_at FROM attempts ORDER BY (score*1.0/total) DESC, taken_at ASC LIMIT 20")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Attempt(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6)));
                }
            }
        }
        return list;
    }
}
