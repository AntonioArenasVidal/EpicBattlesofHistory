package edu.fsu.cs.epicbattlesofhistory;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MyMediaService extends Service {

    public static MediaPlayer mp = null;

    private static final String ACTION_PLAY_EPIC = "PLAY_EPIC";
    private static final String ACTION_PLAY_NEUTRAL = "PLAY_NEUTRAL";
    private static final String ACTION_PAUSE = "PAUSE";
    private static final String ACTION_STOP = "STOP";

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {

        final String action = intent.getAction();

        Log.d("Here", "here");

        if(ACTION_PLAY_EPIC.equals(action) || ACTION_PLAY_NEUTRAL.equals(action)) {
            Log.d("Here1", "here");

            if(mp == null) {
                Log.d("Here2", "here");

                if(ACTION_PLAY_EPIC.equals(action))
                    mp = MediaPlayer.create(this, R.raw.epic);
                else if(ACTION_PLAY_NEUTRAL.equals(action))
                    mp = MediaPlayer.create(this, R.raw.neutral);
                mp.start();
            }
            else {
                mp.release();
                mp = null;

                if(ACTION_PLAY_EPIC.equals(action))
                    mp = MediaPlayer.create(this, R.raw.epic);
                else if(ACTION_PLAY_NEUTRAL.equals(action))
                    mp = MediaPlayer.create(this, R.raw.neutral);
                mp.start();
            }

        }

        if(ACTION_PAUSE.equals(action)) {
            if(mp != null) {
                mp.pause();
            }
        }
        else if(ACTION_STOP.equals(action)) {
            if(mp != null){
                mp.release();
                mp = null;
            }
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
    }

}
