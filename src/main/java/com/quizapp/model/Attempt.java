package com.quizapp.model;

public class Attempt {
    public int id;
    public int userId;
    public int quizId;
    public int score;
    public int total;
    public String takenAt;
    public Attempt(int id, int userId, int quizId, int score, int total, String takenAt) {
        this.id = id;
        this.userId = userId;
        this.quizId = quizId;
        this.score = score;
        this.total = total;
        this.takenAt = takenAt;
    }
}
