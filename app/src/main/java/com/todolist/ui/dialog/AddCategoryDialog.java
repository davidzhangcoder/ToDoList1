package com.todolist.ui.dialog;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.todolist.CategoryListAdapter;
import com.todolist.R;
import com.todolist.db.ToDoItemDao;
import com.todolist.model.ToDoCategory;
import com.todolist.model.ToDoItem;

public class AddCategoryDialog extends AppCompatDialogFragment
{

    private ToDoItemDao db;

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        db = new ToDoItemDao(this.getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_add_category, null, false);

        EditText categoryNameEditText = (EditText)view.findViewById(R.id.categoryName);

        AlertDialog alertDialog = builder
                .setView(view)
                .setCancelable(true)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String name = categoryNameEditText.getText().toString();
                        ContentValues values = new ContentValues();
                        values.put(ToDoItem.COLUMN_NAME, name);
                        db.addContent(ToDoCategory.TABLE_NAME, values);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        return alertDialog;

    }
}
