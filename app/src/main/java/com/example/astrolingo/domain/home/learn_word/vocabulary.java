package com.example.astrolingo.domain.home.learn_word;

import java.io.Serializable;
import java.util.Objects;

public class vocabulary implements Serializable {
    private String word, type, pronunciation;
    private String meaning_vietnamese, meaning_english;
    private String example_english, example_vietnamese;
    private boolean isStar;
    private String image_url, audio_url;
    private int topic_id, level_id;
    private String vocab_id;
    public vocabulary() {
    }

    public vocabulary(String word, String type, String meaning_vietnamese, String meaning_english, String example_vietnamese, String example_english, int topic_id, int level_id) {
        this.word = word;
        this.type = type;
        this.meaning_vietnamese = meaning_vietnamese;
        this.meaning_english = meaning_english;
        this.example_vietnamese = example_vietnamese;
        this.example_english = example_english;

        this.topic_id = topic_id;
        this.level_id = level_id;
    }

    public void setVocabId(String vocab_id) {
        this.vocab_id = vocab_id;
    }
    public String getVocabId() {
        return vocab_id;
    }

    public void setIsStar(boolean isStar) {
        this.isStar = isStar;
    }
    public boolean getIsStar() {
        return isStar;
    }
    public void setPronunciation(String pronunciation) {
        if(pronunciation == null || pronunciation.isEmpty() || Objects.equals(pronunciation, ""))
            pronunciation = "/'" + word + "/";

        this.pronunciation = pronunciation;
    }
    public void setImageUrl(String image_url) {
        if(image_url == null || image_url.isEmpty())
            image_url = "";

        this.image_url = image_url;
    }
    public void setAudioUrl(String audio_url) {
        if(audio_url == null || audio_url.isEmpty())
            audio_url = "";

        this.audio_url = audio_url;
    }

    public boolean isStar() {
        return isStar;
    }
    public String getWord() {
        return word;
    }
    public String getTypeOfWord() {
        return "(" + type + ")";
    }
    public String getPronunciation() {
        return pronunciation;
    }
    public String getMeaningVietnamese() {
        return meaning_vietnamese;
    }
    public String getMeaningEnglish() {
        return meaning_english;
    }
    public String getExampleEnglish() {
        return example_english;
    }
    public String getExampleVietnamese() {
        return example_vietnamese;
    }
    public String getImageUrl() {
        return image_url;
    }
    public String getAudioUrl() {
        return audio_url;
    }
    public int getTopicId() {
        return topic_id;
    }
    public int getLevelId() {
        return level_id;
    }

}
