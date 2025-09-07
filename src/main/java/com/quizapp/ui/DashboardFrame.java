package com.quizapp.ui;

import com.quizapp.dao.AttemptDao;
import com.quizapp.dao.QuizDao;
import com.quizapp.model.Quiz;
import com.quizapp.model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DashboardFrame extends JFrame {
    private final User user;
    private final QuizDao quizDao = new QuizDao();
    private final AttemptDao attemptDao = new AttemptDao();
    private final DefaultListModel<Quiz> quizListModel = new DefaultListModel<>();
    private final JList<Quiz> quizList = new JList<>(quizListModel);

    public DashboardFrame(User user) {
        super("Dashboard - " + user.username);
        this.user = user;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Available Quizzes"), BorderLayout.WEST);

        JButton refreshBtn = new JButton("Refresh");
        JButton historyBtn = new JButton("My Attempts");
        JButton leaderboardBtn = new JButton("Leaderboard");
        JPanel actions = new JPanel();
        actions.add(refreshBtn);
        actions.add(historyBtn);
        actions.add(leaderboardBtn);
        top.add(actions, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        quizList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(quizList), BorderLayout.CENTER);

        JButton startBtn = new JButton("Start Quiz");
        add(startBtn, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadQuizzes());
        historyBtn.addActionListener(e -> new HistoryDialog(this, user.id));
        leaderboardBtn.addActionListener(e -> new LeaderboardDialog(this));
        startBtn.addActionListener(e -> {
            Quiz q = quizList.getSelectedValue();
            if (q == null) {
                JOptionPane.showMessageDialog(this, "Select a quiz");
                return;
            }
            new QuizPlayerFrame(this, user, q);
        });

        loadQuizzes();
        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadQuizzes() {
        quizListModel.clear();
        try {
            List<Quiz> quizzes = quizDao.findAll();
            for (Quiz q : quizzes) quizListModel.addElement(q);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
