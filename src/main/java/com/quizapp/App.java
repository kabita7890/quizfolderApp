package com.quizapp;

import com.quizapp.db.Database;
import com.quizapp.ui.LoginFrame;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Database.init();
            new LoginFrame();
        });
    }
}
