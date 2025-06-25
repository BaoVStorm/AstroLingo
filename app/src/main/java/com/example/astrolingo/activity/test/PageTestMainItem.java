package com.example.astrolingo.activity.test;

import java.io.Serializable;

/*
* Đây là một class Java đại diện cho một đối tượng có thể gửi giữa các Activity qua Intent
* implements Serializable nghĩa là đang cho phép class này có thể được “chuyển thành chuỗi byte”, để truyền qua Intent
* Nếu không implements Serializable, Android sẽ không biết cách truyền object đó giữa các Activity
* */
public class PageTestMainItem implements Serializable {
    private String title;
    private boolean isFullTest; //true là FullTest, false là MiniTest

    public PageTestMainItem(String title, boolean isFullTest){
        this.title = title;
        this.isFullTest = isFullTest;
    }

    public String getTitle(){
        return this.title;
    }

    public boolean isFullTest(){
        return this.isFullTest;
    }

    public int getDurationMinutesOfTest(){
        return this.isFullTest ? 120 : 60;
    }

    public int getNumberOfQuestions(){
        return this.isFullTest ? 200 : 100;
    }
}
