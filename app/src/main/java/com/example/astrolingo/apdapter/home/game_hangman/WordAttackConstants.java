package com.example.astrolingo.apdapter.home.game_hangman;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WordAttackConstants {

    public static final List<String> words = Arrays.asList(
            "BIRD",
            "FISH",
            "TREE",
            "BOOK",
            "STAR",
            "MOON",
            "CLOUD",
            "HOUSE",
            "APPLE",
            "WATER"
    );

    public static String getRandomWord() {
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }
}

