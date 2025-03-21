package com.example.LightSensitiveRadio.AudioProviders;

import android.util.Log;

public class AudioPreparer {

    public static boolean playAds = true;
    private static final String TAG = "AudioPreparer";
    private static short SONGS_BETWEEN_ADVERT = 2;
    private static short audioCounter = 0;

    public static void setSongsBetweenAdvert(short songsBetweenAdvert) {
        SONGS_BETWEEN_ADVERT = songsBetweenAdvert;
    }
    public static short getSongsBetweenAdvert() {
        return SONGS_BETWEEN_ADVERT;
    }

    public static void prepareAudio(){
        if (playAds && NextSongPreparer.isDay){
           prepareMusicOrAds();
        }
        else{
            prepareOnlyMusic();
        }
    }

    private static void prepareMusicOrAds(){
        Log.i(TAG, "prepare music or ads aktywowane");
        audioCounter++;
        Log.i(TAG, String.valueOf(audioCounter));
        Log.i(TAG, String.valueOf(SONGS_BETWEEN_ADVERT));
        if (audioCounter > SONGS_BETWEEN_ADVERT){
            AdvertisePreparer.prepareAdvert();
            audioCounter = 0;
        }
        else{
            NextSongPreparer.prepareNextSong();
        }
    }

    private static void prepareOnlyMusic(){
        NextSongPreparer.prepareNextSong();
    }
}