package com.example.LightSensitiveRadio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FoldersCreatorActivity extends AppCompatActivity {
    private static final String TAG = "FolderCreatorActivity";
    private static final String rootDirectoryName = "WC_Radio";
    private static final String daySongsDirectoryName = "Day songs";
    private static final String nightSongsDirectoryName = "Night songs";
    private static final String lightOffAudioDirectoryName = "pzg";
    private static final String advertsDirectoryName = "ads";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders_creator);
        
        createNecessaryFolders();
    }

    private void createNecessaryFolders() {
        createMainFolder();
        createSubfolders();
    }

    private void createMainFolder() {
        createFolder(rootDirectoryName, true);
    }
    private void createSubfolders() {
        createDayFolder();
        createNightFolder();
        createSilencePlayerFolder();
        createAdvertsFolder();

    }

    private void createDayFolder() {
        createFolder(daySongsDirectoryName, false);
        populateFolder("defaultSongs/daySongs/",daySongsDirectoryName);
    }

    private void createNightFolder() {
        createFolder(nightSongsDirectoryName, false);
        //populateFolder("defaultSongs/nightSongs/", nightSongsDirectoryName);
    }
    private void createSilencePlayerFolder() {
        createFolder(lightOffAudioDirectoryName, false);
        //populateFolder("defaultSongs/pzg/", lightOffAudioDirectoryName);

    }
    private void createAdvertsFolder() {
        createFolder(advertsDirectoryName, false);
        //populateFolder("defaultSongs/ads/", advertsDirectoryName);
    }

    private void createFolder(String folderName, boolean isRootFolder){
        String rootDirectory = isRootFolder? "/":"/"+ rootDirectoryName +"/";
        String path = String.valueOf(Environment.getExternalStorageDirectory()) + rootDirectory + folderName;
        File directory = new File(path);
        boolean folderCreated = directory.mkdir();
        String successIndicator = folderCreated? "✔️":"❌";
        if (folderCreated){
            Toast.makeText(this, "Folder created: " + folderName + " " + successIndicator, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Folder not created: " + folderName+ " " + successIndicator, Toast.LENGTH_SHORT).show();
        }
    }
    private void populateFolder(String songsFolderPath, String targetFolder) {
        String targetPath = Environment.getExternalStorageDirectory() + "/" + rootDirectoryName + "/" + targetFolder + "/";
        AssetManager am = this.getAssets();
        String[] allFilenames = null;
        try {
            allFilenames = am.list(songsFolderPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Log.i(TAG, "Files: " + allFilenames[0]);
        try {
            for (String fileName : allFilenames){
                copyAssetToExternalStorage(songsFolderPath, fileName, targetPath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void copyAssetToExternalStorage(String originalPath, String fileName, String endPath) throws IOException {
        AssetManager am = this.getAssets();
        String completePath = originalPath + fileName;
        InputStream in = am.open(completePath);
        FileOutputStream out = new FileOutputStream(endPath + fileName);
        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }
    }
}