package com.example.astrolingo.domain.test;

public class nav_answer {
    private int count_ans = 0;
    private int currentChoose = 0;
    private int correctAnswer = 10;
    private int question_id, part;
    private String question_id_text, group_question_id;
    private int position;

    public nav_answer() {
        this.count_ans = 0;
        this.currentChoose = 0;

    }

    public nav_answer(int currentChoose, int count_ans) {
        this.count_ans = count_ans;
        this.currentChoose = currentChoose;
    }
    public void setQuestion_id_text(String question_id_text) {
        this.question_id_text = question_id_text;
    }
    public void setGroup_question_id(String group_question_id) {
        this.group_question_id = group_question_id;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public int getPosition() {
        return position;
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
    public String getQuestion_id_text() {
        return question_id_text;
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
    public String getGroup_question_id() {
        return group_question_id;
    }
    public boolean checkAnswer() {
        if(currentChoose == correctAnswer) {
            return true;
        }
        return false;
    }

}
