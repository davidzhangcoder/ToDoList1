package com.todolist.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.todolist.R;
import com.todolist.app.App;
import com.todolist.broadcast.ToDoListAlarmBroadCastReceiver;
import com.todolist.model.ToDoItem;
import com.todolist.tododetail.EditToDoItemActivity;

import java.text.SimpleDateFormat;

public class AlarmUtil {

    public static void doAlarm( long time , ToDoItem toDoItem )
    {
//        ToDoItem toDoItem = null;//ToDoItem.getToDoItem( id );

        Intent intent = new Intent(App.getContext(),ToDoListAlarmBroadCastReceiver.class);
        intent.setAction( App.getContext().getResources().getString(R.string.alarmAction) );
//        intent.putExtra(EditToDoItemActivity.EDITTODOITEMACTIVITY_TODOITEM,toDoItem);

        Bundle bundle = new Bundle();
        bundle.putString("test" , "aaaaa");
        bundle.putParcelable( EditToDoItemActivity.EDITTODOITEMACTIVITY_TODOITEM , toDoItem );
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
                        SimpleDateFormat dateFormatLong = new SimpleDateFormat("EEE MMM dd, yyyy hh:mm:ss", App.getContext().getResources().getConfiguration().locale);
                        String date = dateFormatLong.format( time );
                        //Toast.makeText(App.getContext(), toDoItem + " " + pendingIntent +" " + date , Toast.LENGTH_LONG).show();
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

}
