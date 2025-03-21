package com.example.LightSensitiveRadio.AudioProviders;

import com.example.LightSensitiveRadio.FilenamesRetriever;
import com.example.LightSensitiveRadio.Player.MediaPlayerState;
import com.example.LightSensitiveRadio.Player.MediaPlayer;
import com.example.LightSensitiveRadio.ServerAddressHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdvertisePreparer {

    private static int ADVERTS_WITHOUT_REPETITION = 5;
    private static final MediaPlayer player = MediaPlayer.getInstance();
    private static final FilenamesRetriever filenamesRetriever = new FilenamesRetriever();
    private static List<String> usedAdverts = new ArrayList<>();

    public static int getAdvertsWithoutRepetition() {
        return ADVERTS_WITHOUT_REPETITION;
    }

    public static void setAdvertsWithoutRepetition(int advertsWithoutRepetition) {
        ADVERTS_WITHOUT_REPETITION = advertsWithoutRepetition;
    }

    public static void prepareAdvert(){
            String advert = getAdvert();
            MediaPlayerState.currentSongPath = ServerAddressHolder.filePath + advert;
            player.reset();
            player.setSong(MediaPlayerState.currentSongPath);
            addToUsedAdverts(advert);
            player.play();
    }

    private static String getAdvert(){
        ArrayList<String> adverts = filenamesRetriever.retrieveFilenames(FilenamesRetriever.GET_ADVERTS_FILENAMES);
        ArrayList<String> availableAdverts = (ArrayList<String>) adverts.clone();
        availableAdverts.removeAll(usedAdverts);
        if (availableAdverts.size() == 0){
            availableAdverts = adverts;
            usedAdverts = new ArrayList<>();
        }
        Random rand = new Random();
        int i = rand.nextInt(availableAdverts.size());
//        while (wasAdvertPlayed(availableAdverts.get(i))){
//            i = rand.nextInt(availableAdverts.size());
//        }
        return availableAdverts.get(i);
    }

    private static boolean wasAdvertPlayed(String chosenSong){
        boolean wasPlayed = false;
        for (int i = 0; i < usedAdverts.size(); i++){
            wasPlayed = usedAdverts.contains(chosenSong);
            if (wasPlayed) break;
        }
        return wasPlayed;
    }

    private static void addToUsedAdverts(String song){
        usedAdverts.add(song);
//        while (usedAdverts.size() >= ADVERTS_WITHOUT_REPETITION){
//            removeLastSongFromAdverts();
//        }
    }

    private static void removeLastSongFromAdverts() {
        usedAdverts.remove(0);
    }
}