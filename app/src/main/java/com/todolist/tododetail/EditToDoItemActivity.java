package com.todolist.tododetail;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.maltaisn.recurpicker.Recurrence;
import com.maltaisn.recurpicker.RecurrenceFormat;
import com.maltaisn.recurpicker.RecurrencePickerDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.todolist.R;
import com.todolist.data.Injection;
import com.todolist.data.model.ToDoCategory;
import com.todolist.data.model.ToDoImage;
import com.todolist.data.model.ToDoItem;
import com.todolist.ui.GridItemDecoration;
import com.todolist.ui.adapter.ToDoImageAdapter;
import com.todolist.ui.dialog.CategorySelectionDialog;
import com.todolist.util.AdsUtil;
import com.todolist.util.AlarmUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created
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

    public static final int MAX_IMAGE_COUNT = 6;

    private final int REQUEST_CODE_CHOOSE=0;

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
    private RelativeLayout doneContainer;

    private ImageView imageView;
    private ImageView dueDateImage;
    private ImageView dueTimeImage;
    private ImageView repeatImage;
    private ImageView categoryImage;
    private AdView mAdView;
    private RecyclerView imageRecylerView;
    private SwitchCompat isdoneSwitchCompat;

    private ToDoItem toDoItem;

    private Calendar selectedDate;

    private RecurrenceFormat formatter;
    private DateFormat dateFormatLong;

    private EditToDoItemContract.Presenter presenter;

    private InterstitialAd interstitialAd;

    private ArrayList<ToDoImage> imageDataList = new ArrayList<ToDoImage>();
    private ToDoImageAdapter toDoImageAdapter;
    private List<ToDoCategory> toDoCategoryList = new ArrayList<ToDoCategory>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipedit);

        materialEditTextDueDate = findViewById(R.id.dueDate);
        materialEditTextDueTime = findViewById(R.id.dueTime);
        materialEditTextToDoItemName = findViewById(R.id.toDoItemName);

        dueTimeContainer = findViewById(R.id.dueTimeContainer);
        repeatContainer = findViewById(R.id.repeatContainer);
        categoryContainer = findViewById(R.id.categoryContainer);
        doneContainer = findViewById(R.id.doneContainer);

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
        isdoneSwitchCompat = (SwitchCompat)findViewById(R.id.isdoneSwitchCompat);

        Intent i = getIntent();
        toDoItem = (ToDoItem)i.getParcelableExtra( EDITTODOITEMACTIVITY_TODOITEM );

        Bundle data = i.getBundleExtra("data");
        if( data != null )
            toDoItem = (ToDoItem)data.get(EditToDoItemActivity.EDITTODOITEMACTIVITY_TODOITEM);

        if( toDoItem == null ) {
            toDoItem = new ToDoItem();
            Calendar startDate = Calendar.getInstance();
            selectedDate = startDate;
            selectedRecurrence = new Recurrence(startDate.getTimeInMillis(), Recurrence.NONE);  // Does not repeat
            toDoItem.setRecurrencePeriod( Recurrence.NONE );
            toDoItem.setDone(false);
        }
        else {
            selectedDate = toDoItem.getDueDate();
            selectedRecurrence = new Recurrence( toDoItem.getDueDate().getTimeInMillis() , toDoItem.getRecurrencePeriod() );
            selectedToDoCategory = toDoItem.getToDoCategory();
            imageDataList.addAll( toDoItem.getToDoImageList() );
        }

        ToDoImage toDoImage = new ToDoImage();
        toDoImage.setAdd(true);
        if( imageDataList.size() < MAX_IMAGE_COUNT )
            imageDataList.add( 0 , toDoImage );

        Locale locale = getResources().getConfiguration().locale;
        dateFormatLong = new SimpleDateFormat("EEE MMM dd, yyyy", locale);  // Sun Dec 31, 2017
        formatter = new RecurrenceFormat(this, dateFormatLong);

        new EditToDoItemPresenter(Injection.provideToDoItemRepository( this ),this);

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

    private boolean validate( ToDoItem toDoItem ) {
        boolean hasError = false;
        if( TextUtils.isEmpty( toDoItem.getName() ) ) {
            materialEditTextToDoItemName.setError( this.getResources().getString(R.string.error_todo_name) );
            hasError = true;
        }
        if( toDoItem.getDueDate() == null ) {
            materialEditTextDueDate.setError( this.getResources().getString(R.string.error_todo_duedate) );
            hasError = true;
        }
        if( toDoItem.getToDoCategory() == null ) {
            category.setError( this.getResources().getString(R.string.error_todo_category) );
            hasError = true;
        }
        return hasError;
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

                //Add item into "To Do" List
                String name = materialEditTextToDoItemName.getText().toString();
                toDoItem.setName( name );

                if( validate( toDoItem )) {
                    return;
                }

                if( imageDataList != null ) {
                    for(Iterator<ToDoImage> it = imageDataList.iterator(); it.hasNext() ; ) {
                        ToDoImage toDoImage = it.next();
                        if( toDoImage.isAdd() ) {
                            it.remove();
                            break;
                        }
                    }
                }
                toDoItem.setToDoImageList( imageDataList );

                if( isdoneSwitchCompat != null  && isdoneSwitchCompat.getVisibility() == View.VISIBLE ) {
                    if( isdoneSwitchCompat.isChecked() )
                        toDoItem.setDone(true);
                    else
                        toDoItem.setDone(false);
                }

                presenter.createOrUpdateToDoItem(toDoItem);

            }
        });

        if( toDoItem.getId() <= 0 ) {
            doneContainer.setVisibility(View.GONE);
            toDoItem.setDone(false);
        }
        else {
            doneContainer.setVisibility(View.VISIBLE);
            if( isdoneSwitchCompat != null && toDoItem.isDone() ){
                isdoneSwitchCompat.setChecked(true);
            }
        }

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

                    CategorySelectionDialog categorySelectionDialog = new CategorySelectionDialog( toDoCategoryList );
                    categorySelectionDialog.show(getSupportFragmentManager(), "categoryDialog");

                }
            });

            if( selectedToDoCategory != null )
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
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            imageRecylerView.setLayoutManager( gridLayoutManager );
            int spacing = getResources().getDimensionPixelSize(R.dimen.todo_grid_spacing);
            GridItemDecoration gridItemDecoration = new GridItemDecoration( spacing , 0 , this );
            //MediaGridInset gridItemDecoration = new MediaGridInset( 3, spacing , false );
            imageRecylerView.addItemDecoration( gridItemDecoration );

            toDoImageAdapter = new ToDoImageAdapter( this , imageDataList );
            imageRecylerView.setAdapter( toDoImageAdapter );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE_MATISSE && resultCode == RESULT_OK) {
            List<Uri> mSelected = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected);

            // mSelected should had only one item
            if( mSelected != null && mSelected.size() > 0 ) {
                for( Uri uri : mSelected ) {
                    ToDoImage toDoImage = new ToDoImage();
                    toDoImage.setUri( uri );
                    toDoImageAdapter.addElement( toDoImage );
                }

                if( imageDataList.size() > MAX_IMAGE_COUNT ) {
                    toDoImageAdapter.deleteElement( 0 );
                }
            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        dueTimeContainer.setVisibility( View.VISIBLE );
        categoryContainer.setVisibility( View.VISIBLE );
        resetFocusFraction();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year,monthOfYear,dayOfMonth);
        calendar.set(Calendar.SECOND,0);
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
        toDoItem.setToDoCategoryID( toDoCategory.getId() );
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if ( grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                    //执行逻辑
                    Matisse.from(EditToDoItemActivity.this)
                            .choose(MimeType.ofAll())
                            .capture(true)
                            .captureStrategy(new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider"))
                            .countable(true)
                            .maxSelectable(1)//由于这里我只需要一张照片，所以最多选择设置为1
//                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new GlideEngine())
                            .forResult(REQUEST_CODE_CHOOSE);
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
        }
    }

    public void initialToDoCategoryList( List<ToDoCategory> toDoCategoryList ) {
        this.toDoCategoryList = toDoCategoryList;
    }

    @Override
    public void showAfterCreateOrUpdateToDoItem(ToDoItem toDo) {
        if( !toDo.isDone() ) {
            if( Calendar.getInstance().getTimeInMillis() >= toDo.getDueDate().getTimeInMillis() && ((toDoItem.getRecurrencePeriod() == Recurrence.DAILY)
                    || (toDoItem.getRecurrencePeriod() == Recurrence.WEEKLY)
                    || (toDoItem.getRecurrencePeriod() == Recurrence.MONTHLY)
                    || (toDoItem.getRecurrencePeriod() == Recurrence.YEARLY)) ) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, toDoItem.getDueDate().get(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE, toDoItem.getDueDate().get(Calendar.MINUTE));
                calendar.set(Calendar.SECOND, toDoItem.getDueDate().get(Calendar.SECOND));

                long diff = Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis();
                if (toDo.getDueTimestamp() != 0 && diff <= 0 && Math.abs(diff) <= 60 * 1000) {
                    AlarmUtil.doAlarm(calendar.getTimeInMillis(), toDo);
                }
            } else {
                long diff = Calendar.getInstance().getTimeInMillis() - toDo.getDueTimestamp();
                if (toDo.getDueTimestamp() != 0 && diff <= 0 && Math.abs(diff) <= 60 * 1000) {
                    AlarmUtil.doAlarm(toDo.getDueTimestamp(), toDo);
                }
            }
        }
        finish();
    }
}
