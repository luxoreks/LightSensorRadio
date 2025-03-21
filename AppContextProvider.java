package com.example.LightSensitiveRadio;

import android.content.Context;

public class AppContextProvider {

    private MainActivity appContext;
    private static AppContextProvider instance;

    public static AppContextProvider getInstance(){
        if (instance == null){
            instance = new AppContextProvider();
        }
        return instance;
    }
    public Context getAppContext() {
        return appContext;
    }

    public MainActivity getMainActivity(){
        return appContext;
    }
    public void setAppContext(MainActivity newAppContext) {
            appContext = newAppContext;
    }
}