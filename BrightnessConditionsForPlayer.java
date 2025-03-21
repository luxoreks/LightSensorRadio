package com.example.LightSensitiveRadio;

public class BrightnessConditionsForPlayer {

    private static BrightnessOptions brightnessOptions = BrightnessOptions.getInstance();
    private static final double BRIGHTNESS_DROP = 10;
    private static BrightnessConditionsForPlayer instance;

    private BrightnessConditionsForPlayer(){}

    public static BrightnessConditionsForPlayer getInstance(){
        if (instance == null){
            instance = new BrightnessConditionsForPlayer();
        }
        return instance;
    }

    public boolean shouldPlayerPlay(){
        return brightnessOptions.isBrightnessIncreasing() && isBrightnessHigh();
    }

    public boolean shouldPlayerStopped(){
        return (brightnessOptions.isBrightnessDecreasing() && isBrightnessLow() && brightnessOptions.getCurrentBrightness() < 60 ) || brightnessOptions.getCurrentBrightness() < brightnessOptions.getBrightnessThreshold();
    }

    private boolean isBrightnessLow(){
        double currentBrightness = brightnessOptions.getCurrentBrightness();
        double maxBrightness = brightnessOptions.getMaxBrightness();
        return currentBrightness < (maxBrightness-BRIGHTNESS_DROP);
    }

    private boolean isBrightnessHigh(){
        double currentBrightness = brightnessOptions.getCurrentBrightness();
        double brightnessThreshold = brightnessOptions.getBrightnessThreshold();
        return currentBrightness >= brightnessThreshold;
    }
}