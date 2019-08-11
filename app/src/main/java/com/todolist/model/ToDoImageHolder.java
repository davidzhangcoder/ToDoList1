package com.todolist.model;

import android.Manifest;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.todolist.R;
import com.todolist.tododetail.EditToDoItemActivity;
import com.todolist.todomain.ToDoMainActivity;
import com.todolist.ui.adapter.ToDoImageAdapter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

public class ToDoImageHolder extends BaseViewHolder<String,ToDoImageAdapter> {

    private final int REQUEST_CODE_CHOOSE=0;

    public ToDoImageHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(String model, int position, ToDoImageAdapter adapter) {
        AppCompatImageView appCompatImageView = (AppCompatImageView)getView(R.id.image);

        if( model != null && model.trim().equals("Add") ) {
            appCompatImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Matisse
                if(ContextCompat.checkSelfPermission(adapter.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions((Activity) adapter.getContext(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }else{
                    //执行逻辑
                    Matisse.from((Activity) adapter.getContext())
                            .choose(MimeType.ofAll())
                            .countable(true)
                            .maxSelectable(1)//由于这里我只需要一张照片，所以最多选择设置为1
//                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new GlideEngine())
                            .forResult(EditToDoItemActivity.REQUEST_CODE_CHOOSE_MATISSE);
                }
                }
            });
        }

        Glide.with( adapter.getContext() ).load(R.drawable.add_image ).into( appCompatImageView );
    }

}
