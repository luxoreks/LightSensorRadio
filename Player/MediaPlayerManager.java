package com.example.LightSensitiveRadio.Player;

import com.example.LightSensitiveRadio.PlayerHandlers.BrightnessHandler;
import com.example.LightSensitiveRadio.PlayerHandlers.PlayerHandler;

public class MediaPlayerManager {

    private static final double DEFAULT_MAX_BRIGHTNESS = 40;
    private PlayerHandler playerHandler;
    private static MediaPlayerManager instance;

    private MediaPlayerManager(){
        playerHandler = new BrightnessHandler();
    }

    public static MediaPlayerManager getInstance() {
        if (instance == null){
            instance = new MediaPlayerManager();
        }
        return instance;
    }

    public void updateMediaPlayerState(){
        playerHandler.updatePlayer();
    }

    public void releasePlayer(){
        MediaPlayer.getInstance().release();
    }

    public void setPlayerStateHandler(PlayerHandler handler){
        playerHandler = handler;
    }
}