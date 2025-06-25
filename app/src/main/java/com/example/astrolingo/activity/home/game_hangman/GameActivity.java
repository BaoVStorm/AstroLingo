package com.example.astrolingo.activity.home.game_hangman;

import com.example.astrolingo.function.GameManager;
import com.example.astrolingo.domain.home.game_hangman.GameState;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.astrolingo.R;

public class GameActivity extends AppCompatActivity {

    private GameManager gameManager = new GameManager();

    private TextView wordTextView;
    private TextView lettersUsedTextView;
    private ImageView imageView;
    private TextView gameLostTextView;
    private TextView gameWonTextView;
    private Button newGameButton;
    private ConstraintLayout lettersLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_hangman);

        imageView = findViewById(R.id.imageView);
        wordTextView = findViewById(R.id.wordTextView);
        lettersUsedTextView = findViewById(R.id.lettersUsedTextView);
        gameLostTextView = findViewById(R.id.gameLostTextView);
        gameWonTextView = findViewById(R.id.gameWonTextView);
        newGameButton = findViewById(R.id.newGameButton);
        lettersLayout = findViewById(R.id.lettersLayout);

        newGameButton.setOnClickListener(v -> startNewGame());

        GameState gameState = gameManager.startNewGame();
        updateUI(gameState);

        for (int i = 0; i < lettersLayout.getChildCount(); i++) {
            View letterView = lettersLayout.getChildAt(i);
            if (letterView instanceof TextView) {
                letterView.setOnClickListener(v -> {
                    char letter = ((TextView) v).getText().charAt(0);
                    GameState state = gameManager.play(letter);
                    updateUI(state);
                    v.setVisibility(View.GONE);
                });
            }
        }

        // edit header
        ImageView backButton = findViewById(R.id.backIcon);
        TextView headerTitle = findViewById(R.id.header_title);
        headerTitle.setText("Hangman Game");
        backButton.setOnClickListener(v -> {
            finish(); // Đóng activity và quay về màn hình trước đó
        });
    }

    private void updateUI(GameState gameState) {
        if (gameState instanceof GameState.Lost) {
            showGameLost(((GameState.Lost) gameState).getWordToGuess());
        } else if (gameState instanceof GameState.Won) {
            showGameWon(((GameState.Won) gameState).getWordToGuess());
        } else if (gameState instanceof GameState.Running) {
            GameState.Running running = (GameState.Running) gameState;
            wordTextView.setText(running.getUnderscoreWord());
            lettersUsedTextView.setText("Letters used: " + running.getLettersUsed());
            imageView.setImageDrawable(ContextCompat.getDrawable(this, running.getDrawable()));
        }
    }

    private void showGameLost(String wordToGuess) {
        wordTextView.setText(wordToGuess);
        gameLostTextView.setVisibility(View.VISIBLE);
        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.game7));
        lettersLayout.setVisibility(View.GONE);
    }

    private void showGameWon(String wordToGuess) {
        wordTextView.setText(wordToGuess);
        gameWonTextView.setVisibility(View.VISIBLE);
        lettersLayout.setVisibility(View.GONE);
    }

    private void startNewGame() {
        gameLostTextView.setVisibility(View.GONE);
        gameWonTextView.setVisibility(View.GONE);
        GameState gameState = gameManager.startNewGame();
        lettersLayout.setVisibility(View.VISIBLE);

        for (int i = 0; i < lettersLayout.getChildCount(); i++) {
            View letterView = lettersLayout.getChildAt(i);
            letterView.setVisibility(View.VISIBLE);
        }

        updateUI(gameState);
    }
}
