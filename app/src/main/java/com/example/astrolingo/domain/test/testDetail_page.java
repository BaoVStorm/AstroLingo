package com.example.astrolingo.domain.test;

public class testDetail_page {
    private String title;
    private String type;

    public testDetail_page(String type_) {
        title = "asdasd";
        type = type_;
    }

    public String getTitle() {
        return title;
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
