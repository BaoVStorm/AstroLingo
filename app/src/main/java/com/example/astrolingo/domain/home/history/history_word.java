package com.example.astrolingo.domain.home.history;

public class history_word {
    boolean isTranslateEnglish = true;
    String word = "", meaning = "";
    String date = "";

    history_word() {
    }

    public history_word(boolean isTranslateEnglish, String word, String meaning, String date) {
        this.isTranslateEnglish = isTranslateEnglish;
        this.word = word;
        this.meaning = meaning;
        this.date = date;
    }

    public String getDate() {
        return date;
    }
    public String getMeaning() {
        return meaning;
    }
    public String getWord() {
        return word;
    }
    public boolean isTranslateEnglish() {
        return isTranslateEnglish;
    }
}
