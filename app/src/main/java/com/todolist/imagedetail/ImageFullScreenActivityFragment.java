package com.todolist.imagedetail;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.todolist.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ImageFullScreenActivityFragment extends Fragment {

    public ImageFullScreenActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_full_screen, container, false);
        return view;
    }
}
