package apsupportapp.aperotechnologies.com.designapp;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import java.util.HashMap;


public class BaseLifeCycleCallbacks implements Application.ActivityLifecycleCallbacks {

    static HashMap<String, Integer> activities;
    SharedPreferences sharedPreferences;
    Activity activity;
    BaseLifeCycleCallbacks() {
        activities = new HashMap<String, Integer>();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        this.activity = activity;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        Log.e("onActivityCreated", "onRestart: ");

    }

    @Override
    public void onActivityStarted(Activity activity) {

        activities.put(activity.getLocalClassName(), 1);
        applicationStatus();
    }

    @Override
    public void onActivityResumed(Activity activity) {

        Log.e("onActivityResumed", "onRestart: ");
        if (LocalNotificationReceiver.logoutAlarm) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            activity.finish();
            Intent i = new Intent(activity, LoginActivity.class);
            // set the new task and clear flags
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(i);
        }
    }


    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        //map Activity unique class name with 0 on foreground
        Log.e("onActivityStopped", "onStop: ");

        activities.put(activity.getLocalClassName(), 0);
        applicationStatus();
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    /**
     * Check if any activity is in the foreground
     */
    private static boolean isBackGround() {
        for (String s : activities.keySet()) {
            if (activities.get(s) == 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Log application status.
     */
    public static boolean applicationStatus() {


        Log.e("ApplicationStatus", "Is application background " + isBackGround());
        return !isBackGround();
    }
}
