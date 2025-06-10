package com.example.astrolingo.activity.home.game_quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.astrolingo.R;
import com.example.astrolingo.activity.MainActivity;
import com.example.astrolingo.activity.home.game_hangman.GameActivity;

public class StartGameWordAttack extends AppCompatActivity {

    TextView textScreen, textQuestion, textTitle, textBtn;
    ImageView bigboss;
    Animation smalltobig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_game_wordattack_start);

        smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);

        textQuestion = findViewById(R.id.textQuestion);
        textScreen = findViewById(R.id.textScreen);
        textTitle = findViewById(R.id.textTitle);
        textBtn = findViewById(R.id.textBtn);

        bigboss = findViewById(R.id.bigboss);
        bigboss.startAnimation(smalltobig);

        // Sự kiện khi nhấn vào textTitle (hoặc là Button): Btn Continue
        textTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.astrolingo.activity.home.game_quiz.StartGameWordAttack.this, QuizGameActivity.class);
                startActivity(intent);
                finish(); // kết thúc BossAct để tránh stack chồng
            }
        });

        // Sự kiện Back to home
        textBtn.setOnClickListener(v -> {
            Intent intent = new Intent(com.example.astrolingo.activity.home.game_quiz.StartGameWordAttack.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // đóng BossAct để tránh quay lại bằng nút back
        });

    }
}



