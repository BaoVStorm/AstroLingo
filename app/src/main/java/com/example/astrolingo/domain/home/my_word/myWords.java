package com.example.astrolingo.domain.home.my_word;

import com.example.astrolingo.domain.home.history.history_word;
import com.example.astrolingo.domain.home.learn_word.vocabulary;

public class myWords {
    private vocabulary vocabulary;
    private history_word word;
    private String type;
    private String id;

    public myWords() {
    }

    public void setVocabulary(vocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }
    public void setWord(history_word word) {
        this.word = word;
    }
//    public void setTranslate(history_word translate) {
//        this.translate = translate;
//    }
//    public void setCreated(history_word created) {
//        this.created = created;
//    }

    public void setId(String id) {
        this.id = id;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getId() {
        return id;
    }
    public String getType() {
        return type;
    }
    public vocabulary getVocabulary() {
        return vocabulary;
    }
    public history_word getWord() {
        return word;
    }
    public int getTypeNumber() {
        if(type.equals("vocabulary"))
            return 0;
        return 1;
    }
//    public history_word getTranslate() {
//        return translate;
//    }
//    public history_word getCreated() {
//        return created;
//    }

}
