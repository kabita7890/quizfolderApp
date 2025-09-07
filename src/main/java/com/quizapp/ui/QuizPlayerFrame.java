package com.quizapp.ui;

import com.quizapp.dao.AttemptDao;
import com.quizapp.dao.QuestionDao;
import com.quizapp.model.Option;
import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import com.quizapp.model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizPlayerFrame extends JDialog {
    private final User user;
    private final Quiz quiz;
    private final QuestionDao questionDao = new QuestionDao();
    private final AttemptDao attemptDao = new AttemptDao();

    private List<Question> questions = new ArrayList<>();
    private int index = 0;
    private int score = 0;

    private JLabel questionLabel = new JLabel();
    private JPanel optionsPanel = new JPanel(new GridLayout(0,1));
    private JButton nextBtn = new JButton("Submit & Next");
    private ButtonGroup singleGroup = new ButtonGroup();
    private List<AbstractButton> optionButtons = new ArrayList<>();

    public QuizPlayerFrame(JFrame owner, User user, Quiz quiz) {
        super(owner, "Quiz - " + quiz.title, true);
        this.user = user;
        this.quiz = quiz;

        setLayout(new BorderLayout());
        add(questionLabel, BorderLayout.NORTH);
        add(new JScrollPane(optionsPanel), BorderLayout.CENTER);
        add(nextBtn, BorderLayout.SOUTH);

        nextBtn.addActionListener(e -> submitAndNext());

        loadQuestions();
        renderQuestion();

        setSize(600, 400);
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private void loadQuestions() {
        try {
            this.questions = questionDao.findByQuiz(quiz.id);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load questions: " + e.getMessage());
        }
    }

    private void renderQuestion() {
        if (index >= questions.size()) {
            try {
                attemptDao.recordAttempt(user.id, quiz.id, score, questions.size());
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Failed to record attempt: " + e.getMessage());
            }
            JOptionPane.showMessageDialog(this, "Quiz Finished! Score: " + score + " / " + questions.size());
            dispose();
            return;
        }

        Question q = questions.get(index);
        questionLabel.setText("Q" + (index+1) + ": " + q.text + (q.multipleCorrect ? " (choose multiple)" : ""));
        optionsPanel.removeAll();
        optionButtons.clear();
        singleGroup = new ButtonGroup();

        try {
            List<Option> opts = questionDao.optionsForQuestion(q.id);
            for (Option o : opts) {
                AbstractButton btn;
                if (q.multipleCorrect) {
                    btn = new JCheckBox(o.text);
                    btn.putClientProperty("correct", o.isCorrect);
                } else {
                    JRadioButton r = new JRadioButton(o.text);
                    r.putClientProperty("correct", o.isCorrect);
                    singleGroup.add(r);
                    btn = r;
                }
                optionButtons.add(btn);
                optionsPanel.add(btn);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load options: " + e.getMessage());
        }

        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private void submitAndNext() {
        boolean allCorrect = true;
        boolean anySelected = false;
        for (AbstractButton b : optionButtons) {
            boolean selected = b.isSelected();
            boolean correct = Boolean.TRUE.equals(b.getClientProperty("correct"));
            if (selected) anySelected = true;
            if (selected != correct) {
                allCorrect = false;
            }
        }
        if (!anySelected) {
            JOptionPane.showMessageDialog(this, "Please select at least one option.");
            return;
        }
        if (allCorrect) {
            score++;
            JOptionPane.showMessageDialog(this, "Correct!");
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect.");
        }
        index++;
        renderQuestion();
    }
}
