package com.example.LightSensitiveRadio;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class FilenamesRetriever {

    public static final short GET_ADVERTS_FILENAMES = 0;
    public static final short GET_SONGS_FILENAMES = 1;
    public static final short GET_AMBIENT_SONGS_FILENAMES = 2;
    private String pathFolder = "";

    public ArrayList<String> retrieveFilenames(int filenamesCode){
        readFilenameCode(filenamesCode);
        String path = "/storage/emulated/0/sfx" + pathFolder;
        File rootDir = new File(path);
        ServerAddressHolder.filePath = path;
        String[] files2 = rootDir.list();
        ArrayList<String> allFileNames = new ArrayList<>(Arrays.asList(files2));
        //System.out.println(Arrays.toString(files2));
        return allFileNames;
    }

    private void readFilenameCode(int folderCode) {
        switch (folderCode){
            case GET_ADVERTS_FILENAMES:
                pathFolder = "/ads/";
                break;
            case GET_SONGS_FILENAMES:
                pathFolder = "/day songs/";
                break;
            case GET_AMBIENT_SONGS_FILENAMES:
                pathFolder = "/night songs/";
                break;
        }
    }
}