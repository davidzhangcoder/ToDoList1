package com.todolist.broadcast;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.todolist.EditToDoItemActivity;
import com.todolist.R;
import com.todolist.model.ToDoItem;

import java.io.IOException;
import java.text.SimpleDateFormat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ToDoListAlarmBroadCastReceiver extends BroadcastReceiver{

    private MediaPlayer mMediaPlayer = null;

    // 状态栏提示要用的
    private NotificationManager m_Manager;
    private PendingIntent m_PendingIntent;
    private Notification m_Notification;

    @Override
    public void onReceive(Context context, Intent intent) {
        if( context.getResources().getString(R.string.alarmAction).equals(intent.getAction()) )
        {
            Log.i("AlarmBroadCastReceiver", "Run at: " );
            Toast.makeText(context, "ToDoListAlarmBroadCastReceiver - Alarm - 开启", Toast.LENGTH_LONG).show();

            Bundle data = intent.getBundleExtra("data");
            ToDoItem toDoItem = (ToDoItem)data.get(EditToDoItemActivity.EDITTODOITEMACTIVITY_TODOITEM);

            doNotify( context , toDoItem );
        }
    }

    private void doNotify(Context context , ToDoItem toDoItem) {

        try {

            AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

            // 使用来电铃声的铃声路径
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            // 如果为空，才构造，不为空，说明之前有构造过
            if(mMediaPlayer == null)
                mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(context, uri);
            mMediaPlayer.setLooping(true); //循环播放
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        // 等待3秒，震动3秒，从第0个索引开始，一直循环
        vibrator.vibrate(new long[]{1000, 3000}, 0);

//        // 无论是否震动、响铃，都有状态栏提示
//        m_Manager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
//        m_Manager.cancel(1023);
//        m_PendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, ToDoMainActivity.class), 0);
////        m_Notification = new Notification();
////        m_Notification.icon = R.drawable.ic_check_black_18dp;
////        m_Notification.tickerText = "随时情感助手在呼唤你……";
//////        m_Notification.setLatestEventInfo(mContext, "随时情感助手", intent.getExtras().getString("remind_kind", ""), m_PendingIntent);
////        m_Manager.notify(1023, m_Notification); // 这个notification 的 id 设为1023
//
//        Notification noti = new Notification.Builder(context)
//                .setContentTitle("New mail from " )
//                .setContentText("aaaaa")
//                .setSmallIcon(R.drawable.ic_check_black_18dp)
//                .setContentIntent( m_PendingIntent )
//                .build();
//        m_Manager.notify( 1023 , noti );

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            String channelId="chat";
            String channelName="聊天信息";
            int importance= NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(context,channelId,channelName,importance);

            channelId="subscribe";
            channelName="订阅消息";
            importance=NotificationManager.IMPORTANCE_DEFAULT;
            createNotificationChannel(context,channelId,channelName,importance);
        }

        sendChatMsg( context , toDoItem );

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(Context context , String channelId, String channelName, int importance) {
        NotificationChannel channel=new NotificationChannel(channelId,channelName,importance);
        channel.setShowBadge(true);//设置角标，默认是true
        NotificationManager manager=(NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
    }

    public void sendChatMsg(Context context , ToDoItem toDoItem) {

        NotificationManager manager=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//            NotificationChannel channel=manager.getNotificationChannel("chat");//因为是NotificationManager创建的Channel，所以通过mannager能获取
//            if(channel.getImportance()==NotificationManager.IMPORTANCE_NONE){
//                Intent intent=new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
//                intent.putExtra(Settings.EXTRA_APP_PACKAGE,getPackageName());
//                intent.putExtra(Settings.EXTRA_CHANNEL_ID,channel.getId());
//                startActivity(intent);
//                Toast.makeText(this,"请手动打开权限",Toast.LENGTH_SHORT).show();
//            }
        }

        Intent intent=new Intent(context,EditToDoItemActivity.class);

//        Bundle bundle = new Bundle();
//        bundle.putString("test" , "aaaaa");
//        bundle.putSerializable( EditToDoItemActivity.EDITTODOITEMACTIVITY_TODOITEM , toDoItem );
//        intent.putExtra("data",bundle);

        intent.putExtra( EditToDoItemActivity.EDITTODOITEMACTIVITY_TODOITEM , toDoItem );
//        intent.putExtra("data","今晚吃什么");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        String dateString = simpleDateFormat.format( toDoItem.getDueDate().getTime() );

        int uniqueInt = (int)toDoItem.getId();


//        PendingIntent pi=PendingIntent.getActivity(context,uniqueInt,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(EditToDoItemActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pi = stackBuilder.getPendingIntent(uniqueInt, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification= new NotificationCompat.Builder(context,"chat")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(toDoItem.getName())
                .setContentText(dateString)
                .setNumber(1)//设置角标的数量
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pi)
                .build();
        manager.notify(uniqueInt,notification);

    }


}
