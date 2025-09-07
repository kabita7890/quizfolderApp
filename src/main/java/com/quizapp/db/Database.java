package com.quizapp.db;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:quiz.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void init() {
        try (Connection conn = getConnection()) {
            InputStream in = Database.class.getClassLoader().getResourceAsStream("schema.sql");
            if (in == null) throw new RuntimeException("schema.sql not found");
            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            try (Statement stmt = conn.createStatement()) {
                for (String part : sql.split(";")) {
                    String s = part.trim();
                    if (!s.isEmpty()) stmt.execute(s);
                }
            }
            seedIfEmpty(conn);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize DB: " + e.getMessage());
        }
    }

    private static void seedIfEmpty(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM users");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getInt(1) == 0) {
                String hash = com.quizapp.security.PasswordHasher.hash("admin123");
                try (PreparedStatement ins = conn.prepareStatement("INSERT INTO users(username, password_hash) VALUES(?, ?)")) {
                    ins.setString(1, "admin");
                    ins.setString(2, hash);
                    ins.executeUpdate();
                }
            }
        }
        try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM quizzes");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getInt(1) == 0) {
                conn.setAutoCommit(false);
                try {
                    try (PreparedStatement qz = conn.prepareStatement("INSERT INTO quizzes(title, description, created_by) VALUES(?,?,1)", Statement.RETURN_GENERATED_KEYS)) {
                        qz.setString(1, "Java Basics");
                        qz.setString(2, "Quick quiz on Java fundamentals");
                        qz.executeUpdate();
                        try (ResultSet gk = qz.getGeneratedKeys()) {
                            if (gk.next()) {
                                int quizId = gk.getInt(1);
                                addQuestion(conn, quizId, "Which keyword is used to inherit a class in Java?", new String[]{ "extends", "implements", "inherit", "super" }, new int[]{0}, false);
                                addQuestion(conn, quizId, "Select valid access modifiers.", new String[]{ "public", "friendly", "private", "secret" }, new int[]{0,2}, true);
                            }
                        }
                    }
                    conn.commit();
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                } finally {
                    conn.setAutoCommit(true);
                }
            }
        }
    }

    private static void addQuestion(Connection conn, int quizId, String text, String[] options, int[] correctIndexes, boolean multiple) throws SQLException {
        try (PreparedStatement q = conn.prepareStatement("INSERT INTO questions(quiz_id, text, multiple_correct) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            q.setInt(1, quizId);
            q.setString(2, text);
            q.setInt(3, multiple ? 1 : 0);
            q.executeUpdate();
            try (ResultSet gk = q.getGeneratedKeys()) {
                if (gk.next()) {
                    int qid = gk.getInt(1);
                    for (int i=0;i<options.length;i++) {
                        try (PreparedStatement o = conn.prepareStatement("INSERT INTO options(question_id, text, is_correct) VALUES(?,?,?)")) {
                            o.setInt(1, qid);
                            o.setString(2, options[i]);
                            o.setInt(3, contains(correctIndexes, i) ? 1 : 0);
                            o.executeUpdate();
                        }
                    }
                }
            }
        }
    }

    private static boolean contains(int[] arr, int v) {
        for (int x : arr) if (x == v) return true;
        return false;
    }
}
