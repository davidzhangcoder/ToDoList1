package com.todolist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.todolist.db.ToDoItemDao;
import com.todolist.model.ToDoItem;
import com.todolist.util.KnifeKit;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;


/**
 * Created by santa on 16/7/16.
 */
public class EditToDoItemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener
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

    private RelativeLayout dueTimeContainer;

    private ToDoItem toDoItem;

    private ToDoItemDao db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipedit);

//        KnifeKit.bind(this);

        materialEditTextDueDate = (MaterialEditText) findViewById(R.id.dueDate);
        materialEditTextDueTime = (MaterialEditText) findViewById(R.id.dueTime);
        dueTimeContainer = (RelativeLayout) findViewById(R.id.dueTimeContainer);

        Intent i = getIntent();
        EditText editText = (EditText) findViewById(R.id.edit_content);
        editText.setText(i.getStringExtra("content"));
        toDoItem = (ToDoItem)i.getSerializableExtra( EDITTODOITEMACTIVITY_TODOITEM );

        View reback = findViewById(R.id.edit_reback);
        reback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        db = new ToDoItemDao(this);

        init();
    }

    private void init()
    {

        if( materialEditTextDueDate != null ) {
            materialEditTextDueDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Calendar now = Calendar.getInstance();
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

//                    Calendar now = Calendar.getInstance();
//                    android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
//                            EditToDoItemActivity.this,
//                            (view1, year, month, dayOfMonth) -> Log.d("Orignal", "Got clicked"),
//                            now.get(Calendar.YEAR),
//                            now.get(Calendar.MONTH),
//                            now.get(Calendar.DAY_OF_MONTH)
//                    );
//                    datePickerDialog.show();

                }
            });
        }

        if( materialEditTextDueTime != null ) {
            materialEditTextDueTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Calendar now = Calendar.getInstance();
                    TimePickerDialog tpd = TimePickerDialog.newInstance(
                            EditToDoItemActivity.this, false
                    );
                    tpd.show(EditToDoItemActivity.this.getFragmentManager(), "Datepickerdialog");

                }
            });
        }

//        if( dueDatePopuTextView != null )
//        {
//            dueDatePopuTextView.setActionListener(new PopuTextView.ActionListener() {
//                @Override
//                public void doSave(long time) {
//                    toDoItem.setDueTimestamp( time );
//                    ContentValues values = new ContentValues();
//                    values.put(ToDoItem.COLUMN_DUE_TIMESTAMP, toDoItem.getDueTimestamp());
//                    db.updateContent( ToDoItem.TABLE_NAME , values , ToDoItem.COLUMN_ID + " = ?" , new String[]{String.valueOf(toDoItem.getId())} );
//                }
//            });
//        }
//
//        if( remindDatePopuTextView != null )
//        {
//            remindDatePopuTextView.setActionListener(new PopuTextView.ActionListener() {
//                @Override
//                public void doSave(long time) {
//                    toDoItem.setRemindTimestamp( time );
//                    ContentValues values = new ContentValues();
//                    values.put(ToDoItem.COLUMN_REMIND_TIMESTAMP, toDoItem.getRemindTimestamp());
//                    db.updateContent( ToDoItem.TABLE_NAME , values , ToDoItem.COLUMN_ID + " = ?" , new String[]{String.valueOf(toDoItem.getId())} );
//                }
//            });
//        }
//
//        editAddChild.setOnEditorActionListener( addChildTaskOnEditorActionListener );
//
//        editAddRemark.setOnEditorActionListener( addRemarkOnEditorActionListener );
    }

//    private TextView.OnEditorActionListener addChildTaskOnEditorActionListener = new TextView.OnEditorActionListener() {
//        @Override
//        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//            if( i == EditorInfo.IME_ACTION_DONE)
//            {
//                toDoItem.setChildTask( (String)textView.getText().toString() );
//                ContentValues values = new ContentValues();
//                values.put(ToDoItem.COLUMN_CHILD_TASK, toDoItem.getChildTask());
//                db.updateContent( ToDoItem.TABLE_NAME , values , ToDoItem.COLUMN_ID + " = ?" , new String[]{String.valueOf(toDoItem.getId())} );
//            }
//            return false;
//        }
//    };
//
//    private TextView.OnEditorActionListener addRemarkOnEditorActionListener = new TextView.OnEditorActionListener() {
//        @Override
//        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//            if( i == EditorInfo.IME_ACTION_DONE)
//            {
//                toDoItem.setRemark( (String)textView.getText().toString() );
//                ContentValues values = new ContentValues();
//                values.put(ToDoItem.COLUMN_REMARK, toDoItem.getRemark());
//                db.updateContent( ToDoItem.TABLE_NAME , values , ToDoItem.COLUMN_ID + " = ?" , new String[]{String.valueOf(toDoItem.getId())} );
//            }
//            return false;
//        }
//    };

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        dueTimeContainer.setVisibility( View.VISIBLE );
        String date = dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        materialEditTextDueDate.setText( date );
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String time = "h"+minute+"m"+second;
        materialEditTextDueTime.setText(time);
    }
}
