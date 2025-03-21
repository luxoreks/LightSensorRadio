package com.example.LightSensitiveRadio;

public class BrightnessOptions {

    private static BrightnessOptions instance;
    private double maxBrightness;
    private double currentBrightness;
    private double previousBrightness;
    private int brightnessThreshold;

    private BrightnessOptions(){
        maxBrightness = 40;
        currentBrightness = 0;
        previousBrightness = 0;
        brightnessThreshold = 7;
    }
    public static BrightnessOptions getInstance(){
        if (instance == null){
            instance = new BrightnessOptions();

        }
        return instance;
    }

    public double getCurrentBrightness(){
        return currentBrightness;
    }
    public void setCurrentBrightness(double newBrightness){
        if (newBrightness >= 0){
            if (Math.abs(previousBrightness-newBrightness) >= 2){
                previousBrightness = currentBrightness;
            }
            currentBrightness = newBrightness;
        }

    }
    public double getMaxBrightness(){
        return maxBrightness;
    }
    public void setMaxBrightness(double newMaxBrightness){
        if (newMaxBrightness > this.maxBrightness){
            maxBrightness = newMaxBrightness;
        }
    }
    public int getBrightnessThreshold(){
        return brightnessThreshold;
    }
    public void setBrightnessThreshold(int newBrightnessThreshold){
            brightnessThreshold = newBrightnessThreshold;
    }
    public boolean isBrightnessIncreasing(){
        double difference = currentBrightness - previousBrightness;
        return difference > 0;
    }

    public boolean isBrightnessDecreasing(){
        double difference = currentBrightness - previousBrightness;
        return difference < -2;
    }

    public void updateBrightnessValues(double newBrightness){
        setCurrentBrightness(newBrightness);
        maxBrightnessCheck(newBrightness);
    }

    private void maxBrightnessCheck(double brightness){
        if (brightness > maxBrightness){
            setMaxBrightness(brightness);
        }
    }
}