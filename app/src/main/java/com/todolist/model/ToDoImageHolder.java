package com.todolist.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.todolist.R;
import com.todolist.data.model.ToDoImage;
import com.todolist.imagedetail.ImageFullScreenActivity;
import com.todolist.tododetail.EditToDoItemActivity;
import com.todolist.ui.adapter.ToDoImageAdapter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class ToDoImageHolder extends BaseViewHolder<ToDoImage,ToDoImageAdapter> {

    private final int REQUEST_CODE_CHOOSE=0;

    public ToDoImageHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(ToDoImage toDoImage, int position, ToDoImageAdapter adapter) {
        AppCompatImageView appCompatImageView = (AppCompatImageView) getView(R.id.image);
        AppCompatImageView deleteImageView = (AppCompatImageView) getView(R.id.delete_image_view);

        int imageSize = getImageResize( adapter.getContext() );
        if (toDoImage != null && toDoImage.isAdd()) {
            appCompatImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( Build.VERSION.SDK_INT >= 23 ) {
                        // Marshmallow+

                        //Matisse
                        if (
                                ContextCompat.checkSelfPermission(adapter.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                        || ContextCompat.checkSelfPermission(adapter.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        ) {
                            ActivityCompat.requestPermissions(
                                    (Activity) adapter.getContext(),
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    1);
                        } else {
                            doSelectImage(adapter);
                        }
                    }
                    else {
                        // Pre-Marshmallow
                        doSelectImage(adapter);
                    }
                }
            });

            // Glide not support load vector drawable, so using Imageview to load directly
//            Glide
//                    .with(adapter.getContext())
//                    .load(R.drawable.ic_multiple_image_view_add)
//                    .override( imageSize , imageSize )
//                    .centerCrop()
//                    .into(appCompatImageView);

            appCompatImageView.setImageResource( R.drawable.ic_multiple_image_view_add );
            appCompatImageView.getLayoutParams().height=imageSize;
            appCompatImageView.getLayoutParams().width=imageSize;

            ImageView imageView = (ImageView)getView(R.id.delete_image_view);
            imageView.setVisibility(View.INVISIBLE);

        } else if (toDoImage.getUri() != null) {
            Glide
                    .with(adapter.getContext())
                    .load(toDoImage.getUri())
                    .override( imageSize , imageSize )
                    .centerCrop()
                    .into(appCompatImageView);

            appCompatImageView.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(adapter.getContext(), ImageFullScreenActivity.class);
                    intent.putExtra(ImageFullScreenActivity.KEY,toDoImage);
                    adapter.getContext().startActivity(intent);
                }
            });

            deleteImageView.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    adapter.deleteElement( position );

                    if( adapter.getData() != null
                            && adapter.getData().size() > 0
                            && !adapter.getData().get( 0 ).isAdd()
                            && adapter.getData().size() < EditToDoItemActivity.MAX_IMAGE_COUNT ) {
                        com.todolist.data.model.ToDoImage toDoImage = new com.todolist.data.model.ToDoImage();
                        toDoImage.setAdd(true);
                        adapter.addElement( toDoImage , 0 );
                    }
                }
            });
        }
    }

    private void doSelectImage(ToDoImageAdapter adapter) {
        //执行逻辑
        Matisse.from((Activity) adapter.getContext())
                .choose(MimeType.ofAll())
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider"))
                .countable(true)
//                                .maxSelectable(1)//由于这里我只需要一张照片，所以最多选择设置为1
//                        .gridExpectedSize(App.getContext().getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(EditToDoItemActivity.REQUEST_CODE_CHOOSE_MATISSE);
    }

    private int getImageResize(Context context) {
            int spanCount = 3;
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            int availableWidth = screenWidth - context.getResources().getDimensionPixelSize(
                    R.dimen.todo_grid_spacing) * (spanCount - 1) - context.getResources().getDimensionPixelSize(
                    R.dimen.todo_grid_spacing) * 2;
            int imageSize = availableWidth / spanCount;
       return imageSize;
    }


}
