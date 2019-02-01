package com.todolist.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ToDoListAlarmBroadCastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if( "alarmAction".equals(intent.getAction()) )
        {
            Log.i("AlarmBroadCastReceiver", "Run at: " );
            Toast.makeText(context, "开启", Toast.LENGTH_LONG).show();
        }
    }
}
