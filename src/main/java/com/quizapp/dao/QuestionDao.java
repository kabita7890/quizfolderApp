package com.quizapp.dao;

import com.quizapp.db.Database;
import com.quizapp.model.Option;
import com.quizapp.model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDao {
    public List<Question> findByQuiz(int quizId) throws SQLException {
        List<Question> qs = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT id, quiz_id, text, multiple_correct FROM questions WHERE quiz_id = ?")) {
            ps.setInt(1, quizId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    qs.add(new Question(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4) == 1));
                }
            }
        }
        return qs;
    }

    public List<Option> optionsForQuestion(int questionId) throws SQLException {
        List<Option> list = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT id, question_id, text, is_correct FROM options WHERE question_id = ?")) {
            ps.setInt(1, questionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Option(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4) == 1));
                }
            }
        }
        return list;
    }
}
