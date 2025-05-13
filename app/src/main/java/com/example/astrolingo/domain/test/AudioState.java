package com.example.astrolingo.domain.test;

import android.media.MediaPlayer;

public class AudioState {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private int position;

    public AudioState(MediaPlayer mediaPlayer, int position) {
        this.mediaPlayer = mediaPlayer;
        this.position = position;
    }

    public AudioState(MediaPlayer mediaPlayer, int position, boolean isPlaying) {
        this.mediaPlayer = mediaPlayer;
        this.isPlaying = isPlaying;
        this.position = position;
    }

    public AudioState() {
        mediaPlayer = null;

    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    public int getPosition() {
        return position;
    }
}

