package com.quizapp.ui;

import com.quizapp.dao.UserDao;
import com.quizapp.model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RegisterFrame extends JFrame {
    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JPasswordField confirmField = new JPasswordField(15);
    private final UserDao userDao = new UserDao();

    public RegisterFrame() {
        super("Online Quiz - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0,1,8,8));
        form.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        form.add(new JLabel("Username"));
        form.add(usernameField);
        form.add(new JLabel("Password"));
        form.add(passwordField);
        form.add(new JLabel("Confirm Password"));
        form.add(confirmField);

        JButton createBtn = new JButton("Create");
        JButton backBtn = new JButton("Back to login");

        JPanel actions = new JPanel();
        actions.add(createBtn);
        actions.add(backBtn);

        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        createBtn.addActionListener(e -> doRegister());
        backBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void doRegister() {
        String u = usernameField.getText().trim();
        String p = new String(passwordField.getPassword());
        String c = new String(confirmField.getPassword());
        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }
        if (!p.equals(c)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match");
            return;
        }
        try {
            User user = userDao.register(u, p);
            JOptionPane.showMessageDialog(this, "Account created! Please login.");
            dispose();
            new LoginFrame();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
