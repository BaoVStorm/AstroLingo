package com.example.astrolingo.domain.test;

public class testDetail_page {
    private String title, content;
    private String type;
    private String group_question_id;
    private int part_id;

    public testDetail_page(){}

    public testDetail_page(String type_) {
        type = type_;
    }

    public void setType(String type) {
        this.type = type;
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
