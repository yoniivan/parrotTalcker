package com.example.parrottalker.utils;
import android.media.AudioManager;
import android.media.MediaPlayer;


import java.io.IOException;


public class MusicPlayer{

    private MediaPlayer mPlayer;
    private boolean isPlaying;
    private String path;
    private int times;
    private int playCounter;
    static MusicPlayer musicPlayer;

    public static MusicPlayer get(){
        return  musicPlayer == null ? (musicPlayer = new MusicPlayer()) : musicPlayer;
    }

    private MusicPlayer() {
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void play(String path,int times) {
        this.path = path;
        if(isPlaying){
            stopAll();
            return;
        }
        startPlaying( times);
    }


    private void startPlaying(int times) {
        try {
            this.playCounter = 0;
            this.times = times;
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            isPlaying = true;
            mPlayer.start();
            mPlayer.setOnCompletionListener(mp -> {
                playCounter++;
                if(playCounter >= times){
                    stopAll();
                }else {
                    mp.start();
                }
            });
        } catch (IOException e) {

        }
    }

    public void stopAll() {
        if(isPlaying) {
            if (mPlayer != null) {
                mPlayer.stop();
                mPlayer.reset();
            }
            isPlaying = false;
        }
    }

}
