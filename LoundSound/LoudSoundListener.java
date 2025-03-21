package com.example.LightSensitiveRadio.LoundSound;

import com.example.LightSensitiveRadio.AudioProviders.NextSongPreparer;

public class LoudSoundListener {

    public void checkSoundValues(short[] currentSoundVal){
        boolean areSoundsLoud = checkLoudness(currentSoundVal);
        if (areSoundsLoud){
            NextSongPreparer.prepareNextSong();
        }
    }
    private boolean checkLoudness(short[] soundVal){
        boolean isFirstSoundLoud = soundVal[0] > SoundThresholdHolder.loudnessThreshold;
        boolean isSecondSoundOneLoud = soundVal[1] > SoundThresholdHolder.loudnessThreshold;

        return isFirstSoundLoud && isSecondSoundOneLoud;
    }
}