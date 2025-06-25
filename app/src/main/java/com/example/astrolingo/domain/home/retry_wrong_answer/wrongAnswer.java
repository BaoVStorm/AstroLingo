package com.example.astrolingo.domain.home.retry_wrong_answer;

public class wrongAnswer {
    private int selected_answer, correct_answer;
    private boolean is_wrong;
    private String answered_at;
    private int test_id;
    private int part_id;
    private String group_question_id;
    private String question_id, test_title;
    private int question_number;

    // Constructors
    public wrongAnswer() {}

    public wrongAnswer(int selected_answer, boolean is_wrong, String answered_at,
                      int test_id, int part_id, String group_question_id,
                      String question_id, int question_number) {
        this.selected_answer = selected_answer;
        this.is_wrong = is_wrong;
        this.answered_at = answered_at;
        this.test_id = test_id;
        this.part_id = part_id;
        this.group_question_id = group_question_id;
        this.question_id = question_id;
        this.question_number = question_number;
    }

    // Getters and Setters
    public int getCorrect_answer() {
        return correct_answer;
    }
    public void setCorrect_answer(int correct_answer) {
        this.correct_answer = correct_answer;
    }
    public int getSelected_answer() { return selected_answer; }
    public void setSelected_answer(int selected_answer) { this.selected_answer = selected_answer; }

    public String getTest_title() { return test_title; }
    public void setTest_title(String test_title) { this.test_title = test_title; }

    public boolean isIs_wrong() { return is_wrong; }
    public void setIs_wrong(boolean is_wrong) { this.is_wrong = is_wrong; }

    public String getAnswered_at() { return answered_at; }
    public void setAnswered_at(String answered_at) { this.answered_at = answered_at; }

    public int getTest_id() { return test_id; }
    public void setTest_id(int test_id) { this.test_id = test_id; }

    public int getPart_id() { return part_id; }
    public void setPart_id(int part_id) { this.part_id = part_id; }

    public String getGroup_question_id() { return group_question_id; }
    public void setGroup_question_id(String group_question_id) { this.group_question_id = group_question_id; }

    public String getQuestion_id() { return question_id; }
    public void setQuestion_id(String question_id) { this.question_id = question_id; }

    public int getQuestion_number() { return question_number; }
    public void setQuestion_number(int question_number) { this.question_number = question_number; }
}
