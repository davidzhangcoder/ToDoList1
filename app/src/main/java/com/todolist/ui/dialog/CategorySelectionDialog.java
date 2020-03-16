package com.todolist.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.todolist.ui.adapter.CategoryListAdapter;
import com.todolist.R;
import com.todolist.model.IToDoCategory;
import com.todolist.model.ToDoCategory;

import java.util.ArrayList;
import java.util.List;

public class CategorySelectionDialog extends AppCompatDialogFragment
{
    private RecyclerView recyclerView;

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_category, null, false);
        recyclerView = view.findViewById(R.id.category_recycler);
        initRecyclerView( recyclerView );

        AlertDialog alertDialog = builder
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(getResources().getString( R.string.done ), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
//                        Toast.makeText(getActivity(),((CategoryListAdapter)recyclerView.getAdapter()).getSelectedToDoCategory().getName(),Toast.LENGTH_LONG).show();
//
//                        recyclerView.getAdapter();

                        if( CategorySelectionDialog.this.getCallback() != null && ((CategoryListAdapter)recyclerView.getAdapter()).getSelectedToDoCategory() != null )
                            CategorySelectionDialog.this.getCallback().onCategorySelected( (ToDoCategory)((CategoryListAdapter)recyclerView.getAdapter()).getSelectedToDoCategory() );
                    }
                })
                .setNegativeButton(getResources().getString( R.string.cancel ), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        return alertDialog;
    }

    private void initRecyclerView( RecyclerView recyclerView )
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        List<IToDoCategory> categoryList = new ArrayList<IToDoCategory>();
        categoryList.addAll( ToDoCategory.getToDoCategorys() );

        CategoryListAdapter categoryListAdapter = new CategoryListAdapter( this.getContext(), categoryList , null );
        for (IToDoCategory toDoCategory : categoryList) {
            if (CategorySelectionDialog.this.getCallback().getSelectedCategory() != null
                    && ((ToDoCategory) toDoCategory).getId() == CategorySelectionDialog.this.getCallback().getSelectedCategory().getId()
                    ) {
                ((ToDoCategory) toDoCategory).setSelected(true);
                categoryListAdapter.setSelectedToDoCategory(toDoCategory);
            }
            ((ToDoCategory) toDoCategory).setWorkflow(ToDoCategory.WORKFLOW.EDIT);
        }

        CategoryListAdapter.ItemCallBack itemCallBack = new CategoryListAdapter.ItemCallBack() {
            @Override
            public void doItemClickCallBack(IToDoCategory iToDoCategory) {
                if( categoryListAdapter.getSelectedToDoCategory() != null )
                    ((ToDoCategory)categoryListAdapter.getSelectedToDoCategory()).setSelected( false );
                ((ToDoCategory)iToDoCategory).setSelected( true );
                categoryListAdapter.setSelectedToDoCategory(iToDoCategory);
                categoryListAdapter.notifyDataSetChanged();
            }
        };

        categoryListAdapter.setItemCallBack( itemCallBack );
        recyclerView.setAdapter( categoryListAdapter );
    }


    public interface CategorySelectedCallback {
        void onCategorySelected(ToDoCategory toDoCategory);
        ToDoCategory getSelectedCategory();
    }

    @Nullable
    private CategorySelectedCallback getCallback() {
        try {
            if (getTargetFragment() != null) {
                return (CategorySelectedCallback) getTargetFragment();
            } else {
                return (CategorySelectedCallback) getActivity();
            }
        } catch (ClassCastException e) {
            // Interface callback is not implemented in activity
            return null;
        }
    }


}
