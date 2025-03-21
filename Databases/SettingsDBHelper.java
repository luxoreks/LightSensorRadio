package com.example.LightSensitiveRadio.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SettingsDBHelper extends SQLiteOpenHelper {

    public static final String COLUMN_BRIGHTNESS_THRESHOLD = "brightness_threshold";
    public static final String COLUMN_SONGS_WITHOUT_REPETITION = "songs_without_repetition";
    public static final String COLUMN_SONGS_BEFORE_AD = "songs_before_ad";
    public static final String COLUMN_ADS_WITHOUT_REPETITION= "ads_without_repetition";
    public static final String COLUMN_DAY_MODE_HOUR = "day_mode_hour";
    public static final String COLUMN_DAY_MODE_MINUTE = "day_mode_minute";
    public static final String COLUMN_NIGHT_MODE_HOUR = "night_mode_hour";
    public static final String COLUMN_NIGHT_MODE_MINUTE = "night_mode_minute";

    private static final String TAG = "SettingsDBHelper";
    private static final String DATABASE_NAME = "kibelSoundSettings.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "settingsValues";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_VALUE_NAME = "mode_name";
    private static final String COLUMN_VALUE= "is_mode_on";

    public SettingsDBHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_VALUE_NAME + " TEXT, "
                + COLUMN_VALUE + " INTEGER)";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean isDataBaseEmpty(){
        Cursor c = this.readAllData();
        boolean isEmpty = c.getCount() == 0;
        return isEmpty;
    }

    public Cursor readAllData()
    {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null)
        {
            cursor = db.rawQuery(query, null);
        }
        return  cursor;
    }

    public void addSettingsValue(String settingsValueName, int settingsValue){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_VALUE_NAME, settingsValueName);
        cv.put(COLUMN_VALUE, settingsValue);

        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    public void updateSettingsValue(String settingsValueName, int settingsValue){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_VALUE_NAME, settingsValueName);
        cv.put(COLUMN_VALUE, settingsValue);

        String queryForId = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_VALUE_NAME + " = " + "\"" + settingsValueName + "\"";
        System.out.println(queryForId);
        Cursor c = db.rawQuery(queryForId, null);
        c.moveToFirst();
        String id = c.getString(0);
        db.update(TABLE_NAME, cv, "_id = ?", new String[]{id});
        c.close();
    }

    public int getSettingsValue(String settingsValueName){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_VALUE_NAME + " = " + "\"" + settingsValueName + "\"";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int settingValue = c.getInt(2);
        c.close();
        return settingValue;
    }
}