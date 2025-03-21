package com.example.LightSensitiveRadio.LoundSound;

public class RecordedSoundHolder {
    private static boolean shouldSaveValues = false;
    private static String recordedSound = "";

    public static String getRecordedSound() {
        return recordedSound;
    }

    public static void addRecordedSound(String recordedSound) {
        RecordedSoundHolder.recordedSound += recordedSound + "\n";
    }

    public static void resetRecordedSound(){
        RecordedSoundHolder.recordedSound = "";
    }

    public static boolean shouldSaveValues() {
        return shouldSaveValues;
    }

    public static void setShouldSaveValues(boolean shouldSaveValues) {
        RecordedSoundHolder.shouldSaveValues = shouldSaveValues;
    }

}