package com.todolist.todomain.fragment.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.todolist.R;
import com.todolist.app.App;
import com.todolist.data.model.ToDoCategory;
import com.todolist.ui.adapter.CategoryListAdapter;
import com.todolist.ui.dialog.AddCategoryDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryFragment extends BottomSheetDialogFragment implements CategoryContract.View
{
    public static final String KEY_SELECTED_TODOCATEGORY = "KEY_SELECTED_TODOCATEGORY";
    public static final int ADD_NEW_CATEGORY_REQUEST_CODE = 1;

    private RecyclerView recyclerView;

    private OnFragmentInteractionListener mListener;

    private ToDoCategory selectedToDoCategory;

    @Inject
    CategoryContract.Presenter presenter;

    private List<ToDoCategory> categoryList = new ArrayList<ToDoCategory>();

    private CategoryListAdapter categoryListAdapter;

    public CategoryFragment() {

    }

    public static CategoryFragment newInstance(ToDoCategory toDoCategory)
    {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putSerializable("selectedToDoCategory",toDoCategory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedToDoCategory = (ToDoCategory)getArguments().getSerializable("selectedToDoCategory");
        }
        DaggerCategoryComponent
                .builder()
                .appComponent(((App)getActivity().getApplication()).getAppComponent())
                .categoryModule(new CategoryModule(this))
                .build()
                .inject(this);
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

        categoryListAdapter = new CategoryListAdapter( this.getContext(), categoryList , selectedToDoCategory );
        CategoryListAdapter.ItemCallBack itemCallBack = new CategoryListAdapter.ItemCallBack() {
            @Override
            public void doItemClickCallBack(ToDoCategory iToDoCategory) {
                CategoryFragment.this.dismiss();
                if( ((ToDoCategory)iToDoCategory).getId() != ToDoCategory.Companion.getCATEGORY_ALL_ID() ) {
                    setResult((ToDoCategory) iToDoCategory);
                }
                else
                {
                    AddCategoryDialog addCategoryDialog = new AddCategoryDialog();
                    addCategoryDialog.setTargetFragment( CategoryFragment.this , ADD_NEW_CATEGORY_REQUEST_CODE);
                    addCategoryDialog.show(CategoryFragment.this.getFragmentManager(), "addCategoryDialog");
                }
            }
        };
        categoryListAdapter.setItemCallBack( itemCallBack );

        recyclerView.setAdapter( categoryListAdapter );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        new CategoryPresenter(Injection.provideToDoItemRepository(),this);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(CategoryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void showToDoCategorys(List<ToDoCategory> toDoCategoryList) {
        categoryList.clear();

        ToDoCategory allToDoCategory = new ToDoCategory( ToDoCategory.Companion.getCATEGORY_ALL_ID() , ToDoCategory.Companion.getAllCatrgoryName() );
        categoryList.add( allToDoCategory );

        categoryList.addAll( toDoCategoryList );

//        for( ToDoCategory toDoCategory : categoryList )
//            ((ToDoCategory)toDoCategory).setWorkflow( ToDoCategory.WORKFLOW.LIST );
//
//        ToDoCategory toDoCategoryAddNew = new ToDoCategory();
//        toDoCategoryAddNew.setId( ToDoCategory.CATEGORY_ADD_NEW_ID );
//        toDoCategoryAddNew.setName( ToDoCategory.CATEGORY_ADD_NEW_NAME );
//        toDoCategoryAddNew.setWorkflow( ToDoCategory.WORKFLOW.LIST );
//        categoryList.add( toDoCategoryAddNew );

        categoryListAdapter.replaceData( categoryList);
    }

    //需要传递数据时调用
    private void setResult(ToDoCategory toDoCategory) {
        Intent intent = new Intent();
        intent.putExtra(KEY_SELECTED_TODOCATEGORY, toDoCategory);
        getTargetFragment().onActivityResult(getTargetRequestCode(), this.getTargetRequestCode(), intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == ADD_NEW_CATEGORY_REQUEST_CODE) {
            String newCategoryName = (String) data.getExtras().get(AddCategoryDialog.KEY_NEW_ADDED_CATEGORY);
            ToDoCategory toDoCategory = new ToDoCategory( newCategoryName );
            presenter.saveCategory(toDoCategory);
        }
    }

    public interface OnFragmentInteractionListener {
    }
}
