package com.example.astrolingo.Service;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.example.astrolingo.R;
import com.example.astrolingo.domain.test.AudioState;
import com.example.astrolingo.function.AudioListener;
import com.example.astrolingo.function.NumberManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;


public class AudioManager {
    private static boolean isPlaying = false;
    private static MediaPlayer mediaPlayer;
    private static Thread downloadThread;

    public static void setAudio(Context context, String urlAudio, AudioListener audioListener) {
        release();

        downloadThread = new Thread(() -> {
            try {
                // Bước 1: Tải về file tạm
                URL url = new URL(urlAudio);
                URLConnection connection = url.openConnection();
                connection.connect();

                InputStream input = new BufferedInputStream(connection.getInputStream());
                File tempFile = File.createTempFile("temp_audio", ".mp3", context.getCacheDir());
                FileOutputStream output = new FileOutputStream(tempFile);

                byte[] buffer = new byte[4096];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                output.flush();
                output.close();
                input.close();

                // Bước 2: Tạo MediaPlayer từ file tạm
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(tempFile.getAbsolutePath());

                mediaPlayer.setOnPreparedListener(mp -> {
                    isPlaying = true;
                    mp.start();
                    if (audioListener != null) {
                        new Handler(Looper.getMainLooper()).post(audioListener::onPrepared);
                    }
                });

                mediaPlayer.setOnCompletionListener(mp -> {
                    release();
                    if (audioListener != null) {
                        new Handler(Looper.getMainLooper()).post(audioListener::onCompletion);
                    }
                    isPlaying = false;
                });

                mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                    release();
                    isPlaying = false;
                    return true;
                });

                mediaPlayer.prepare();

            } catch (Exception e) {
                e.printStackTrace();
                if (audioListener != null) {
                    new Handler(Looper.getMainLooper()).post(audioListener::onError);
                }
            }
        });

        downloadThread.start();
    }

    public static void release() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            } catch (IllegalStateException ignored) {
            }

            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (downloadThread != null && downloadThread.isAlive()) {
            downloadThread.interrupt();
            downloadThread = null;
        }

        isPlaying = false;
    }

    public static boolean isPlaying() {
        return isPlaying;
    }
}