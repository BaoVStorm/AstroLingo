package com.example.astrolingo.domain.test;

public class question_test {
/*
    "question_id": "test1_32",
    "group_question_id": "test1_part3_1",
    "question_text": "What does the woman want to find?",
    "correct_answer": 2,
    "ans_1": "(A) Some money",
    "ans_2": "(B) A file",
    "ans_3": "(C) An office key",
    "ans_4": "(D) A check"
 */
    String question_id = "null", question_text = "null", ans_1 = "null", ans_2 = "null", ans_3 = "null", ans_4 = "null";
    int correct_answer;

    String imageUrl = "null";

    String audioUrl = "null";

    String type;

    public question_test() {};

    // set type "question"
    public question_test(String question_text, String ans_1, String ans_2, String ans_3, String ans_4, int correct_answer) {
        this.question_text = question_text;
        this.ans_1 = ans_1;
        this.ans_2 = ans_2;
        this.ans_3 = ans_3;
        this.ans_4 = ans_4;
        this.correct_answer = correct_answer;

        this.type = "question";
    }

    // set type "image"
    public question_test(String imageUrl) {
        this.imageUrl = imageUrl;

        this.type = "image";
    }

//    public question_test(String audioUrl, String type) {
//        this.audioUrl = audioUrl;
//
//        this.type = "audio";
//    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getType() {
        return type;
    }
    public void setQuestionId(String question_id) {
        this.question_id = question_id;
    }
    public String getQuestionId() {
        return question_id;
    }

    public String getQuestionText() {
        return question_text;
    }

    public String getAns1() {
        return ans_1;
    }

    public String getAns2() {
        return ans_2;
    }

    public String getAns3() {
        return ans_3;
    }

    public String getAns4() {
        return ans_4;
    }

    public int getCorrectAnswer() {
        return correct_answer;
    }

}
