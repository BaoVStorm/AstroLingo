package com.example.astrolingo.Service;

import com.example.astrolingo.domain.test.nav_answer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static void calScore() {
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
//        calScore();

        return Math.min(list_answer.size() * 5 - 10, score);
//        return score;
    }

    public static int getNumberOfCorrect() {
//        if(numCorrect == -1)
//            calScore();

        return numCorrect;
    }

    public static int getNumberOfCorrect_Listening() {
//        if(numberCorrect_Listening == -1)
//            calScore();

        return numberCorrect_Listening;
    }

    public static int getNumberOfCorrect_Reading() {
//        if(numberCorrect_Reading == -1)
//            calScore();

        return numberCorrect_Reading;
    }

    public static int getNumberOfWrong() {
//        if(numWrong == -1)
//            calScore();

        return numWrong;
    }

    public static int getNumberOfQuestion() {
        return list_answer.size();
    }

    public static JSONArray getLisJSONArray() throws JSONException {
        //selected_answer
        //is_wrong

        //question_number
        //question_id
        //group_question_id
        //part_id

        JSONArray answersArray = new JSONArray();

        for(int i = 0; i < list_answer.size(); i++) {
            nav_answer navAnswer = list_answer.get(i);

            JSONObject answer = new JSONObject();
            answer.put("selected_answer", navAnswer.getCurrentChoose());
            answer.put("is_wrong", !navAnswer.checkAnswer());
            answer.put("correct_answer", navAnswer.getCorrectAnswer());
            answer.put("question_number", navAnswer.getQuestion_id());
            answer.put("question_id", navAnswer.getQuestion_id_text());
            answer.put("group_question_id", navAnswer.getGroup_question_id());
            answer.put("part_id", navAnswer.getPart());

            answersArray.put(answer);
        }

        return answersArray;
    }

}
