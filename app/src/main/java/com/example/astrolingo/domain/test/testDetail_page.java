package com.example.astrolingo.domain.test;

public class testDetail_page {
    private String title, content;
    private String type;
    private int part_id;

    public testDetail_page(String type_) {
        type = type_;
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
        if(type.equals("part1"))
            return 1;
        if(type.equals("part2"))
            return 2;

        return 0;
    }
}
