package com.quizapp.ui;

import com.quizapp.dao.AttemptDao;
import com.quizapp.model.Attempt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class LeaderboardDialog extends JDialog {
    public LeaderboardDialog(JFrame owner) {
        super(owner, "Leaderboard", true);
        setLayout(new BorderLayout());
        String[] cols = {"Attempt ID", "User ID", "Quiz ID", "Score", "Total", "Taken At"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        AttemptDao dao = new AttemptDao();
        try {
            List<Attempt> list = dao.leaderboard();
            for (Attempt a : list) {
                model.addRow(new Object[]{a.id, a.userId, a.quizId, a.score, a.total, a.takenAt});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

        setSize(600, 300);
        setLocationRelativeTo(owner);
        setVisible(true);
    }
}
