package com.example.LightSensitiveRadio;

import com.example.LightSensitiveRadio.AudioProviders.AudioPreparer;
import com.example.LightSensitiveRadio.Player.MediaPlayer;

import ai.picovoice.porcupine.PorcupineException;
import ai.picovoice.porcupine.PorcupineManager;
import ai.picovoice.porcupine.PorcupineManagerCallback;

public class HotWordDetector {
    private static HotWordDetector instance;
    private PorcupineManager porcupineManager;
    private HotWordDetector(){}
    public static HotWordDetector getInstance(){
        if (instance == null){
            instance = new HotWordDetector();
        }
        return instance;
    }

    public void prepareHotWordDetector(){
        PorcupineManagerCallback wakeWordCallback = keywordIndex -> {
            if (keywordIndex == 0) {
                if (MediaPlayer.getInstance().isPlaying()){
                    AudioPreparer.prepareAudio();
                }
            }
        };

        try {
            porcupineManager = new PorcupineManager.Builder()

                    .setAccessKey("here paste generated access key")
                    .setKeywordPath("model/keyword.ppn")
                    .setModelPath("model/model.pv")
                    //.setKeyword(Porcupine.BuiltInKeyword.ALEXA)
                    .build(AppContextProvider.getInstance().getAppContext(), wakeWordCallback);
        } catch (PorcupineException e) {
            throw new RuntimeException(e);
        }
    }

    public void enableHotWordDetector(boolean enableDetector){
        if (enableDetector){
            startHotWordDetector();
        }
        else{
            stopHotWordDetector();
        }
    }
    private void startHotWordDetector(){
        if (porcupineManager == null) prepareHotWordDetector();
        try {
            porcupineManager.start();
        } catch (PorcupineException e) {
            throw new RuntimeException(e);
        }
    }

    private void stopHotWordDetector(){
        try {
            porcupineManager.stop();
        } catch (PorcupineException e) {
            throw new RuntimeException(e);
        }
    }
}