package com.example.LightSensitiveRadio.AudioProviders;

import android.util.Log;

import com.example.LightSensitiveRadio.FilenamesRetriever;
import com.example.LightSensitiveRadio.Player.MediaPlayerState;
import com.example.LightSensitiveRadio.Player.MediaPlayer;
import com.example.LightSensitiveRadio.ServerAddressHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NextSongPreparer {
    private static final String TAG = "NextSongPreparer";
    public static boolean useNightMode = true;
    private static int SONGS_WITHOUT_REPETITION = 5;
    private static final MediaPlayer player = MediaPlayer.getInstance();
    private static final FilenamesRetriever filenamesRetriever = new FilenamesRetriever();
    private static List<String> usedSongs = new ArrayList<>();
    public static boolean isDay = true;


    public static void setSongsWithoutRepetition(int amount){
        SONGS_WITHOUT_REPETITION = amount;
//        if (usedSongs.size() > amount){
//            while (usedSongs.size() > amount){
//                removeLastSongFromUsed();
//            }
//        }
    }
    public static int getSongsWithoutRepetition() {
        return SONGS_WITHOUT_REPETITION;
    }

    public static void prepareNextSong(){
        String song = getSong();
        MediaPlayerState.currentSongPath = ServerAddressHolder.filePath + song;
        player.reset();
        player.setSong(MediaPlayerState.currentSongPath);
        addToUsedSongs(song);
        player.play();
    }

    private static String getSong(){

        ArrayList<String> songs = new ArrayList<>();
        if (!useNightMode){
            isDay = true;
        }
        if (isDay){
            songs = filenamesRetriever.retrieveFilenames(FilenamesRetriever.GET_SONGS_FILENAMES);
        }
        else{
            songs = filenamesRetriever.retrieveFilenames(FilenamesRetriever.GET_AMBIENT_SONGS_FILENAMES);
        }
        Random rand = new Random();
        List<String> availableSongs =(ArrayList<String>) songs.clone();
        availableSongs.removeAll(usedSongs);
        if (availableSongs.size() == 0){
            availableSongs = songs;
            usedSongs = new ArrayList<>();
        }
        int i = rand.nextInt(availableSongs.size());
        Log.d(TAG, availableSongs.get(i));
        return availableSongs.get(i);
    }

    private static boolean wasSongPlayed(String chosenSong){
        boolean wasPlayed = false;
        for (int i=0; i < usedSongs.size(); i++){
            wasPlayed = usedSongs.contains(chosenSong);
            if (wasPlayed) break;
        }
        return wasPlayed;
    }

    private static void addToUsedSongs(String song){
        usedSongs.add(song);
    }

    private static void removeLastSongFromUsed() {
        usedSongs.remove(0);
    }

}