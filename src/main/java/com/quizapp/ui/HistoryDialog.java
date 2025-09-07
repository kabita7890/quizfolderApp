package com.quizapp.ui;

import com.quizapp.dao.AttemptDao;
import com.quizapp.model.Attempt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class HistoryDialog extends JDialog {
    public HistoryDialog(JFrame owner, int userId) {
        super(owner, "My Attempts", true);
        setLayout(new BorderLayout());
        String[] cols = {"ID", "Quiz ID", "Score", "Total", "Taken At"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        AttemptDao dao = new AttemptDao();
        try {
            List<Attempt> attempts = dao.historyForUser(userId);
            for (Attempt a : attempts) {
                model.addRow(new Object[]{a.id, a.quizId, a.score, a.total, a.takenAt});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

        setSize(500, 300);
        setLocationRelativeTo(owner);
        setVisible(true);
    }
}
