package com.todolist.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.todolist.R;

public class AddCategoryDialog extends AppCompatDialogFragment
{

    public static final String KEY_NEW_ADDED_CATEGORY = "KEY_NEW_ADDED_CATEGORY";

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_add_category, null, false);

        EditText categoryNameEditText = (EditText)view.findViewById(R.id.categoryName);

        AlertDialog alertDialog = builder
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(getResources().getString( R.string.done ), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(categoryNameEditText.getText().toString());
                    }
                })
                .setNegativeButton(getResources().getString( R.string.cancel ), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        return alertDialog;

    }

    private void setResult(String newAddedCategoryName) {
        Intent intent = new Intent();
        intent.putExtra(KEY_NEW_ADDED_CATEGORY, newAddedCategoryName);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

    }

}
