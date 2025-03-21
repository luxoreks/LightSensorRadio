package com.example.LightSensitiveRadio.KeepScreenAlive;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.LightSensitiveRadio.AudioProviders.AudioPreparer;
import com.example.LightSensitiveRadio.BrightnessChangeListener;
import com.example.LightSensitiveRadio.MainActivity;
import com.example.LightSensitiveRadio.R;

public class ForegroundService extends Service {
    private static final String TAG = "ForegroundService";
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static boolean isServiceRunning;
    private SensorManager mSensorManager;
    private final SensorEventListener brightnessChangeListener = new BrightnessChangeListener();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals("StopService")){
            Log.i(TAG, "action StopService");
            AudioPreparer.prepareAudio();
            //mSensorManager.unregisterListener(brightnessChangeListener);
            //stopForeground(true);
            //AppContextProvider.getInstance().getMainActivity().stopService(notificationIntent);
            //stopForeground(STOP_FOREGROUND_REMOVE);
            stopSelf();
        }
        else{

            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            Sensor mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            mSensorManager.registerListener(brightnessChangeListener, mLightSensor, SensorManager.SENSOR_DELAY_UI);
            String input = intent.getStringExtra("notificationText");
            createNotificationChannel();
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("WC Radio")
                    .setContentText(input)
                    .setSmallIcon(R.drawable.service_icon)
                    //.setContentIntent(pendingIntent)
                    .build();

            startForeground(1001, notification);
            isServiceRunning = true;
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "od Destroy function");
        stopForeground(STOP_FOREGROUND_REMOVE);
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public static boolean isServiceRunning() {
        return isServiceRunning;
    }

    public static void setServiceRunning(boolean serviceRunning) {
        isServiceRunning = serviceRunning;
    }
}