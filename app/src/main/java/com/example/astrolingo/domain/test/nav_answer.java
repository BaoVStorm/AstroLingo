package com.example.astrolingo.domain.test;

public class nav_answer {
    private int count_ans = 0;
    private int currentChoose = 0;

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

    public int getCountAns() {
        return count_ans;
    }

    public int getCurrentChoose() {
        return currentChoose;
    }

}
