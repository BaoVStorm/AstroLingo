package com.example.astrolingo.domain.home.history;

import com.example.astrolingo.R;

public class history_word {
    boolean isTranslateEnglish = true;
    String word = "", meaning = "";
    String date = "";
    boolean isStar = false;

    public history_word() {
    }

    public history_word(boolean isTranslateEnglish, String word, String meaning, String date) {
        this.isTranslateEnglish = isTranslateEnglish;
        this.word = word;
        this.meaning = meaning;
        this.date = date;
    }

    public void setIsStar(boolean isStar) {
        this.isStar = isStar;
    }
    public boolean getIsStar() {
        return isStar;
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
    public int getTranslateType() {
        if (isTranslateEnglish) {
            return R.string.english;
        } else {
            return R.string.vietnamese;
        }
    }
    public boolean isTranslateEnglish() {
        return isTranslateEnglish;
    }
}
