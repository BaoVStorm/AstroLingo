package com.example.astrolingo.Service;

import com.example.astrolingo.domain.test.AudioState;

import java.util.HashMap;
import java.util.Map;

public class AudioTestManager {
    public static Map<Integer, AudioState> map_audio = new HashMap<>();

    public AudioTestManager() {
    }

    public static void createOrResetAudio() {
        releaseAll();
        map_audio = new HashMap<>();
    }

    public static AudioState getAudio(int position) {
        return map_audio.get(position);
    }

    public static void addAudio(int position, AudioState audioState) {
        map_audio.put(position, audioState);
    }

    public static void releaseAll() {
        for (AudioState audioState : map_audio.values()) {
            if (audioState != null) {
                audioState.release();
            }
        }
        map_audio.clear(); // Xóa map để giải phóng tham chiếu
    }
}
