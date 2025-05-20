package com.example.astrolingo.domain.test;

import java.util.ArrayList;

public class testDetail_page {
    private String title, content;
    private String type;
    private int first_question;
    private int question_count;
    private String audioUrl;
    private String group_question_id;
    private ArrayList<String> list_image_url;
    private int part_id;

    public testDetail_page(){}

    public testDetail_page(String type_) {
        type = type_;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setListImageUrl(ArrayList<String> list_image_url) {
        this.list_image_url = list_image_url;
    }
    public ArrayList<String> getListImageUrl() {
        return list_image_url;
    }
    public void setPartHeader(int first_question, int question_count) {
        this.first_question = first_question;
        this.question_count = question_count;
    }
    private String editPartNumber(String s) {
        int len = s.length();
        for(int i = 1; i <= 3 - len; i++) {
            s = "0" + s;
        }

        return s;
    }
    public String getPartHeader() {
        String partHeader = editPartNumber(first_question + "");

        if(question_count > 1)
            partHeader += " - " + editPartNumber((first_question + question_count - 1) + "");

        return partHeader;
    }
    public int getQuestionCount() {
        return question_count;
    }
    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
    public String getAudioUrl() {
        return audioUrl;
    }
    public void setGroupQuestionId(String group_question_id) {
        this.group_question_id = group_question_id;
    }
    public String getGroupQuestionId() {
        return this.group_question_id;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }
    public void setTitle(String title_) {
        title = title_;
    }
    public String getTitle() {
        return title;
    }

    public void setPart(int part_) {
        part_id = part_;
    }
    public int getPart() {
        return part_id;
    }

    public int getType() {
        if(type.equals("start_part"))
            return 0;
        if(type.equals("listening"))
            return 1;
        if(type.equals("reading"))
            return 2;

        return 0;
    }
}
