package com.example.LightSensitiveRadio;

import android.media.MediaPlayer;

import com.example.LightSensitiveRadio.AudioProviders.AudioPreparer;

public class SongEndListener implements android.media.MediaPlayer.OnCompletionListener{

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        AudioPreparer.prepareAudio();
    }
}