package com.example.astrolingo.function;

import java.util.Locale;

public class NumberManager {

    public static String numberToTime_hour(long time_ms) {
        long seconds = time_ms / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);
    }

    public static String numberToTime_minute(long time_ms) {
        if(time_ms > 60 * 60 * 1000)
            return numberToTime_hour(time_ms);

        long seconds = time_ms / 1000;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        return String.format(Locale.getDefault(), "%02d:%02d", minutes, secs);
    }
}
