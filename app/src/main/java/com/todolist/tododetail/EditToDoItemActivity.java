package com.todolist.tododetail;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.maltaisn.recurpicker.Recurrence;
import com.maltaisn.recurpicker.RecurrenceFormat;
import com.maltaisn.recurpicker.RecurrencePickerDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.todolist.R;
import com.todolist.data.Injection;
import com.todolist.db.GenericDao;
import com.todolist.model.ToDoCategory;
import com.todolist.model.ToDoItem;
import com.todolist.ui.adapter.ToDoImageAdapter;
import com.todolist.ui.dialog.CategorySelectionDialog;
import com.todolist.util.AdsUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.zhihu.matisse.Matisse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by santa on 16/7/16.
 */
public class EditToDoItemActivity extends AppCompatActivity
        implements
        EditToDoItemContract.View,
        DatePickerDialog.OnDateSetListener ,
        TimePickerDialog.OnTimeSetListener ,
        RecurrencePickerDialog.RecurrenceSelectedCallback ,
        CategorySelectionDialog.CategorySelectedCallback
{

    public static String EDITTODOITEMACTIVITY_TODOITEM = "EDITTODOITEMACTIVITY_TODOITEM";

    public static final int REQUEST_CODE_CHOOSE_MATISSE=0;

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
    private MaterialEditText repeat;
    private MaterialEditText category;
    private View reback;

    private Recurrence selectedRecurrence;
    private ToDoCategory selectedToDoCategory;

    private RelativeLayout dueTimeContainer;
    private RelativeLayout repeatContainer;
    private RelativeLayout categoryContainer;

    private ImageView imageView;
    private ImageView dueDateImage;
    private ImageView dueTimeImage;
    private ImageView repeatImage;
    private ImageView categoryImage;
    private AdView mAdView;
    private RecyclerView imageRecylerView;

    private ToDoItem toDoItem;

    private Calendar selectedDate;

    private RecurrenceFormat formatter;
    private DateFormat dateFormatLong;

    private EditToDoItemContract.Presenter presenter;

    private InterstitialAd interstitialAd;

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
        categoryContainer = findViewById(R.id.categoryContainer);

        repeat = findViewById(R.id.repeatText);
        category = findViewById(R.id.categoryText);
        imageView = findViewById(R.id.edit_confirmation);
        dueDateImage = findViewById(R.id.dueDateImage);
        dueTimeImage = findViewById(R.id.dueTimeImage);
        repeatImage = findViewById(R.id.repeatImage);
        categoryImage = findViewById(R.id.categoryImage);
        reback = findViewById(R.id.edit_reback);
        mAdView = findViewById(R.id.adView);
        imageRecylerView = findViewById(R.id.imageRecyclerView);

        Intent i = getIntent();
        toDoItem = (ToDoItem)i.getSerializableExtra( EDITTODOITEMACTIVITY_TODOITEM );

        Bundle data = i.getBundleExtra("data");
        if( data != null )
            toDoItem = (ToDoItem)data.get(EditToDoItemActivity.EDITTODOITEMACTIVITY_TODOITEM);

        if( toDoItem == null ) {
            toDoItem = new ToDoItem();
            Calendar startDate = Calendar.getInstance();
            selectedDate = startDate;
            selectedRecurrence = new Recurrence(startDate.getTimeInMillis(), Recurrence.NONE);  // Does not repeat
            toDoItem.setRecurrencePeriod( Recurrence.NONE );
            toDoItem.setToDoCategory( ToDoCategory.getToDoCategory( ToDoCategory.CATEGORY_DEFAULT_ID ) );
            toDoItem.setDone(false);
            selectedToDoCategory = ToDoCategory.getToDoCategory( ToDoCategory.CATEGORY_DEFAULT_ID );
        }
        else {
            selectedDate = toDoItem.getDueDate();
            selectedRecurrence = new Recurrence( toDoItem.getDueDate().getTimeInMillis() , toDoItem.getRecurrencePeriod() );
            selectedToDoCategory = toDoItem.getToDoCategory();
        }

        Locale locale = getResources().getConfiguration().locale;
        dateFormatLong = new SimpleDateFormat("EEE MMM dd, yyyy", locale);  // Sun Dec 31, 2017
        formatter = new RecurrenceFormat(this, dateFormatLong);

        new EditToDoItemPresenter(Injection.provideToDoItemRepository(),this);

        init();

        //Ads
        displayBannerAds(mAdView);

        interstitialAd = AdsUtil.setupInterstitialAd(this);
    }

    private void displayBannerAds(AdView mAdView) {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        if(AdsUtil.displayBannerAds( this ))
            mAdView.setVisibility(View.VISIBLE);
        else
            mAdView.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onBackPressed() {
        if( AdsUtil.displayInterstitialAds(this) ) {
            if (interstitialAd != null && interstitialAd.isLoaded()) {
                interstitialAd.show();
            }
        }

        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.presenter.start();
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

                presenter.createOrUpdateToDoItem(toDoItem);

//                ContentValues values = new ContentValues();
//                values.put(ToDoItem.COLUMN_NAME, name);
//                values.put(ToDoItem.COLUMN_DONE_INDICATOR, toDoItem.isDone());
//                values.put(ToDoItem.COLUMN_DUE_TIMESTAMP, toDoItem.getDueTimestamp());
//                values.put(ToDoItem.COLUMN_RECURRENCE_PERIOD, toDoItem.getRecurrencePeriod());
//                values.put(ToDoItem.COLUMN_CATEGORY, toDoItem.getToDoCategory().getId() );
//
//                if( toDoItem.getId() == 0 ) {
//                    long id = db.addContent(ToDoItem.TABLE_NAME, values);
//                    toDoItem.setId(id);
//                }
//                else
//                {
//                    db.updateContent( ToDoItem.TABLE_NAME , values , ToDoItem.COLUMN_ID + " = ?" , new String[]{String.valueOf(toDoItem.getId())} );
//                }

                finish();
            }
        });

        if( materialEditTextToDoItemName != null ) {
            materialEditTextToDoItemName.setAccentTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            materialEditTextToDoItemName.setFocusFraction(1.0f);
            materialEditTextToDoItemName.setText(toDoItem.getName() == null ? "" : toDoItem.getName());

            materialEditTextToDoItemName.setOnTouchListener( new View.OnTouchListener() {
                @Override
                public boolean onTouch(View arg0, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_DOWN) {
                        resetFocusFraction();
                    }
                    return false;
                }
            } );
        }

        if( materialEditTextDueDate != null ) {
            materialEditTextDueDate.setAccentTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            materialEditTextDueDate.setFocusFraction(1.0f);
            dueDateImage.setColorFilter(getResources().getColor(R.color.colorPrimaryDark));
            if( toDoItem.getDueDate() != null ) {
                materialEditTextDueDate.setText(getDateString(toDoItem.getDueDate()));
            }
            materialEditTextDueDate.setInputType(InputType.TYPE_NULL );
            materialEditTextDueDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View arg0, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_DOWN) {
                        resetFocusFraction();
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
                    return false;
                }
            });
        }

        if( materialEditTextDueTime != null ) {
            materialEditTextDueTime.setAccentTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            materialEditTextDueTime.setFocusFraction(1.0f);
            dueTimeImage.setColorFilter(getResources().getColor(R.color.colorPrimaryDark));
            materialEditTextDueTime.setInputType( InputType.TYPE_NULL );
            materialEditTextDueTime.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View arg0, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_DOWN) {
                        resetFocusFraction();
                        Calendar now = selectedDate;

                        TimePickerDialog tpd = TimePickerDialog.newInstance(
                                EditToDoItemActivity.this,
                                now.get(Calendar.HOUR_OF_DAY),
                                now.get(Calendar.MINUTE),
                                false
                        );
                        tpd.show(EditToDoItemActivity.this.getFragmentManager(), "Datepickerdialog");
                    }
                    return false;
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
            repeat.setAccentTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            repeat.setFocusFraction(1.0f);
            repeatImage.setColorFilter(getResources().getColor(R.color.colorPrimaryDark));
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
                    resetFocusFraction();
                    // Set the settings
                    pickerDialog
                            .setEnabledModes(true , false)
                            .setShowHeaderInOptionList(false)
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

        if( category != null )
        {
            category.setAccentTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            category.setFocusFraction(1.0f);
            categoryImage.setColorFilter(getResources().getColor(R.color.colorPrimaryDark));

            category.setOnClickListener( new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {

                    CategorySelectionDialog categorySelectionDialog = new CategorySelectionDialog();
                    categorySelectionDialog.show(getSupportFragmentManager(), "categoryDialog");

                }
            });

            category.setText( selectedToDoCategory.getName() );

            if( toDoItem.getId() > 0 ) {
                categoryContainer.setVisibility( View.VISIBLE );
            }
        }

        if( imageRecylerView != null) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager( this , 3) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            imageRecylerView.setLayoutManager( gridLayoutManager );
            List<String> dataList = new ArrayList<String>();
            dataList.add("Add");
            dataList.add("Test");
            dataList.add("Test");
            dataList.add("Test");
            dataList.add("Test");
            ToDoImageAdapter toDoImageAdapter = new ToDoImageAdapter( this , dataList );
            imageRecylerView.setAdapter( toDoImageAdapter );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE_MATISSE && resultCode == RESULT_OK) {
            List<Uri> mSelected = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        dueTimeContainer.setVisibility( View.VISIBLE );
        repeatContainer.setVisibility( View.VISIBLE );
        categoryContainer.setVisibility( View.VISIBLE );
        resetFocusFraction();

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
        resetFocusFraction();

        String time = getTimeString( hourOfDay, minute );

        selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        selectedDate.set(Calendar.MINUTE, minute);
        selectedDate.set(Calendar.SECOND, second);

        toDoItem.setDueTimestamp( selectedDate.getTimeInMillis() );
        toDoItem.setDueDate( selectedDate );

        materialEditTextDueTime.setText(time);
    }

    private void resetFocusFraction()
    {
        materialEditTextToDoItemName.setFocusFraction(1.0f);
        materialEditTextDueTime.setFocusFraction(1.0f);
        materialEditTextDueDate.setFocusFraction(1.0f);
        repeat.setFocusFraction(1.0f);
        category.setFocusFraction(1.0f);
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
        resetFocusFraction();

        selectedRecurrence = r;
        String recurrenceValue = formatter.format(r);
        repeat.setText( recurrenceValue );

        toDoItem.setRecurrencePeriod( r.getPeriod() );
    }

    @Override
    public void onRecurrencePickerCancelled(Recurrence r) {

    }

    @Override
    public void onCategorySelected(ToDoCategory toDoCategory) {
        resetFocusFraction();

        selectedToDoCategory = toDoCategory;
        category.setText( toDoCategory.getName() );

        toDoItem.setToDoCategory( toDoCategory );
    }

    @Override
    public ToDoCategory getSelectedCategory() {
        return selectedToDoCategory;
    }

    @Override
    public void setPresenter(EditToDoItemContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showAfterCreateOrUpdateToDoItem() {
        finish();
    }
}
