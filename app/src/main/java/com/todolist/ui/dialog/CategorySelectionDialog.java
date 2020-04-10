package com.todolist.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.todolist.R;
import com.todolist.data.model.ToDoCategory;
import com.todolist.ui.adapter.CategoryListAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategorySelectionDialog extends AppCompatDialogFragment
{
    private RecyclerView recyclerView;

    private List<ToDoCategory> toDoCategoryList = new ArrayList<ToDoCategory>();

    public CategorySelectionDialog( List<ToDoCategory> toDoCategoryList ) {
        this.toDoCategoryList = toDoCategoryList;
    }

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

        List<ToDoCategory> categoryList = new ArrayList<ToDoCategory>();
        categoryList.addAll( toDoCategoryList );

        CategoryListAdapter categoryListAdapter = new CategoryListAdapter( this.getContext(), categoryList , null );
        for (ToDoCategory toDoCategory : categoryList) {
            if (CategorySelectionDialog.this.getCallback().getSelectedCategory() != null
                    && toDoCategory.getId() == CategorySelectionDialog.this.getCallback().getSelectedCategory().getId()
                    ) {
                toDoCategory.setSelected(true);
                categoryListAdapter.setSelectedToDoCategory(toDoCategory);
            }
//            ((ToDoCategory) toDoCategory).setWorkflow(ToDoCategory.WORKFLOW.EDIT);
        }

        CategoryListAdapter.ItemCallBack itemCallBack = new CategoryListAdapter.ItemCallBack() {
            @Override
            public void doItemClickCallBack(ToDoCategory iToDoCategory) {
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
