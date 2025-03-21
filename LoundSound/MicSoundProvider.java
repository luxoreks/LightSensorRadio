package com.example.LightSensitiveRadio.LoundSound;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class MicSoundProvider {
    boolean isRecording = true;

    RecordedSoundValueHolder valueHolder = RecordedSoundValueHolder.getInstance();
    private LoudSoundListener loudSoundListener;
    AudioRecord record = null;

    public MicSoundProvider(){
        loudSoundListener = new LoudSoundListener();
    }

    public void initSoundDetection(Context c){

        int min = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        if (ActivityCompat.checkSelfPermission(c, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("permission", "permission not granted");
            return;
        }
        record = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, 8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
                min);
/*        if (AcousticEchoCanceler.isAvailable())
        {
            AcousticEchoCanceler echoCancler = AcousticEchoCanceler.create(record.getAudioSessionId());
            echoCancler.setEnabled(true);
        }*/

        (new Thread()
        {
            @Override
            public void run()
            {
                recordAndPlay();
            }
        }).start();
    }

    private void recordAndPlay()
    {
        //int size = 2048;
        int size = 1024;
        short[] lin = new short[size];
        record.startRecording();
        while (true)
        {
            if (isRecording)
            {
                record.read(lin, 0, size);
                Log.i("record", String.valueOf(findMaxShort(lin)));
                RecordedSoundHolder.addRecordedSound(String.valueOf(findMaxShort(lin)));

                valueHolder.updateCurrentSoundValues(findMaxShort(lin));
                loudSoundListener.checkSoundValues(valueHolder.getCurrentSoundValues());
            }
        }
    }

    private short findMaxShort(short[] list){
        short max = 0;
        for (short s : list) {
            if (Math.abs(s) > max) max = s;
        }
        return max;
    }
}