package com.example.astrolingo.domain.home.history_test;

import java.io.Serializable;

public class history_test implements Serializable {
    private String test_id;
    private String certificate_name;
    private String awarded_at;
    private int listening_score;
    private int total_score;
    private int reading_score;
    private int correct_count;
    private int wrong_count;

    // Constructor
    public history_test(String test_id, String certificate_name, String awarded_at,
                       int listening_score, int total_score, int reading_score,
                       int correct_count, int wrong_count) {
        this.test_id = test_id;
        this.certificate_name = certificate_name;
        this.awarded_at = awarded_at;
        this.listening_score = listening_score;
        this.total_score = total_score;
        this.reading_score = reading_score;
        this.correct_count = correct_count;
        this.wrong_count = wrong_count;
    }

    // Getters
    public String getTest_id() {
        return test_id;
    }

    public String getCertificate_name() {
        return certificate_name;
    }

    public String getAwarded_at() {
        return awarded_at;
    }

    public int getListening_score() {
        return listening_score;
    }

    public int getTotal_score() {
        return total_score;
    }

    public int getReading_score() {
        return reading_score;
    }

    public int getCorrect_count() {
        return correct_count;
    }

    public int getWrong_count() {
        return wrong_count;
    }

    // Setters
    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    public void setCertificate_name(String certificate_name) {
        this.certificate_name = certificate_name;
    }

    public void setAwarded_at(String awarded_at) {
        this.awarded_at = awarded_at;
    }

    public void setListening_score(int listening_score) {
        this.listening_score = listening_score;
    }

    public void setTotal_score(int total_score) {
        this.total_score = total_score;
    }

    public void setReading_score(int reading_score) {
        this.reading_score = reading_score;
    }

    public void setCorrect_count(int correct_count) {
        this.correct_count = correct_count;
    }

    public void setWrong_count(int wrong_count) {
        this.wrong_count = wrong_count;
    }
}
