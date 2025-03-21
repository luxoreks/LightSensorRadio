package com.example.LightSensitiveRadio.LoundSound;

public class RecordedSoundValueHolder{

    private short[] maxSoundValues;

    private static RecordedSoundValueHolder instance;

    private RecordedSoundValueHolder(){
        maxSoundValues = new short[2];
    }

    public static RecordedSoundValueHolder getInstance(){
        if (instance == null){
            instance = new RecordedSoundValueHolder();
        }
        return instance;
    }

    public short[] getCurrentSoundValues(){
        return maxSoundValues;
    }

    public void updateCurrentSoundValues(short val){
        maxSoundValues[0] = maxSoundValues[1];
        maxSoundValues[1] = val;
    }
}