package com.example.LightSensitiveRadio.Player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SilencePlayer {

    private final android.media.MediaPlayer mp;
    private final ArrayList<String> silenceSounds = new ArrayList<>(Arrays.asList("zgrywus.mp3", "silence.mp3"));
    private int soundIndex;
    private Context con;

    public SilencePlayer(Context c) {
        this.mp = new android.media.MediaPlayer();
        con = c;
        soundIndex = 0;
        setupPlayer(c);
    }


    public void play(){
        if (!mp.isPlaying()){
            mp.reset();
            Log.i("Silence Player", "start");
            setupPlayer(con);
            preparePlayer();
        }

    }
    public void stop(){
        if (mp.isPlaying()){
            Log.i("Silence Player", "stop");
            mp.stop();
            soundIndex = 0;
        }
    }

    private void setupPlayer(Context c) {
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mp.start();
            }
        });
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                soundIndex = 1;
                mp.reset();
                setupPlayer(con);
                preparePlayer();
            }
        });
        setSong(c);

    }
    private void setSong(Context c){
    try {
        mp.setDataSource(c, Uri.parse("/storage/emulated/0/sfx/pzg/" + silenceSounds.get(soundIndex)));
    } catch (IOException e) {
        Log.i("Silence Player", "NOT set data");
        throw new RuntimeException(e);
    }
}
    private void preparePlayer(){
        try {
            mp.prepare();
        } catch (IOException e) {
            Log.i("Silence Player", "NOT prepared");
            throw new RuntimeException(e);
        }
    }
}