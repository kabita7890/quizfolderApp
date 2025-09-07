package com.quizapp.ui;

import com.quizapp.dao.UserDao;
import com.quizapp.model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final UserDao userDao = new UserDao();

    public LoginFrame() {
        super("Online Quiz - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0,1,8,8));
        form.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        form.add(new JLabel("Username"));
        form.add(usernameField);
        form.add(new JLabel("Password"));
        form.add(passwordField);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Create account");

        JPanel actions = new JPanel();
        actions.add(loginBtn);
        actions.add(registerBtn);

        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        loginBtn.addActionListener(e -> doLogin());
        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void doLogin() {
        String u = usernameField.getText().trim();
        String p = new String(passwordField.getPassword());
        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password");
            return;
        }
        try {
            User user = userDao.login(u, p);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Welcome, " + user.username + "!");
                dispose();
                new DashboardFrame(user);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
