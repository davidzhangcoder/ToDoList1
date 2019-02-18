package com.todolist.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.todolist.App;
import com.todolist.EditToDoItemActivity;
import com.todolist.R;
import com.todolist.broadcast.ToDoListAlarmBroadCastReceiver;
import com.todolist.context.ContextHolder;
import com.todolist.db.ToDoItemDao;
import com.todolist.model.ToDoItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ToDoListAlarmService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Test
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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent toDoListAlarmServiceIntent = new Intent(this,ToDoListAlarmService.class);
            PendingIntent pendingIntent=PendingIntent.getService(this,0, toDoListAlarmServiceIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            App.getAlarmManager().setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + App.INITIAL_SECONDS_IN_MILLS , pendingIntent);
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent toDoListAlarmServiceIntent = new Intent(this,ToDoListAlarmService.class);
            PendingIntent pendingIntent=PendingIntent.getService(this,0, toDoListAlarmServiceIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            App.getAlarmManager().setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + App.INITIAL_SECONDS_IN_MILLS , pendingIntent);
        }

        doDectectToDoDue();

        //Test
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

    private void doDectectToDoDue()
    {
        Thread thread = new Thread( createToDoDueAlarmRunnable() );
        thread.start();
    }

    private Runnable createToDoDueAlarmRunnable()
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                Calendar current = Calendar.getInstance();
                long from = current.getTimeInMillis();

                current.add( Calendar.HOUR_OF_DAY  , 1);
                long to = current.getTimeInMillis();

                ToDoItemDao db = new ToDoItemDao(App.getContext());
                List<Map<String, String>> resultList = db.getListData( ToDoItem.TABLE_NAME , ToDoItem.COLUMN_DUE_TIMESTAMP + " between ? and ? " , new String[]{ String.valueOf(from) , String.valueOf(to) } , ToDoItem.COLUMN_DUE_TIMESTAMP + " ASC " );

                for( Map<String, String> map : resultList )
                {
                    if( map.get(ToDoItem.COLUMN_DUE_TIMESTAMP) != null && !map.get(ToDoItem.COLUMN_DUE_TIMESTAMP).trim().equalsIgnoreCase("") ) {
                        doAlarm( Long.parseLong(map.get(ToDoItem.COLUMN_DUE_TIMESTAMP)) , Long.parseLong(map.get(ToDoItem.COLUMN_ID)) );
                    }
                }

            }
        };

        return runnable;
    }

    private void doAlarm( long time , long id )
    {
        ToDoItem toDoItem = ToDoItem.getToDoItem( id );

        Intent intent = new Intent(App.getContext(),ToDoListAlarmBroadCastReceiver.class);
        intent.setAction( getResources().getString(R.string.alarmAction) );
//        intent.putExtra(EditToDoItemActivity.EDITTODOITEMACTIVITY_TODOITEM,toDoItem);

        Bundle bundle = new Bundle();
        bundle.putString("test" , "aaaaa");
        bundle.putSerializable( EditToDoItemActivity.EDITTODOITEMACTIVITY_TODOITEM , toDoItem );
        intent.putExtra("data",bundle);

//        intent.putExtra("test1" , "11111" );

        int uniqueInt = (int)toDoItem.getId(); //(int) (System.currentTimeMillis() & 0xfffffff);

//        if( isPendingIntentAvailable( App.getContext() , uniqueInt , intent ) )
//            return;

        PendingIntent pendingIntent=PendingIntent.getBroadcast(App.getContext(),uniqueInt, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        //Test
        Handler handler = new Handler( Looper.getMainLooper() );
        handler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        SimpleDateFormat dateFormatLong = new SimpleDateFormat("EEE MMM dd, yyyy hh:mm:ss", getResources().getConfiguration().locale);
                        String date = dateFormatLong.format( time );
                        Toast.makeText(App.getContext(), toDoItem + " " + pendingIntent +" " + date , Toast.LENGTH_LONG).show();
                    }
                }
        );


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            App.getAlarmManager().setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time , pendingIntent);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            App.getAlarmManager().setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        else
            App.getAlarmManager().set( AlarmManager.RTC_WAKEUP , time , pendingIntent );
    }

    public static boolean isPendingIntentAvailable(Context context , int requestCode , Intent i ) {
        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

}
