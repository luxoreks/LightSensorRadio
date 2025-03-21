package com.example.LightSensitiveRadio.Player;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.LightSensitiveRadio.AppContextProvider;
import com.example.LightSensitiveRadio.SongEndListener;

import java.io.IOException;

public class MediaPlayer {

    private static MediaPlayer instance;
    private android.media.MediaPlayer player;
    private boolean isPlaying;
    private final AppContextProvider contextHolder = AppContextProvider.getInstance();

    private MediaPlayer(){
        this.player = new android.media.MediaPlayer();
        this.player.setOnCompletionListener(new SongEndListener());
        this.isPlaying = false;
    }

    public static MediaPlayer getInstance(){
        if (instance == null){
            instance = new MediaPlayer();
        }
        return instance;
    }

    public void setSong(String url){
        setSongData(contextHolder.getAppContext(), url);
        preparePlayer();
    }

    public void play(){
        if (!player.isPlaying()){
            player.start();
            isPlaying = true;
            Log.i("MediaPlayer", "start");
        }
    }

    public void disablePlayer(){
        player.reset();
        player.release();
    }

    public void pause(){
        if (player.isPlaying()){
            player.pause();
            isPlaying = false;
            Log.i("MediaPlayer", "stop");
        }
    }

    public void reset(){
        player.reset();
    }

    public boolean isPlaying(){
        return isPlaying;
    }

    private void setSongData(Context c, String url){
        try {
            player.setDataSource(c, Uri.parse(url));
        } catch (IOException e) {
            Log.i("main App", "NOT set data");
            throw new RuntimeException(e);
        }
    }
    private void preparePlayer(){
        try {
            player.prepare();
        } catch (IOException e) {
            Log.i("main App", "NOT prepared");
            throw new RuntimeException(e);
        }
    }

    public void release() {
        if(player.isPlaying()){
            player.pause();
        }
        player.reset();
        player.release();
    }
}