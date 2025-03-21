package com.example.LightSensitiveRadio.startScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.LightSensitiveRadio.MainActivity;
import com.example.LightSensitiveRadio.R;
import com.example.LightSensitiveRadio.ServerAddressHolder;

import java.io.File;

public class StartingActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 42069;
    private static final String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_MEDIA_AUDIO,
            android.Manifest.permission.RECORD_AUDIO};
    private static final int NECESSARY_AMOUNT_OF_SONGS = 5;
    private static final int NUMBER_OF_DIRECTORIES = 2;

    private String errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        errorMessage = "Nie spełniono warunków: \n";


        TextView messageHolder = findViewById(R.id.messageHolder);
        messageHolder.setText(TextProvider.getStaringMessage());

        Button buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
//                AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
//                dialog.setMessage(errorMessage);
//                dialog.setCancelable(true);
//                dialog.create().show();
            }
        });

    }

    private void requestPermission() {
        Log.i("main", "request permission entered");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i("main", "if enetered");
            requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
        } else {
            Log.i("main", "else enetered");
            // do your code if permission granted
            Intent mainActivity = new Intent(StartingActivity.this, MainActivity.class);
            finish();
            startActivity(mainActivity);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("main", "on permission result");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                Log.i("main", "case enetred");
                if (grantResults.length > 0 && permissions[0].equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) && permissions[1].equals(android.Manifest.permission.READ_EXTERNAL_STORAGE) && permissions[2].equals(Manifest.permission.READ_MEDIA_AUDIO)) {
                    // check whether storage permission granted or not.
                    Log.i("main", "if 1");
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        // do you stuff if your permission is granted
                        Log.i("main", "if 2");
                        if (areNecessaryFilesCreated()){
                            Intent mainActivity = new Intent(StartingActivity.this, MainActivity.class);
                            //Intent mainActivity = new Intent(StartingActivity.this, FoldersCreatorActivity.class);
                            finish();
                            startActivity(mainActivity);
                        }
                        else{
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
                            dialog.setMessage(errorMessage);
                            dialog.setCancelable(true);
                            dialog.create().show();
                        }
                    }
                }
            default:
                break;
        }
    }

    private boolean areNecessaryFilesCreated(){
        boolean areFilesCreated = sfxDirectoryExist() && pzgDirectoryExist() && hasNecessaryAmountOfSongs();
        return true;
    }
    private boolean sfxDirectoryExist(){
        File sfxDirectory = new File(ServerAddressHolder.filePath);
        if (!sfxDirectory.exists()){
            errorMessage += "Brak folderu o nazwie \"sfx\" (lub zotsał utworzony w złym miejscu) \n";
        }
        return sfxDirectory.exists();
    }

    private boolean pzgDirectoryExist(){
        File pzgDirectory = new File(ServerAddressHolder.filePath + "pzg");
        if (!pzgDirectory.exists()){
            errorMessage += "Brak folderu o nazwie \"pzg\" (lub zotsał utworzony w złym miejscu) \n";
        }
        return pzgDirectory.exists();
    }

    private boolean hasNecessaryAmountOfSongs(){
        File sfxDirectory = new File(ServerAddressHolder.filePath);
        String[] filenames = sfxDirectory.list();
        if (filenames.length > (NECESSARY_AMOUNT_OF_SONGS + NUMBER_OF_DIRECTORIES)){
            errorMessage += "Brak odpowiedniej ilości plików audio w folderze \"sfx\" \n";
        }
        return filenames.length > (NECESSARY_AMOUNT_OF_SONGS + NUMBER_OF_DIRECTORIES);
    }
}