package com.example.parrottalker.utils;

import android.app.Application;
import android.content.Context;

import com.karumi.dexter.Dexter;

public class App extends Application{

    static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Dexter.initialize(this);
    }
    public static Context get() {
        return instance == null ? (instance = new App()): instance;
    }


}
