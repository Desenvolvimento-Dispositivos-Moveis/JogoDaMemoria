package com.example.jogodamemoria.model;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.jogodamemoria.R;

public class SoundManager {

    private SoundPool soundPool;
    private MediaPlayer musicPlayer;
    private Context context;
    private int soundAcertoId;
    private int soundErroId;
    private int soundVirarId;

    public SoundManager(Context context) {
        this.context = context;

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(3)
                .setAudioAttributes(audioAttributes)
                .build();

        soundAcertoId = soundPool.load(context, R.raw.sound_acerto, 1);
        soundErroId = soundPool.load(context, R.raw.sound_erro, 1);
        soundVirarId = soundPool.load(context, R.raw.carta_virando, 1);
    }

    public void playSoundAcerto() {
        soundPool.play(soundAcertoId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playSoundErro() {
        soundPool.play(soundErroId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playSoundVirar() {
        soundPool.play(soundVirarId, 1.0f, 1.0f, 1, 0, 1.0f);
    }


    private void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.release();
            musicPlayer = null;
        }
    }

    public void playMenuMusic() {
        stopMusic();
        musicPlayer = MediaPlayer.create(context, R.raw.song_tela_inicial);
        musicPlayer.setLooping(true);
        musicPlayer.start();
    }

    public void playTriunfoMusic() {
        stopMusic();
        musicPlayer = MediaPlayer.create(context, R.raw.song_triunfo);
        musicPlayer.setLooping(false);
        musicPlayer.start();
    }


    public void pauseMusic() {
        if (musicPlayer != null && musicPlayer.isPlaying()) {
            musicPlayer.pause();
        }
    }
    public void resumeMusic() {
        if (musicPlayer != null && !musicPlayer.isPlaying()) {
            if (musicPlayer.isLooping()) {
                musicPlayer.start();
            }
        }
    }
    public void release() {
        stopMusic();
        soundPool.release();
    }
}