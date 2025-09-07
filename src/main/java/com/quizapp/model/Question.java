package com.quizapp.model;

public class Question {
    public int id;
    public int quizId;
    public String text;
    public boolean multipleCorrect;
    public Question(int id, int quizId, String text, boolean multipleCorrect) {
        this.id = id;
        this.quizId = quizId;
        this.text = text;
        this.multipleCorrect = multipleCorrect;
    }
}
