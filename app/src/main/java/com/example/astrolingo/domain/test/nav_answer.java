package com.example.astrolingo.domain.test;

public class nav_answer {
    private int count_ans = 0;
    private int currentChoose = 0;
    private int correctAnswer = 0;
    private int question_id, part;

    public nav_answer() {
        this.count_ans = 0;
        this.currentChoose = 0;
    }

    public nav_answer(int currentChoose, int count_ans) {
        this.count_ans = count_ans;
        this.currentChoose = currentChoose;
    }

    public void setCurrentChoose(int currentChoose) {
        this.currentChoose = currentChoose;
    }
    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    public void setInfo(int part, int question_id) {
        this.part = part;
        this.question_id = question_id;
    }
    public int getCorrectAnswer() {
        return correctAnswer;
    }
    public int getCountAns() {
        return count_ans;
    }
    public int getPart() {
        return part;
    }
    public int getQuestion_id() {
        return question_id;
    }
    public int getCurrentChoose() {
        return currentChoose;
    }

}
