package com.example.astrolingo.activity.home.game_quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.astrolingo.R;
import com.example.astrolingo.apdapter.home.game_hangman.WordAttackConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.google.android.flexbox.FlexboxLayout;


public class QuizGameActivity extends AppCompatActivity {

    private int presCounter = 0;
    private int maxPresCounter;
    private String[] keys;
    private String textAnswer;

    Animation smallbigforth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_wordattack1);

        // Load animation
        smallbigforth = AnimationUtils.loadAnimation(this, R.anim.smallbigforth);

        // Random 1 từ trả lời và tạo keys tương ứng
        textAnswer = WordAttackConstants.getRandomWord().toUpperCase();
        keys = generateKeys(textAnswer);
        maxPresCounter = textAnswer.length();

        // Thêm các chữ cái
        FlexboxLayout layoutParent = findViewById(R.id.letterContainer);
        EditText editText = findViewById(R.id.editText);
        for (String key : keys) {
            addKeyView(layoutParent, key, editText);
        }

        // Gán header
        ImageView backButton = findViewById(R.id.backIcon);
        TextView headerTitle = findViewById(R.id.header_title);
        headerTitle.setText("Word attack");
        backButton.setOnClickListener(v -> finish());
    }

    private String[] generateKeys(String answer) {
        List<Character> keyList = new ArrayList<>();

        for (char c : answer.toCharArray()) {
            if (!keyList.contains(c)) {
                keyList.add(c);
            }
        }

        Random random = new Random();
        while (keyList.size() < 10) {
            char randomChar = (char) (random.nextInt(26) + 'A');
            if (!keyList.contains(randomChar)) {
                keyList.add(randomChar);
            }
        }

        Collections.shuffle(keyList);
        String[] keysArray = new String[keyList.size()];
        for (int i = 0; i < keyList.size(); i++) {
            keysArray[i] = String.valueOf(keyList.get(i));
        }

        return keysArray;
    }

    private void addKeyView(FlexboxLayout parent, String text, EditText inputField) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.rightMargin = 30;

        TextView keyView = new TextView(this);
        keyView.setLayoutParams(params);
        keyView.setBackground(getResources().getDrawable(R.drawable.bgpink));
        keyView.setTextColor(getResources().getColor(R.color.main_color_purple));
        keyView.setGravity(Gravity.CENTER);
        keyView.setText(text);
        keyView.setTextSize(32);
        keyView.setClickable(true);
        keyView.setFocusable(true);

        keyView.setOnClickListener(v -> {
            if (presCounter < maxPresCounter) {
                if (presCounter == 0) inputField.setText("");
                inputField.setText(inputField.getText().toString() + text);
                keyView.startAnimation(smallbigforth);
                keyView.animate().alpha(0).setDuration(300);
                presCounter++;
                if (presCounter == maxPresCounter) validateAnswer();
            }
        });

        parent.addView(keyView);
    }

    private void validateAnswer() {
        EditText inputField = findViewById(R.id.editText);
        FlexboxLayout layoutParent = findViewById(R.id.letterContainer);
        String input = inputField.getText().toString();

        presCounter = 0;

        // Kiểm tra đúng sai
        if (input.equalsIgnoreCase(textAnswer)) {
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
        }

        // Dù đúng hay sai đều chuyển sang BossAct
        Intent intent = new Intent(this, BossAct.class);
        startActivity(intent);

        // Reset input
        inputField.setText("");

        // Tạo câu mới
        textAnswer = WordAttackConstants.getRandomWord().toUpperCase();
        keys = generateKeys(textAnswer);
        maxPresCounter = textAnswer.length();

        layoutParent.removeAllViews();
        for (String key : keys) {
            addKeyView(layoutParent, key, inputField);
        }
    }

}
