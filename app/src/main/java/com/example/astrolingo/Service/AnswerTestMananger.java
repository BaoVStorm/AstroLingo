package com.example.astrolingo.Service;

import com.example.astrolingo.domain.test.nav_answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnswerTestMananger {
//    public static HashMap<Integer, nav_answer> map_answer = new HashMap<>();
    public static ArrayList<nav_answer> list_answer = new ArrayList<>();
    private static int numCorrect = -1, numberCorrect_Listening = -1, numberCorrect_Reading = -1;
    private static int numWrong = -1;
    private static int score = - 1;

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

    private static void calScore() {
        numCorrect = numWrong = 0;
        numberCorrect_Reading = numberCorrect_Listening = 0;
        score = 0;

        for(int i = 0; i < list_answer.size(); i++) {
            if(list_answer.get(i).checkAnswer()) {
                numCorrect++;
                score += 5;

                if(list_answer.get(i).getPart() <= 4)
                    numberCorrect_Listening++;
                else
                    numberCorrect_Reading++;

            } else {
                numWrong++;
            }
        }
    }

    public static int getScore() {
        if(score == -1)
            calScore();

        return Math.min(list_answer.size() * 5 - 10, score);
    }

    public static int getNumberOfCorrect() {
        if(numCorrect == -1)
            calScore();

        return numCorrect;
    }

    public static int getNumberOfCorrect_Listening() {
        if(numberCorrect_Listening == -1)
            calScore();

        return numberCorrect_Listening;
    }

    public static int getNumberOfCorrect_Reading() {
        if(numberCorrect_Reading == -1)
            calScore();

        return numberCorrect_Reading;
    }

    public static int getNumberOfWrong() {
        if(numWrong == -1)
            calScore();

        return numWrong;
    }

    public static int getNumberOfQuestion() {
        return list_answer.size();
    }

}
