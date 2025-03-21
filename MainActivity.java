package com.example.LightSensitiveRadio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.LightSensitiveRadio.AudioProviders.AdvertisePreparer;
import com.example.LightSensitiveRadio.AudioProviders.AudioPreparer;
import com.example.LightSensitiveRadio.AudioProviders.NextSongPreparer;
import com.example.LightSensitiveRadio.Databases.ModesDBHelper;
import com.example.LightSensitiveRadio.Databases.SettingsDBHelper;
import com.example.LightSensitiveRadio.KeepScreenAlive.ForegroundService;
import com.example.LightSensitiveRadio.Player.MediaPlayerManager;
import com.example.LightSensitiveRadio.Settings.SettingsActivity;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    private TextView loudnessIndicator;
    private SensorManager mSensorManager;
    private Sensor mLightSensor;
    private float mLightQuantity;
    private ModesDBHelper modesDBHelper;
    private SettingsDBHelper settingsDBHelper;

    private SensorEventListener brightnessChangeListener;


    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            loudnessIndicator.setText(message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppContextProvider.getInstance().setAppContext(this);
        stupChangeSOngButton();
        prepareDataBases();
        setSettingsFromDataBases();
        setupHotWordDetector();
        //startAppBackgroundService();
        startPartOfDaySupervisor();
        setupBrightnessSensorEventListeners();
        //setupLoudnessIndicator(); //zastąpione przez hot word detector
        setupSettingsButton();
        AudioPreparer.prepareAudio();
    }

    private void stupChangeSOngButton() {
        Button changeSOngButton = findViewById(R.id.buttonChangeSong);
        changeSOngButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioPreparer.prepareAudio();
            }
        });
    }

    @Override
    protected void onDestroy() {
        Intent serviceStop = new Intent(MainActivity.this, ForegroundService.class);
        MainActivity.this.stopService(serviceStop);
        MediaPlayerManager.getInstance().releasePlayer();
        super.onDestroy();
//        serviceStop.setAction("StopService");
//        //startService(serviceStop);

    }

    private void setupLoudnessIndicator() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("update-text"));
        loudnessIndicator = findViewById(R.id.loudnessTextBox);
        loudnessIndicator.setMovementMethod(new ScrollingMovementMethod());
    }
    private void prepareDataBases(){
        modesDBHelper = new ModesDBHelper(MainActivity.this);
        if (modesDBHelper.isDataBaseEmpty()){
            modesDBHelper.addMode(ModesDBHelper.NIGHT_MODE, true);
            modesDBHelper.addMode(ModesDBHelper.PLAY_ADS_MODE, true);
            modesDBHelper.addMode(ModesDBHelper.DETECT_HOT_WORD, true);
        }

        settingsDBHelper = new SettingsDBHelper(MainActivity.this);
        if (settingsDBHelper.isDataBaseEmpty()){
            settingsDBHelper.addSettingsValue(SettingsDBHelper.COLUMN_BRIGHTNESS_THRESHOLD, 7);
            settingsDBHelper.addSettingsValue(SettingsDBHelper.COLUMN_SONGS_WITHOUT_REPETITION, 5);
            settingsDBHelper.addSettingsValue(SettingsDBHelper.COLUMN_SONGS_BEFORE_AD, 2);
            settingsDBHelper.addSettingsValue(SettingsDBHelper.COLUMN_ADS_WITHOUT_REPETITION, 5);
            settingsDBHelper.addSettingsValue(SettingsDBHelper.COLUMN_DAY_MODE_HOUR, 10);
            settingsDBHelper.addSettingsValue(SettingsDBHelper.COLUMN_DAY_MODE_MINUTE, 0);
            settingsDBHelper.addSettingsValue(SettingsDBHelper.COLUMN_NIGHT_MODE_HOUR, 22);
            settingsDBHelper.addSettingsValue(SettingsDBHelper.COLUMN_NIGHT_MODE_MINUTE, 0);
        }
    }
    private void setSettingsFromDataBases(){
        NextSongPreparer.useNightMode = modesDBHelper.isModeActive(ModesDBHelper.NIGHT_MODE);
        AudioPreparer.playAds = modesDBHelper.isModeActive(ModesDBHelper.PLAY_ADS_MODE);
        AudioPreparer.setSongsBetweenAdvert((short) settingsDBHelper.getSettingsValue(SettingsDBHelper.COLUMN_SONGS_BEFORE_AD));
        AdvertisePreparer.setAdvertsWithoutRepetition(settingsDBHelper.getSettingsValue(SettingsDBHelper.COLUMN_ADS_WITHOUT_REPETITION));
        NightModeValuesHolder.StartNightModeHour = settingsDBHelper.getSettingsValue(SettingsDBHelper.COLUMN_NIGHT_MODE_HOUR);
        NightModeValuesHolder.StartNightModeMinute = settingsDBHelper.getSettingsValue(SettingsDBHelper.COLUMN_NIGHT_MODE_MINUTE);
        NightModeValuesHolder.StopNightModeHour = settingsDBHelper.getSettingsValue(SettingsDBHelper.COLUMN_DAY_MODE_HOUR);
        NightModeValuesHolder.StopNightModeMinute = settingsDBHelper.getSettingsValue(SettingsDBHelper.COLUMN_DAY_MODE_MINUTE);
        BrightnessOptions.getInstance().setBrightnessThreshold(settingsDBHelper.getSettingsValue(SettingsDBHelper.COLUMN_BRIGHTNESS_THRESHOLD));
        NextSongPreparer.setSongsWithoutRepetition(settingsDBHelper.getSettingsValue(SettingsDBHelper.COLUMN_SONGS_WITHOUT_REPETITION));
        HotWordDetector.getInstance().enableHotWordDetector(modesDBHelper.isModeActive(ModesDBHelper.DETECT_HOT_WORD));
    }
    private void setupHotWordDetector(){
        HotWordDetector hotWordDetector = HotWordDetector.getInstance();
        hotWordDetector.prepareHotWordDetector();
        hotWordDetector.enableHotWordDetector(true);
    }
    private void startPartOfDaySupervisor() {
        (new Thread(){
            @Override
            public void run() {
                while (true){
                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    String actual = sdf.format(currentTime);
                    String lowerLimitTime = NightModeValuesHolder.StartNightModeHour + ":" + NightModeValuesHolder.StartNightModeMinute;
                    String upperLimitTime = NightModeValuesHolder.StopNightModeHour + ":" + NightModeValuesHolder.StopNightModeMinute;

                    DateTimeFormatter df = DateTimeFormat.forPattern("HH:mm");
                    DateTime ac = df.parseLocalTime(actual).toDateTimeToday();
                    DateTime lowerLim = df.parseLocalTime(lowerLimitTime).toDateTimeToday();
                    DateTime upperLim = df.parseLocalTime(upperLimitTime).toDateTimeToday();

                    if(NextSongPreparer.useNightMode){
                        NextSongPreparer.isDay = ac.isAfter(upperLim) && ac.isBefore(lowerLim);
                    }
                    //Log.i("Thread", "Is Day: " + NextSongPreparer.isDay);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }
    private void setupSettingsButton() {
        ImageButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(view -> {
            Intent settingsActivityIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsActivityIntent);
        });
    }
    private void setupBrightnessSensorEventListeners() {
        TextView brightnessNumber = findViewById(R.id.brightnessNumber);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        SensorEventListener listener2 = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                mLightQuantity = event.values[0];
                brightnessNumber.setText(String.valueOf(Math.round(mLightQuantity*10)/10));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        mSensorManager.registerListener(listener2, mLightSensor, SensorManager.SENSOR_DELAY_UI);
    }
}