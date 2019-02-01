package com.todolist;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import com.todolist.broadcast.ToDoListAlarmBroadCastReceiver;
import com.todolist.service.ToDoListAlarmService;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by wanglei on 2016/12/9.
 */

public class App extends Application {

    private final int HOUR_IN_MILLS = 60 * 60 * 1000;

    public static final int INITIAL_SECONDS_IN_MILLS = 10 * 1000;

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

        context = getApplicationContext();

        //Alarm Service
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar =Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(System.currentTimeMillis());

        Intent intent = new Intent(this,ToDoListAlarmService.class);
        intent.setAction("alarmAction");
        PendingIntent pendingIntent=PendingIntent.getService(context,0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        alarmManager.set( AlarmManager.RTC_WAKEUP , calendar.getTimeInMillis() + HOUR_IN_MILLS , pendingIntent );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + INITIAL_SECONDS_IN_MILLS , pendingIntent);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + INITIAL_SECONDS_IN_MILLS, pendingIntent);
        else
            alarmManager.setRepeating( AlarmManager.RTC_WAKEUP , calendar.getTimeInMillis() + INITIAL_SECONDS_IN_MILLS , INITIAL_SECONDS_IN_MILLS , pendingIntent );

    }

    public static Context getContext() {
        return context;
    }
}
