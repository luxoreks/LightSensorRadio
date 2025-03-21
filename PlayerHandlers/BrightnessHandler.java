package com.example.LightSensitiveRadio.PlayerHandlers;

import android.util.Log;

import com.example.LightSensitiveRadio.AppContextProvider;
import com.example.LightSensitiveRadio.BrightnessConditionsForPlayer;
import com.example.LightSensitiveRadio.BrightnessOptions;
import com.example.LightSensitiveRadio.Player.MediaPlayer;
import com.example.LightSensitiveRadio.AudioProviders.NextSongPreparer;
import com.example.LightSensitiveRadio.Player.SilencePlayer;

public class BrightnessHandler implements PlayerHandler{

    private static final MediaPlayer player = MediaPlayer.getInstance();
    private static String playerPartOfDay = "day";
    private final SilencePlayer silencePlayer = new SilencePlayer(AppContextProvider.getInstance().getAppContext());
    private static final BrightnessOptions brightnessOptions = BrightnessOptions.getInstance();
    private static final BrightnessConditionsForPlayer conditionsForPlayer = BrightnessConditionsForPlayer.getInstance();

    @Override
    public void updatePlayer() {
        if (player.isPlaying() && conditionsForPlayer.shouldPlayerStopped()){
            Log.i("player manager", "Player stopped");
            player.pause();
            brightnessOptions.setMaxBrightness(brightnessOptions.getCurrentBrightness());
        }
        else if (!player.isPlaying() && conditionsForPlayer.shouldPlayerPlay()){
            Log.i("player manager", "Player started");
            silencePlayer.stop();
            Log.i("MediaPlayerManager", "Part od fay changed: " + partOfDayChanged());
            if (partOfDayChanged()){
                playerPartOfDay = NextSongPreparer.isDay? "day" : "night";
                NextSongPreparer.prepareNextSong();
            }
            else{
                player.play();
            }
        }
        if ((brightnessOptions.getCurrentBrightness() <= brightnessOptions.getBrightnessThreshold()) && !player.isPlaying()){
            player.pause();
            silencePlayer.play();
        }
    }

    private boolean partOfDayChanged(){
        String currentPartOfDay = NextSongPreparer.isDay? "day" : "night";
        Log.i("MediaPlayerManager", "Player day: " + playerPartOfDay);
        return !playerPartOfDay.equals(currentPartOfDay);
    }
}