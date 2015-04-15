package net.ecoarttech.edibleecologies;

import android.app.Application;
import android.content.Context;

import net.ecoarttech.edibleecologies.util.SharedPrefs;

import java.lang.ref.WeakReference;

/**
 * Created by pkoronkevich on 4/10/15.
 */
public class App extends Application{

    private static WeakReference<App> sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = new WeakReference<>(this);
        SharedPrefs.initDefaultQuestions();
    }

    public static Context getContext(){
        return sApp.get().getApplicationContext();
    }
}
