package com.example.astrolingo.domain.test;

import android.media.MediaPlayer;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.astrolingo.R;
import com.example.astrolingo.function.NumberManager;

public class AudioState {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private int position;
    private ImageView audio_pause;
    private TextView audio_endtime;

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

    public void setAudioPause(ImageView audio_pause) {
        this.audio_pause = audio_pause;
    }
    public void setAudioEndtime(TextView audio_endtime) {
        this.audio_endtime = audio_endtime;
    }
    public void updateAudioEndtime() {
        if(audio_endtime == null) {
            return;
        }

        audio_endtime.setText(NumberManager.numberToTime_minute(mediaPlayer.getDuration()));
    }
    public void changeAudioPauseStop(boolean isStop) {
        if(audio_pause == null)
            return;

        if(isStop)
            audio_pause.setImageResource(R.drawable.icon_asset_play_fill);
        else
            audio_pause.setImageResource(R.drawable.icon_asset_pause_fill);
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

    public void release() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

