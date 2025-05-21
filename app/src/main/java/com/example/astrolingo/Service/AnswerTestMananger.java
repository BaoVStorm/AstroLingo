package com.example.astrolingo.Service;

import com.example.astrolingo.domain.test.nav_answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnswerTestMananger {
//    public static HashMap<Integer, nav_answer> map_answer = new HashMap<>();
    public static ArrayList<nav_answer> list_answer = new ArrayList<>();

    public AnswerTestMananger() {
    }

    public static nav_answer getAnswer(int position) {
//        return map_answer.get(position);
        return list_answer.get(position);
    }

    public static void addAnswer(int position, nav_answer navAnswer) {
//        map_answer.put(position, navAnswer);
        list_answer.add(navAnswer);
    }

    public static void releaseAll() {
//        map_answer.clear();
        list_answer.clear();
    }
}
