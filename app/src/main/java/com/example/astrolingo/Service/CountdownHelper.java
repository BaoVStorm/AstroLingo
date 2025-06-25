package com.example.astrolingo.Service;

import android.os.CountDownTimer;

import com.example.astrolingo.function.CountdownListener;
import com.example.astrolingo.function.NumberManager;

import java.util.Locale;

public class CountdownHelper {
    private CountDownTimer countDownTimer;
    private CountdownListener listener;

    public CountdownHelper(CountdownListener listener) {
        this.listener = listener;
    }

    public void startCountdown(long millisInFuture) {
        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String timeFormatted = NumberManager.numberToTime_hour(millisUntilFinished);

                if (listener != null) {
                    listener.onTick(timeFormatted);
                }
            }

            @Override
            public void onFinish() {
                if (listener != null) {
                    listener.onTick("00:00:00");
                    listener.onFinish();
                }
            }
        };

        countDownTimer.start();
    }

    public void startCountdown_Minutes(long millisInFuture) {
        if(millisInFuture > 60 * 60 * 1000)
            millisInFuture = 60 * 60 * 1000 - 1;

        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String timeFormatted = NumberManager.numberToTime_minute(millisUntilFinished);

                if (listener != null) {
                    listener.onTick(timeFormatted);
                }
            }

            @Override
            public void onFinish() {
                if (listener != null) {
                    listener.onTick("00:00");
                    listener.onFinish();
                }
            }
        };

        countDownTimer.start();
    }

    public void stop() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
