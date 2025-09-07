package com.quizapp.model;

public class Option {
    public int id;
    public int questionId;
    public String text;
    public boolean isCorrect;
    public Option(int id, int questionId, String text, boolean isCorrect) {
        this.id = id;
        this.questionId = questionId;
        this.text = text;
        this.isCorrect = isCorrect;
    }
}
