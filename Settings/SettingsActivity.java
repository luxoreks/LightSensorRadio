package com.example.LightSensitiveRadio.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.LightSensitiveRadio.AboutApp.AboutAppActivity;
import com.example.LightSensitiveRadio.AudioProviders.AdvertisePreparer;
import com.example.LightSensitiveRadio.AudioProviders.AudioPreparer;
import com.example.LightSensitiveRadio.BrightnessOptions;
import com.example.LightSensitiveRadio.Databases.SettingsDBHelper;
import com.example.LightSensitiveRadio.HotWordDetector;
import com.example.LightSensitiveRadio.KeepScreenAlive.ForegroundService;
import com.example.LightSensitiveRadio.Player.MediaPlayerManager;
import com.example.LightSensitiveRadio.Databases.ModesDBHelper;
import com.example.LightSensitiveRadio.AudioProviders.NextSongPreparer;
import com.example.LightSensitiveRadio.NightModeValuesHolder;
import com.example.LightSensitiveRadio.R;
import com.example.LightSensitiveRadio.Special.SpecialActivity;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";

    private Intent serviceIntent;
    private ModesDBHelper modesDBHelper;
    private SettingsDBHelper settingsDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        modesDBHelper = new ModesDBHelper(SettingsActivity.this);
        settingsDBHelper = new SettingsDBHelper(SettingsActivity.this);

        prepareBrightnessThresholdSection();
        prepareNightModeSection();
        prepareMusicSection();
        prepareAdsSection();
        prepareMoreSection();

        CheckBox hotWordDetectorCheckBox = findViewById(R.id.hot_word_detector_CheckBox);
        hotWordDetectorCheckBox.setChecked(modesDBHelper.isModeActive(ModesDBHelper.DETECT_HOT_WORD));
        hotWordDetectorCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean enableHotWordDetector = hotWordDetectorCheckBox.isChecked();
                modesDBHelper.updateModeState(ModesDBHelper.DETECT_HOT_WORD, enableHotWordDetector);
                HotWordDetector.getInstance().enableHotWordDetector(enableHotWordDetector);
            }
        });

        LinearLayout aboutAppLayout = findViewById(R.id.aboutLayout);
        aboutAppLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent aboutActivity = new Intent(SettingsActivity.this, AboutAppActivity.class);
                startActivity(aboutActivity);
            }
        });

        LinearLayout specialThanksLayout = findViewById(R.id.specialThanksLayout);
        specialThanksLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent specialActivity = new Intent(SettingsActivity.this, SpecialActivity.class);
                startActivity(specialActivity);
            }
        });

    }

    private void prepareBrightnessThresholdSection() {
        BrightnessOptions.getInstance().setBrightnessThreshold(settingsDBHelper.getSettingsValue(SettingsDBHelper.COLUMN_BRIGHTNESS_THRESHOLD));
        TextView brightnessThresholdIndicator = findViewById(R.id.brightnessThresholdIndicator);
        brightnessThresholdIndicator.setText(String.valueOf(BrightnessOptions.getInstance().getBrightnessThreshold()));
        SeekBar brightnessThreshold = findViewById(R.id.seekBar2);
        brightnessThreshold.setProgress(BrightnessOptions.getInstance().getBrightnessThreshold());
        brightnessThreshold.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                brightnessThresholdIndicator.setText(Integer.toString(seekBar.getProgress()));
                BrightnessOptions.getInstance().setBrightnessThreshold(i);
                MediaPlayerManager mpManager = MediaPlayerManager.getInstance();
                mpManager.updateMediaPlayerState();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                settingsDBHelper.updateSettingsValue(SettingsDBHelper.COLUMN_BRIGHTNESS_THRESHOLD, (int) seekBar.getProgress());
            }
        });
    }
    private void prepareNightModeSection() {
        //dodana baza danych
        NextSongPreparer.useNightMode = modesDBHelper.isModeActive(ModesDBHelper.NIGHT_MODE);
        CheckBox nightModeCheckbox = findViewById(R.id.night_mode_checkbox);
        nightModeCheckbox.setChecked(NextSongPreparer.useNightMode);
        enableNightModeViews(NextSongPreparer.useNightMode);
        nightModeCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableNightModeViews(true);
                NextSongPreparer.useNightMode = nightModeCheckbox.isChecked();
                if(!nightModeCheckbox.isChecked()){
                    NextSongPreparer.isDay = true;
                    enableNightModeViews(false);
                }
                modesDBHelper.updateModeState(ModesDBHelper.NIGHT_MODE, NextSongPreparer.useNightMode);
            }
        });

        //dodana baza danych
        NightModeValuesHolder.StartNightModeHour = settingsDBHelper.getSettingsValue(SettingsDBHelper.COLUMN_NIGHT_MODE_HOUR);
        NightModeValuesHolder.StartNightModeMinute = settingsDBHelper.getSettingsValue(SettingsDBHelper.COLUMN_NIGHT_MODE_MINUTE);
        TextView nighTimeHolder = findViewById(R.id.nightmodeTimeHolder);
        String timeRefactor = NightModeValuesHolder.StartNightModeMinute<10? "0" : "";
        nighTimeHolder.setText(String.format("Set night mode on hour: %d:%s%d", NightModeValuesHolder.StartNightModeHour, timeRefactor, NightModeValuesHolder.StartNightModeMinute));
        Button autoNightModeStart = findViewById(R.id.nightModeTimeSetter_btn);
        autoNightModeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

                        String timeRefactor = minute<10? "0" : "";
                        nighTimeHolder.setText("Set night mode on hour: " + hourOfDay + ":" + timeRefactor + minute);
                        NightModeValuesHolder.StartNightModeHour = hourOfDay;
                        NightModeValuesHolder.StartNightModeMinute = minute;
                        settingsDBHelper.updateSettingsValue(SettingsDBHelper.COLUMN_NIGHT_MODE_HOUR, hourOfDay);
                        settingsDBHelper.updateSettingsValue(SettingsDBHelper.COLUMN_NIGHT_MODE_MINUTE, minute);
                    }
                }, hour, minutes, true);
                timePickerDialog.show();
            }
        });

        //dodana baza danych
        NightModeValuesHolder.StopNightModeHour = settingsDBHelper.getSettingsValue(SettingsDBHelper.COLUMN_DAY_MODE_HOUR);
        NightModeValuesHolder.StopNightModeMinute = settingsDBHelper.getSettingsValue(SettingsDBHelper.COLUMN_DAY_MODE_MINUTE);
        Log.i(TAG, "Hour: " + NightModeValuesHolder.StopNightModeHour + "Minute: " + NightModeValuesHolder.StopNightModeMinute);
        TextView dayTimeHolder = findViewById(R.id.daymodeTimeHolder);
        String timeRefactor2 = NightModeValuesHolder.StopNightModeMinute<10? "0" : "";
        dayTimeHolder.setText(String.format("Set day mode on hour: %d:%s%d", NightModeValuesHolder.StopNightModeHour, timeRefactor2, NightModeValuesHolder.StopNightModeMinute));
        Button autoNightModeStop = findViewById(R.id.dayModeTimeSetter_btn);
        autoNightModeStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        String timeRefactor = minute<10? "0" : "";
                        dayTimeHolder.setText("Set day mode on hour: " + hourOfDay + ":" + timeRefactor + minute);
                        NightModeValuesHolder.StopNightModeHour = hourOfDay;
                        NightModeValuesHolder.StopNightModeMinute = minute;
                        settingsDBHelper.updateSettingsValue(SettingsDBHelper.COLUMN_DAY_MODE_HOUR, hourOfDay);
                        settingsDBHelper.updateSettingsValue(SettingsDBHelper.COLUMN_DAY_MODE_MINUTE, minute);
                    }
                }, hour, minutes, true);
                timePickerDialog.show();
            }
        });
    }
    private void prepareMusicSection() {
        // songs without repetition
        NextSongPreparer.setSongsWithoutRepetition(settingsDBHelper.getSettingsValue(SettingsDBHelper.COLUMN_SONGS_WITHOUT_REPETITION));
        TextView songsWithoutRepetitionIndicator = findViewById(R.id.songsWithoutRepetitionIndicator);
        SeekBar songsWithoutRepetition = findViewById(R.id.songsWithoutRepetitionSeekBar);
        songsWithoutRepetition.setProgress(NextSongPreparer.getSongsWithoutRepetition());
        songsWithoutRepetition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                songsWithoutRepetitionIndicator.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                NextSongPreparer.setSongsWithoutRepetition(seekBar.getProgress());
                settingsDBHelper.updateSettingsValue(SettingsDBHelper.COLUMN_SONGS_WITHOUT_REPETITION, (int) seekBar.getProgress());
            }
        });
    }
    private void prepareAdsSection(){
        AudioPreparer.playAds = modesDBHelper.isModeActive(ModesDBHelper.PLAY_ADS_MODE);
        CheckBox playAdsCheckBox = findViewById(R.id.play_ads_check_box);
        playAdsCheckBox.setChecked(AudioPreparer.playAds);
        enableAdsViews(AudioPreparer.playAds);
        playAdsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioPreparer.playAds = playAdsCheckBox.isChecked();
                enableAdsViews(playAdsCheckBox.isChecked());
                modesDBHelper.updateModeState(ModesDBHelper.PLAY_ADS_MODE, AudioPreparer.playAds);
            }
        });

        //adverts without repetition
        AdvertisePreparer.setAdvertsWithoutRepetition(settingsDBHelper.getSettingsValue(SettingsDBHelper.COLUMN_ADS_WITHOUT_REPETITION));
        TextView adsWithoutRepIndicator = findViewById(R.id.ads_without_rep_indicator);
        adsWithoutRepIndicator.setText(String.valueOf((AdvertisePreparer.getAdvertsWithoutRepetition())));
        SeekBar adsWithoutRepSeekBar = findViewById(R.id.ads_without_rep_SeekBar);
        adsWithoutRepSeekBar.setProgress(AdvertisePreparer.getAdvertsWithoutRepetition());
        adsWithoutRepSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                adsWithoutRepIndicator.setText(String.valueOf((i)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                AdvertisePreparer.setAdvertsWithoutRepetition(seekBar.getProgress());
                settingsDBHelper.updateSettingsValue(SettingsDBHelper.COLUMN_ADS_WITHOUT_REPETITION, (int) seekBar.getProgress());
            }
        });

        //songs between advert
        AudioPreparer.setSongsBetweenAdvert((short) settingsDBHelper.getSettingsValue(SettingsDBHelper.COLUMN_SONGS_BEFORE_AD));
        TextView songsBetweenAdsIndicator = findViewById(R.id.songsBeforeAdIndicator);
        songsBetweenAdsIndicator.setText(String.valueOf(AudioPreparer.getSongsBetweenAdvert()));
        SeekBar songsBeforeAdSeekBar = findViewById(R.id.songsBeforeAdSeekBar);
        songsBeforeAdSeekBar.setProgress(AudioPreparer.getSongsBetweenAdvert());
        songsBeforeAdSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TextView indicator = findViewById(R.id.songsBeforeAdIndicator);
                indicator.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                AudioPreparer.setSongsBetweenAdvert((short) seekBar.getProgress());
                settingsDBHelper.updateSettingsValue(SettingsDBHelper.COLUMN_SONGS_BEFORE_AD, (int) seekBar.getProgress());
            }
        });
    }
    private void prepareMoreSection(){
        Button startStopServiceButton = findViewById(R.id.start_service_button);
        startStopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ForegroundService.isServiceRunning()){
                    boolean serviceStopped = stopAppBackgroundService();
                    if(serviceStopped){
                        Toast.makeText(SettingsActivity.this, "Działanie w tle zatrzymane", Toast.LENGTH_SHORT).show();
                        ForegroundService.setServiceRunning(false);
                    }
                    else{
                        Toast.makeText(SettingsActivity.this, "Nie udało się zatrzymać działania w tle", Toast.LENGTH_SHORT).show();
                        ForegroundService.setServiceRunning(true);
                    }
                }
                else{
                    boolean serviceStared = startAppBackgroundService();
                    if(serviceStared){
                        Toast.makeText(SettingsActivity.this, "Aplikacja działa w tle", Toast.LENGTH_SHORT).show();
                        ForegroundService.setServiceRunning(true);
                    }
                    else{
                        Toast.makeText(SettingsActivity.this, "Nie udało się uruchomić działania w tle", Toast.LENGTH_SHORT).show();
                        ForegroundService.setServiceRunning(false);
                    }
                }
                String buttonText = ForegroundService.isServiceRunning()? "Stop" : "Start";
                startStopServiceButton.setText(buttonText);
            }
        });
    }
    private boolean startAppBackgroundService(){
        serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("notificationText", "Aplikacja działa w tle");
        serviceIntent.setAction("StartService");
        startService(serviceIntent);
        return isServiceRunning(ForegroundService.class);
    }
    private boolean stopAppBackgroundService(){
        Intent serviceStop = new Intent(SettingsActivity.this, ForegroundService.class);
        serviceStop.setAction("StopService");
        //startService(serviceStop);
        stopService(serviceStop);
        return !isServiceRunning(ForegroundService.class);
    }
    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private void enableNightModeViews(boolean enableViews){
        TextView dayModeTitle = findViewById(R.id.day_mode_title);
        TextView nightModeTitle = findViewById(R.id.night_mode_title);
        TextView nighTimeHolder = findViewById(R.id.nightmodeTimeHolder);
        TextView dayTimeHolder = findViewById(R.id.daymodeTimeHolder);
        Button nightModeTimePicker_btn = findViewById(R.id.nightModeTimeSetter_btn);
        Button dayModeTimePicker_btn = findViewById(R.id.dayModeTimeSetter_btn);

        dayModeTitle.setEnabled(enableViews);
        nightModeTitle.setEnabled(enableViews);
        nighTimeHolder.setEnabled(enableViews);
        dayTimeHolder.setEnabled(enableViews);
        nightModeTimePicker_btn.setEnabled(enableViews);
        dayModeTimePicker_btn.setEnabled(enableViews);
    }
    private void enableAdsViews(boolean enableViews){
        int visibility = enableViews? View.VISIBLE : View.INVISIBLE;

        TextView adsTitle = findViewById(R.id.songs_before_ad_title);
        TextView description = findViewById(R.id.songs_before_ads_descr);
        TextView indicator = findViewById(R.id.songsBeforeAdIndicator);
        SeekBar seekBar = findViewById(R.id.songsBeforeAdSeekBar);

        TextView adsWithoutRepTitle = findViewById(R.id.ads_without_rep_title);
        TextView adsWithoutRepDescr = findViewById(R.id.ads_without_rep_descr);
        TextView adsWithoutRepIndicator = findViewById(R.id.ads_without_rep_indicator);
        SeekBar adsWithoutRepSeekBar = findViewById(R.id.ads_without_rep_SeekBar);

        adsTitle.setEnabled(enableViews);
        description.setEnabled(enableViews);
        indicator.setEnabled(enableViews);
        seekBar.setVisibility(visibility);

        adsWithoutRepTitle.setEnabled(enableViews);
        adsWithoutRepDescr.setEnabled(enableViews);
        adsWithoutRepIndicator.setEnabled(enableViews);
        adsWithoutRepSeekBar.setVisibility(visibility);

    }
}