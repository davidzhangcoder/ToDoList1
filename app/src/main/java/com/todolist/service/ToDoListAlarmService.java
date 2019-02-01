package com.todolist.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.todolist.App;

public class ToDoListAlarmService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                for ( int i = 0 ; i < 1000 ; i++ ) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    Log.i("ToDoListAlarmService", "Run at: " + this.toString() + " " + i );
//                }
//            }
//        };
//
//        Thread thread = new Thread( runnable , "Test Thread" );
//        thread.start();
//
//
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Log.i("ToDoListAlarmService", "Run at: " + " Service " );


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent toDoListAlarmServiceIntent = new Intent(this,ToDoListAlarmService.class);
            intent.setAction("alarmAction");
            PendingIntent pendingIntent=PendingIntent.getService(this,0, toDoListAlarmServiceIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + App.INITIAL_SECONDS_IN_MILLS , pendingIntent);
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent toDoListAlarmServiceIntent = new Intent(this,ToDoListAlarmService.class);
            intent.setAction("alarmAction");
            PendingIntent pendingIntent=PendingIntent.getService(this,0, toDoListAlarmServiceIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + App.INITIAL_SECONDS_IN_MILLS , pendingIntent);
        }


        Toast.makeText(App.getContext(), "开启", Toast.LENGTH_LONG).show();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("ToDoListAlarmService", "onDestroy" );
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
