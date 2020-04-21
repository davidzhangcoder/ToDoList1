package com.todolist.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.maltaisn.recurpicker.Recurrence;
import com.todolist.app.App;
import com.todolist.data.Injection;
import com.todolist.data.model.ToDoItem;
import com.todolist.data.model.ToDoItemDatabase;
import com.todolist.data.source.GenericDataSource;
import com.todolist.data.source.ToDoItemDataSource;
import com.todolist.data.source.ToDoItemRepository;
import com.todolist.db.ToDoItemDao;
import com.todolist.util.AlarmUtil;
import com.todolist.util.DateUtil;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.Nullable;

public class ToDoListAlarmService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

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
//        Toast.makeText(App.getContext(), "开启 : ToDoListAlarmService - onStartCommand", Toast.LENGTH_LONG).show();

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
                Calendar fromCalendar = current;

                current.add( Calendar.SECOND  , App.INITIAL_SECONDS );
                long to = current.getTimeInMillis();
                Calendar toCalendar = Calendar.getInstance();
                toCalendar.set( current.get( Calendar.YEAR ) ,
                        current.get( Calendar.MONTH ) ,
                        current.get( Calendar.DATE ) ,
                        current.get( Calendar.HOUR_OF_DAY ) ,
                        current.get( Calendar.MINUTE ) ,
                        current.get( Calendar.SECOND )
                    );

//                ToDoItemDatabase.Companion.getInstance( ToDoListAlarmService.this ).getOpenHelper().getReadableDatabase();
//                ToDoItemDatabase.Companion.getInstance( ToDoListAlarmService.this ).beginTransaction();

                ToDoItemRepository toDoItemRepository = Injection.provideToDoItemRepository(App.getContext());
                ToDoItemDataSource.LoadToDoItemsCallBack callBack = new ToDoItemDataSource.LoadToDoItemsCallBack(){
                    @Override
                    public void onToDoItemsLoaded(List<ToDoItem> toDoItemList) {
                        for( ToDoItem toDoItem : toDoItemList ) {
                            if( toDoItem.getDueDate() != null && toDoItem.getDueTimestamp() != 0 && DateUtil.isHourAndMinutesInRange( toDoItem.getDueDate() , fromCalendar , toCalendar ) ) {
                                if( DateUtil.sameDay( toDoItem.getDueDate() , Calendar.getInstance() ) )
                                    AlarmUtil.doAlarm( toDoItem.getDueTimestamp() , toDoItem );
                                else if( ( toDoItem.getRecurrencePeriod() == Recurrence.DAILY )
                                        || ( toDoItem.getRecurrencePeriod() == Recurrence.WEEKLY && DateUtil.isSameWeekDay( toDoItem.getDueDate() , Calendar.getInstance() ) )
                                        || ( toDoItem.getRecurrencePeriod() == Recurrence.MONTHLY && DateUtil.isSameMonthDay( toDoItem.getDueDate() , Calendar.getInstance() ) )
                                        || ( toDoItem.getRecurrencePeriod() == Recurrence.YEARLY && DateUtil.isSameYearDay( toDoItem.getDueDate() , Calendar.getInstance() ) ) ) {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set( Calendar.HOUR_OF_DAY , toDoItem.getDueDate().get( Calendar.HOUR_OF_DAY ) );
                                    calendar.set( Calendar.MINUTE , toDoItem.getDueDate().get( Calendar.MINUTE ) );
                                    calendar.set( Calendar.SECOND , toDoItem.getDueDate().get( Calendar.SECOND ) );
                                    AlarmUtil.doAlarm( calendar.getTimeInMillis() , toDoItem );
                                }
                            }
                        }
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                };
                toDoItemRepository.getToDoItemsForAlarm( from , to , 0 , Recurrence.NONE , callBack );

//                ToDoItemDao toDoItemDao = new ToDoItemDao();
//                String sql = " ( ( " + ToDoItem.COLUMN_DUE_TIMESTAMP + " between ? and ? )  or ( " + ToDoItem.COLUMN_DUE_TIMESTAMP + " < ? and  " + ToDoItem.COLUMN_RECURRENCE_PERIOD + " != ? ) ) " +
//                        " and " + ToDoItem.COLUMN_DONE_INDICATOR + " == ? ";
//                String[] parameter = new String[]{ String.valueOf(from) , String.valueOf(to) , String.valueOf(from) , String.valueOf( Recurrence.NONE ) , String.valueOf( 0 )};
//                List<ToDoItem> toDoItemList = toDoItemDao.loadToDoItems( sql , parameter );

//                for( ToDoItem toDoItem : toDoItemList ) {
//                    if( toDoItem.getDueDate() != null && toDoItem.getDueTimestamp() != 0 && DateUtil.isHourAndMinutesInRange( toDoItem.getDueDate() , fromCalendar , toCalendar ) ) {
//                        if( DateUtil.sameDay( toDoItem.getDueDate() , Calendar.getInstance() ) )
//                            AlarmUtil.doAlarm( toDoItem.getDueTimestamp() , toDoItem );
//                        else if( ( toDoItem.getRecurrencePeriod() == Recurrence.DAILY )
//                                || ( toDoItem.getRecurrencePeriod() == Recurrence.WEEKLY && DateUtil.isSameWeekDay( toDoItem.getDueDate() , Calendar.getInstance() ) )
//                                || ( toDoItem.getRecurrencePeriod() == Recurrence.MONTHLY && DateUtil.isSameMonthDay( toDoItem.getDueDate() , Calendar.getInstance() ) )
//                                || ( toDoItem.getRecurrencePeriod() == Recurrence.YEARLY && DateUtil.isSameYearDay( toDoItem.getDueDate() , Calendar.getInstance() ) ) ) {
//                            Calendar calendar = Calendar.getInstance();
//                            calendar.set( Calendar.HOUR_OF_DAY , toDoItem.getDueDate().get( Calendar.HOUR_OF_DAY ) );
//                            calendar.set( Calendar.MINUTE , toDoItem.getDueDate().get( Calendar.MINUTE ) );
//                            calendar.set( Calendar.SECOND , toDoItem.getDueDate().get( Calendar.SECOND ) );
//                            AlarmUtil.doAlarm( calendar.getTimeInMillis() , toDoItem );
//                        }
//                    }
//                }

            }
        };

        return runnable;
    }

    public static boolean isPendingIntentAvailable(Context context , int requestCode , Intent i ) {
        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

}
