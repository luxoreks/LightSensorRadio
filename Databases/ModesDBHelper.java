package com.example.LightSensitiveRadio.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ModesDBHelper extends SQLiteOpenHelper {

    public static final String NIGHT_MODE = "night_mode";
    public static final String PLAY_ADS_MODE = "play_ads";
    public static final String DETECT_HOT_WORD = "detect_hot_word";

    private static final String TAG = "ModesDBHelper";
    private static final String DATABASE_NAME = "kibelSoundModes.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "modes";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_MODE_NAME = "mode_name";
    private static final String COLUMN_IS_MODE_ON = "is_mode_on";


    public ModesDBHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MODE_NAME + " TEXT, "
                + COLUMN_IS_MODE_ON + " INTEGER)";

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

    public void addMode(String modeName, boolean isModeOn){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        int isModeOnValForDB = isModeOn? 1:0;
        cv.put(COLUMN_MODE_NAME, modeName);
        cv.put(COLUMN_IS_MODE_ON, isModeOnValForDB);

        //long result = db.insert(TABLE_NAME, null, cv);
        db.insert(TABLE_NAME, null, cv);
        db.close();
        /*
        if (result == -1)
        {
            Toast.makeText(context, "nie udało się", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "udało się", Toast.LENGTH_SHORT).show();
        }
         */
    }

    public void updateModeState(String moduleName, boolean isModeOn){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MODE_NAME, moduleName);
        cv.put(COLUMN_IS_MODE_ON, isModeOn);

        String queryForId = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_MODE_NAME + " = " + "\"" + moduleName + "\"";
        System.out.println(queryForId);
        Cursor c = db.rawQuery(queryForId, null);
        c.moveToFirst();
        String id = c.getString(0);
        db.update(TABLE_NAME, cv, "_id = ?", new String[]{id});
        c.close();
    }

    public boolean isModeActive(String moduleName){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_MODE_NAME + " = " + "\"" + moduleName + "\"";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int isModeTurnedOn = c.getInt(2);
        boolean isModeOn = isModeTurnedOn == 1;
        c.close();
        return isModeOn;
    }
}