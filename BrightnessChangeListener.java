package com.example.LightSensitiveRadio;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.example.LightSensitiveRadio.Player.MediaPlayerManager;

public class BrightnessChangeListener implements SensorEventListener {
    private final int MIN_NOTICEABLE_BRIGHTNESS_CHANGE = 3;
    private BrightnessOptions brightnessOptions = BrightnessOptions.getInstance();
    private final MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance();


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double registeredBrightness = sensorEvent.values[0];
        if (Math.abs(brightnessOptions.getCurrentBrightness() - registeredBrightness) > MIN_NOTICEABLE_BRIGHTNESS_CHANGE){
            brightnessOptions.updateBrightnessValues(registeredBrightness);
            mediaPlayerManager.updateMediaPlayerState();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}