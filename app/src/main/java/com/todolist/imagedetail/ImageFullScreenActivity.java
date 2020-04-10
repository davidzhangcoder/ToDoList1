package com.todolist.imagedetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.todolist.R;
import com.todolist.data.model.ToDoImage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

public class ImageFullScreenActivity extends AppCompatActivity {

    public static final String KEY = "ImageFullScreenKey";;

    private ToDoImage toDoImage;

    private AppCompatImageView appCompatImageView;
    private ImageView imageDetailBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent i = getIntent();
        toDoImage = i.getParcelableExtra( KEY );
        AppCompatImageView appCompatImageView = (AppCompatImageView) findViewById(R.id.imagefullscreen);
        imageDetailBack = (ImageView)findViewById(R.id.imageDetailBack);

        if( toDoImage != null ) {
//            int screenWidth = getResources().getDisplayMetrics().widthPixels;
//            int screenHeight = getResources().getDisplayMetrics().heightPixels;

            Glide
                    .with(this.getApplicationContext())
                    .load(toDoImage.getUri())
                    .listener(new RequestListener<Uri, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            if (appCompatImageView == null) {
                                return false;
                            }
                            if (appCompatImageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                                appCompatImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            }
                            ViewGroup.LayoutParams params = appCompatImageView.getLayoutParams();
                            int vw = appCompatImageView.getWidth() - appCompatImageView.getPaddingLeft() - appCompatImageView.getPaddingRight();
                            float scale = (float) vw / (float) resource.getIntrinsicWidth();
                            int vh = Math.round(resource.getIntrinsicHeight() * scale);
                            params.height = vh + appCompatImageView.getPaddingTop() + appCompatImageView.getPaddingBottom();
                            appCompatImageView.setLayoutParams(params);
                            return false;

                        }
                    }
                    )
//                    .override(screenWidth, screenHeight)
//                    .centerCrop()
                    .into(appCompatImageView);
        }

        imageDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
