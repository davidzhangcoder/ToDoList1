package com.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maltaisn.recurpicker.Recurrence;
import com.maltaisn.recurpicker.RecurrenceFormat;
import com.maltaisn.recurpicker.RecurrencePickerDialog;
import com.maltaisn.recurpicker.RecurrencePickerView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.todolist.db.ToDoItemDao;
import com.todolist.model.ToDoItem;
import com.todolist.util.KnifeKit;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by santa on 16/7/16.
 */
public class EditToDoItemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener , RecurrencePickerDialog.RecurrenceSelectedCallback
{

    public static String EDITTODOITEMACTIVITY_TODOITEM = "EDITTODOITEMACTIVITY_TODOITEM";

//    @BindView(R.id.dueDate)
//    PopuTextView dueDatePopuTextView;
//
//    @BindView(R.id.remindDate)
//    PopuTextView remindDatePopuTextView;
//
//    @BindView(R.id.edit_addchild)
//    EditText editAddChild;
//
//    @BindView(R.id.edit_addremark)
//    EditText editAddRemark;

    private MaterialEditText materialEditTextDueDate;
    private MaterialEditText materialEditTextDueTime;
    private MaterialEditText materialEditTextToDoItemName;
    private View reback;

    private TextView repeat;
    private Recurrence selectedRecurrence;

    private RelativeLayout dueTimeContainer;
    private RelativeLayout repeatContainer;

    private ImageView imageView;

    private ToDoItem toDoItem;
    private ToDoItemDao db;

    private Calendar selectedDate;

    private RecurrenceFormat formatter;
    private DateFormat dateFormatLong;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipedit);

//        KnifeKit.bind(this);

        materialEditTextDueDate = findViewById(R.id.dueDate);
        materialEditTextDueTime = findViewById(R.id.dueTime);
        materialEditTextToDoItemName = findViewById(R.id.toDoItemName);
        dueTimeContainer = findViewById(R.id.dueTimeContainer);
        repeatContainer = findViewById(R.id.repeatContainer);
        repeat = findViewById(R.id.repeatText);
        imageView = findViewById(R.id.edit_confirmation);

        Intent i = getIntent();
        toDoItem = (ToDoItem)i.getSerializableExtra( EDITTODOITEMACTIVITY_TODOITEM );
        if( toDoItem == null ) {
            toDoItem = new ToDoItem();
            Calendar startDate = Calendar.getInstance();
            selectedDate = startDate;
            selectedRecurrence = new Recurrence(startDate.getTimeInMillis(), Recurrence.NONE);  // Does not repeat
            toDoItem.setRecurrencePeriod( Recurrence.NONE );
        }
        else {
            selectedDate = toDoItem.getDueDate();
            selectedRecurrence = new Recurrence( toDoItem.getDueDate().getTimeInMillis() , toDoItem.getRecurrencePeriod() );
        }

        reback = findViewById(R.id.edit_reback);

        db = new ToDoItemDao(this);

        Locale locale = getResources().getConfiguration().locale;
        dateFormatLong = new SimpleDateFormat("EEE MMM dd, yyyy", locale);  // Sun Dec 31, 2017
        formatter = new RecurrenceFormat(this, dateFormatLong);

        init();
    }

    private void init()
    {
        reback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                toDoItem.setName("");
//                toDoItem.setDueTimestamp(0);

                //Add item into "To Do" List
                String name = materialEditTextToDoItemName.getText().toString();
                toDoItem.setName( name );

                ContentValues values = new ContentValues();
                values.put(ToDoItem.COLUMN_NAME, name);
                values.put(ToDoItem.COLUMN_DONE_INDICATOR, toDoItem.isDone());
                values.put(ToDoItem.COLUMN_DUE_TIMESTAMP, toDoItem.getDueTimestamp());
                values.put(ToDoItem.COLUMN_RECURRENCE_PERIOD, toDoItem.getRecurrencePeriod());

                if( toDoItem.getId() == 0 ) {
                    long id = db.addContent(ToDoItem.TABLE_NAME, values);
                    toDoItem.setId(id);
                }
                else
                {
                    db.updateContent( ToDoItem.TABLE_NAME , values , ToDoItem.COLUMN_ID + " = ?" , new String[]{String.valueOf(toDoItem.getId())} );
                }

                finish();
            }
        });

        if( materialEditTextToDoItemName != null )
            materialEditTextToDoItemName.setText( toDoItem.getName()==null?"":toDoItem.getName() );

        if( materialEditTextDueDate != null ) {
            if( toDoItem.getDueDate() != null ) {
                materialEditTextDueDate.setText(getDateString(toDoItem.getDueDate()));
            }
            materialEditTextDueDate.setInputType(InputType.TYPE_NULL );
            materialEditTextDueDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar now = selectedDate;

                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            EditToDoItemActivity.this,
                            now.get(Calendar.YEAR), // Initial year selection
                            now.get(Calendar.MONTH), // Initial month selection
                            now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                    );
// If you're calling this from a support Fragment
//                    dpd.show(getFragmentManager(), "Datepickerdialog");
// If you're calling this from an AppCompatActivity
//                    dpd.setAccentColor(Color.parseColor("#9C27B0"));
                    dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                    dpd.show(EditToDoItemActivity.this.getFragmentManager(), "Datepickerdialog");
                }
            });
        }

        if( materialEditTextDueTime != null ) {
            materialEditTextDueTime.setInputType( InputType.TYPE_NULL );
            materialEditTextDueTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Calendar now = selectedDate;

                    TimePickerDialog tpd = TimePickerDialog.newInstance(
                            EditToDoItemActivity.this,
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            false
                    );
                    tpd.show(EditToDoItemActivity.this.getFragmentManager(), "Datepickerdialog");

                }
            });

            if( toDoItem.getDueDate() != null ) {
                dueTimeContainer.setVisibility(View.VISIBLE);
                int hour = toDoItem.getDueDate().get(Calendar.HOUR_OF_DAY);
                int minute = toDoItem.getDueDate().get(Calendar.MINUTE);
                materialEditTextDueTime.setText( getTimeString( hour , minute ) );
            }
        }

        if( repeat != null ) {
            Locale locale = getResources().getConfiguration().locale;
            final DateFormat dateFormatShort = new SimpleDateFormat("dd-MM-yyyy", locale);  // 31-12-2017

            Calendar startDate = selectedDate;

//            if( selectedRecurrence == null )
//                selectedRecurrence = new Recurrence(startDate.getTimeInMillis(), Recurrence.NONE);  // Does not repeat

            // Set up dialog recurrence picker
            final RecurrencePickerDialog pickerDialog = new RecurrencePickerDialog();
            pickerDialog.setDateFormat(dateFormatShort, dateFormatLong);
            repeat.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Set the settings
                    pickerDialog
                            .setEnabledModes(true , false)
                            .setShowHeaderInOptionList(true)
                            .setShowDoneButtonInOptionList(true)
                            .setShowCancelButton(true)
                            .setRecurrence(selectedRecurrence, startDate.getTimeInMillis());

                    // Not necessary, but if a cancel button is shown, often dialog isn't cancelable
                    pickerDialog.setCancelable(true);

                    // Show the recurrence dialog
                    pickerDialog.show(getSupportFragmentManager(), "recur_picker_dialog");
                }
            } );

            String recurrenceValue = formatter.format( selectedRecurrence );
            repeat.setText( recurrenceValue );

            if( toDoItem.getId() > 0 ) {
                repeatContainer.setVisibility( View.VISIBLE );
            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        dueTimeContainer.setVisibility( View.VISIBLE );
        repeatContainer.setVisibility( View.VISIBLE );

        Calendar calendar = Calendar.getInstance();
        calendar.set(year,monthOfYear,dayOfMonth);
        String date = getDateString( calendar );
        selectedDate = calendar;

        if( toDoItem.getDueDate() != null ) {
            int hourOfDay = toDoItem.getDueDate().get( Calendar.HOUR_OF_DAY );
            int minute = toDoItem.getDueDate().get( Calendar.MINUTE );
            selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selectedDate.set(Calendar.MINUTE, minute);
        }

        toDoItem.setDueTimestamp( selectedDate.getTimeInMillis() );
        toDoItem.setDueDate( selectedDate );

        materialEditTextDueDate.setText( date );

        String time = getTimeString( toDoItem.getDueDate().get( Calendar.HOUR_OF_DAY ), toDoItem.getDueDate().get( Calendar.MINUTE ) );
        materialEditTextDueTime.setText(time);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second)
    {
        String time = getTimeString( hourOfDay, minute );

        selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        selectedDate.set(Calendar.MINUTE, minute);
        selectedDate.set(Calendar.SECOND, second);

        toDoItem.setDueTimestamp( selectedDate.getTimeInMillis() );
        toDoItem.setDueDate( selectedDate );

        materialEditTextDueTime.setText(time);
    }

    private String getDateString( Calendar calendar )
    {
        Locale locale = getResources().getConfiguration().locale;
        SimpleDateFormat dateFormatLong = new SimpleDateFormat("EEE MMM dd, yyyy", locale);
        String date = dateFormatLong.format( calendar.getTime() );
        return date;
    }

    private String getTimeString( int hourOfDay, int minute ) {
        boolean isAM = hourOfDay<12?true:false;
        String time = (hourOfDay>12?hourOfDay-12:hourOfDay) + ":" + minute + " " + (isAM?"AM":"PM");
        return time;
    }

    @Override
    public void onRecurrencePickerSelected(Recurrence r) {
        selectedRecurrence = r;
        String recurrenceValue = formatter.format(r);
        repeat.setText( recurrenceValue );

        toDoItem.setRecurrencePeriod( r.getPeriod() );
    }

    @Override
    public void onRecurrencePickerCancelled(Recurrence r) {

    }
}
