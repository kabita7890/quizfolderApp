package com.quizapp.dao;

import com.quizapp.db.Database;
import com.quizapp.model.Quiz;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDao {
    public List<Quiz> findAll() throws SQLException {
        List<Quiz> list = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT id, title, description, created_by FROM quizzes ORDER BY id DESC")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Quiz(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
                }
            }
        }
        return list;
    }

    public int countQuestions(int quizId) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM questions WHERE quiz_id = ?")) {
            ps.setInt(1, quizId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}
