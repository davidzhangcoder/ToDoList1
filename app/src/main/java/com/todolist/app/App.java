package com.todolist.app;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.MobileAds;
import com.todolist.R;
import com.todolist.context.ContextHolder;
import com.todolist.service.ToDoListAlarmService;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created
 */

public class App extends Application {

    private final int HOUR_IN_MILLS = 60 * 60 * 1000;

    public static final int INITIAL_SECONDS = 60;

    public static final int INITIAL_SECONDS_IN_MILLS = INITIAL_SECONDS * 1000;

    private static Context context;

    private static AlarmManager alarmManager;

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
//                .readTimeout(10000L, TimeUnit.MILLISECONDS)
//                //其他配置
//                .build();
//
//        OkHttpUtils.initClient(okHttpClient);

        Stetho.initializeWithDefaults(this);

        context = getApplicationContext();

        appComponent = DaggerAppComponent
                .builder()
                .appModule( new AppModule( this ) )
                .build();

        //Test
        Log.i("App", "================================" + appComponent.getTestA());

        init();

    }

    private void doDueAlarmService() {
        //Alarm Service
        Calendar calendar =Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(System.currentTimeMillis());

        Intent intent = new Intent(this,ToDoListAlarmService.class);
//        intent.setAction("alarmAction");
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

    public void init()
    {
        ContextHolder.initial(this);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        doDueAlarmService();

        MobileAds.initialize(this, getResources().getString(R.string.admob_app_id));
    }

    public static AlarmManager getAlarmManager() {
        return alarmManager;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
