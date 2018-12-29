package com.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.todolist.db.ToDoItemDao;
import com.todolist.model.ToDoItem;
import com.todolist.util.KnifeKit;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;


/**
 * Created by santa on 16/7/16.
 */
public class EditToDoItemActivity extends AppCompatActivity
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

    private MaterialEditText materialEditText;

    private ToDoItem toDoItem;

    private ToDoItemDao db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipedit);

//        KnifeKit.bind(this);

        materialEditText = (MaterialEditText) findViewById(R.id.dueDate);

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

        if( materialEditText != null ) {
            materialEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            null,
                            now.get(Calendar.YEAR), // Initial year selection
                            now.get(Calendar.MONTH), // Initial month selection
                            now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                    );
// If you're calling this from a support Fragment
//                    dpd.show(getFragmentManager(), "Datepickerdialog");
// If you're calling this from an AppCompatActivity
                    dpd.show(EditToDoItemActivity.this.getFragmentManager(), "Datepickerdialog");
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

}
