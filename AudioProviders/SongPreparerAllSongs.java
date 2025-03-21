//package com.example.kibelsound.AudioProviders;
//
//import com.example.kibelsound.FilenamesRetriever;
//import com.example.kibelsound.Player.MediaPlayerState;
//import com.example.kibelsound.Player.MediaPlayer;
//import com.example.kibelsound.ServerAddressHolder;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Random;
//
//public class SongPreparerAllSongs {
//    public static boolean useNightMode = true;
//    private static int SONGS_WITHOUT_REPETITION = 5;
//    private static final MediaPlayer player = MediaPlayer.getInstance();
//    private static final FilenamesRetriever filenamesRetriever = new FilenamesRetriever();
//    private static List<String> usedSongs = new ArrayList<>();
//    public static boolean isDay = false;
//
//
//    public static void setSongsWithoutRepetition(int amount){
//        SONGS_WITHOUT_REPETITION = amount;
//        if (usedSongs.size() > amount){
//            while (usedSongs.size() > amount){
//                removeLastSongFromUsed();
//            }
//        }
//    }
//    public static int getSongsWithoutRepetition() {
//        return SONGS_WITHOUT_REPETITION;
//    }
//
//    public static void prepareNextSong(){
//        String song = getSong();
//        MediaPlayerState.currentSongPath = ServerAddressHolder.filePath + song;
//        player.reset();
//        player.setSong(MediaPlayerState.currentSongPath);
//        addToUsedSongs(song);
//        player.play();
//    }
//
//    private static String getSong(){
//
//        ArrayList<String> songs = new ArrayList<>();
//        if (!useNightMode){
//            isDay = true;
//        }
//        if (isDay){
//            songs = filenamesRetriever.retrieveFilenames(FilenamesRetriever.GET_SONGS_FILENAMES);
//        }
//        else{
//            songs = filenamesRetriever.retrieveFilenames(FilenamesRetriever.GET_AMBIENT_SONGS_FILENAMES);
//        }
//        Random rand = new Random();
//        List<String> availableSongs = songs;
//        availableSongs.removeAll(usedSongs);
//        int i = rand.nextInt(availableSongs.size());
//        return availableSongs.get(i);
//    }
//
//    private static boolean wasSongPlayed(String chosenSong){
//        boolean wasPlayed = false;
//        for (int i=0; i < usedSongs.size(); i++){
//            wasPlayed = usedSongs.contains(chosenSong);
//            if (wasPlayed) break;
//        }
//        return wasPlayed;
//    }
//
//    private static void addToUsedSongs(String song){
//        usedSongs.add(song);
//        while (usedSongs.size() >= SONGS_WITHOUT_REPETITION){
//            removeLastSongFromUsed();
//        }
//    }
//
//    private static void removeLastSongFromUsed() {
//        usedSongs.remove(0);
//    }
//
//}
//
//package com.example.kibelsound.AudioProviders;
//
//        import com.example.kibelsound.FilenamesRetriever;
//        import com.example.kibelsound.Player.MediaPlayerState;
//        import com.example.kibelsound.Player.MediaPlayer;
//        import com.example.kibelsound.ServerAddressHolder;
//
//        import java.util.ArrayList;
//        import java.util.Arrays;
//        import java.util.List;
//        import java.util.Random;
//
//public class NextSongPreparer {
//    public static boolean useNightMode = true;
//    private static int SONGS_WITHOUT_REPETITION = 5;
//    private static final MediaPlayer player = MediaPlayer.getInstance();
//    private static final FilenamesRetriever filenamesRetriever = new FilenamesRetriever();
//    private static List<String> usedSongs = new ArrayList<>(Arrays.asList("nieUsuwac.mp3"));
//    public static boolean isDay = false;
//
//
//    public static void setSongsWithoutRepetition(int amount){
//        SONGS_WITHOUT_REPETITION = amount;
//        if (usedSongs.size() > amount){
//            while (usedSongs.size() > amount){
//                removeLastSongFromUsed();
//            }
//        }
//    }
//    public static int getSongsWithoutRepetition() {
//        return SONGS_WITHOUT_REPETITION;
//    }
//
//    public static void prepareNextSong(){
//        String song = getSong();
//        MediaPlayerState.currentSongPath = ServerAddressHolder.filePath + song;
//        player.reset();
//        player.setSong(MediaPlayerState.currentSongPath);
//        addToUsedSongs(song);
//        player.play();
//    }
//
//    private static String getSong(){
//
//        String[] songs = new String[]{};
//        if (!useNightMode){
//            isDay = true;
//        }
//        if (isDay){
//            songs = filenamesRetriever.retrieveFilenames(FilenamesRetriever.GET_SONGS_FILENAMES);
//        }
//        else{
//            songs = filenamesRetriever.retrieveFilenames(FilenamesRetriever.GET_AMBIENT_SONGS_FILENAMES);
//        }
//        Random rand = new Random();
//        int i = rand.nextInt(songs.length);
//        while (wasSongPlayed(songs[i])){
//            i = rand.nextInt(songs.length);
//        }
//        return songs[i];
//    }
//
//    private static boolean wasSongPlayed(String chosenSong){
//        boolean wasPlayed = false;
//        for (int i=0; i < usedSongs.size(); i++){
//            wasPlayed = usedSongs.contains(chosenSong);
//            if (wasPlayed) break;
//        }
//        return wasPlayed;
//    }
//
//    private static void addToUsedSongs(String song){
//        usedSongs.add(song);
//        while (usedSongs.size() >= SONGS_WITHOUT_REPETITION){
//            removeLastSongFromUsed();
//        }
//    }
//
//    private static void removeLastSongFromUsed() {
//        usedSongs.remove(0);
//    }
//
//}