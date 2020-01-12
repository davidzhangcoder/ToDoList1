package com.todolist.imagedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bumptech.glide.Glide;
import com.todolist.R;
import com.todolist.model.ToDoImage;

public class ImageFullScreenActivity extends AppCompatActivity {

    public static final String KEY = "ImageFullScreenKey";;

    private ToDoImage toDoImage;

    private AppCompatImageView appCompatImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        toDoImage = i.getParcelableExtra( KEY );
        AppCompatImageView appCompatImageView = (AppCompatImageView) findViewById(R.id.imagefullscreen);

        if( toDoImage != null ) {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int screenHeight = getResources().getDisplayMetrics().heightPixels;

            Glide
                    .with(this.getApplicationContext())
                    .load(toDoImage.getUri())
//                    .override(screenWidth, screenHeight)
                    .centerCrop()
                    .into(appCompatImageView);
        }

    }

}
