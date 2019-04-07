package com.todolist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.todolist.db.ToDoItemDao;
import com.todolist.model.IToDoCategory;
import com.todolist.model.ToDoCategory;
import com.todolist.model.ToDoItem;
import com.todolist.ui.dialog.AddCategoryDialog;
import com.todolist.util.ToDoItemUtil;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends BottomSheetDialogFragment
{
    private RecyclerView recyclerView;

    private ToDoItemDao db;

    private OnFragmentInteractionListener mListener;

    public CategoryFragment() {

    }

    public static CategoryFragment newInstance()
    {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        db = new ToDoItemDao(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        recyclerView = view.findViewById(R.id.category_recycler);

        initRecyclerView( recyclerView );

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initRecyclerView( RecyclerView recyclerView )
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        List<IToDoCategory> categoryList = new ArrayList<IToDoCategory>();

        ToDoCategory allToDoCategory = new ToDoCategory();
        allToDoCategory.setId( ToDoCategory.CATEGORY_ALL_ID );
        allToDoCategory.setName( ToDoCategory.CATEGORY_ALL_NAME );
        categoryList.add( allToDoCategory );

        categoryList.addAll( ToDoCategory.getToDoCategorys() );
        for( IToDoCategory toDoCategory : categoryList )
            ((ToDoCategory)toDoCategory).setWorkflow( ToDoCategory.WORKFLOW.LIST );

        ToDoCategory toDoCategoryAddNew = new ToDoCategory();
        toDoCategoryAddNew.setId( ToDoCategory.CATEGORY_ADD_NEW_ID );
        toDoCategoryAddNew.setName( ToDoCategory.CATEGORY_ADD_NEW_NAME );
        toDoCategoryAddNew.setWorkflow( ToDoCategory.WORKFLOW.LIST );
        categoryList.add( toDoCategoryAddNew );


        CategoryListAdapter categoryListAdapter = new CategoryListAdapter( this.getContext(), categoryList );
        CategoryListAdapter.ItemCallBack itemCallBack = new CategoryListAdapter.ItemCallBack() {
            @Override
            public void doItemClickCallBack(IToDoCategory iToDoCategory) {
                CategoryFragment.this.dismiss();
                if( ((ToDoCategory)iToDoCategory).getId() != ToDoCategory.CATEGORY_ADD_NEW_ID )
                    mListener.refresh( ((ToDoCategory)iToDoCategory).getId() );
                else
                {
                    AddCategoryDialog addCategoryDialog = new AddCategoryDialog();
                    addCategoryDialog.show(CategoryFragment.this.getFragmentManager(), "addCategoryDialog");
                }
            }
        };
        categoryListAdapter.setItemCallBack( itemCallBack );

//        tipListAdapter.setDoneAction( getDoneAction() );
        recyclerView.setAdapter( categoryListAdapter );
//        ItemTouchHelper.Callback callback = new TipListItemTouchHelperCallback();
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);

        void refresh( long categoryID );
    }
}
