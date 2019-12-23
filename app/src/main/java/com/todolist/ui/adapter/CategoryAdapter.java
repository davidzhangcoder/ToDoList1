package com.todolist.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.todolist.R;
import com.todolist.model.ToDoCategory;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter implements SpinnerAdapter{

    private Context context;

    private List<ToDoCategory> toDoCategoryList;

    private ToDoCategory selectedToDoCategory;

    public CategoryAdapter(Context context, List<ToDoCategory> toDoCategoryList , ToDoCategory selectedToDoCategory ) {
        super(context , R.layout.item_category_title , R.id.categoryName , toDoCategoryList);
        this.context = context;
        this.toDoCategoryList = toDoCategoryList;
        this.selectedToDoCategory = selectedToDoCategory;
    }

    @Override
    public int getCount() {
        return toDoCategoryList.size();
    }

    @Override
    public ToDoCategory getItem(int position) {
        return toDoCategoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ToDoCategory toDoCategory = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category_title, parent, false);
        }

        if( convertView.getTag() == null ) {
            new ViewHolder( convertView );
        }

        // Lookup view for data population

        ViewHolder viewHolder = (ViewHolder)convertView.getTag();
        TextView categoryName = viewHolder.categoryName;
        // Populate the data into the template view using the data object
        categoryName.setText(toDoCategory.getName());
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ToDoCategory toDoCategory = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        }

        if( convertView.getTag() == null ) {
            new ViewHolder( convertView );
        }

        // Lookup view for data population

        ViewHolder viewHolder = (ViewHolder)convertView.getTag();
        TextView categoryName = viewHolder.categoryName;
        View categoryImageCheck = viewHolder.categoryImageCheck;
        // Populate the data into the template view using the data object
        categoryName.setText(toDoCategory.getName());
        if( selectedToDoCategory != null && selectedToDoCategory.getId() == toDoCategory.getId() ) {
            categoryImageCheck.setVisibility(View.VISIBLE);
        }
        else {
            categoryImageCheck.setVisibility(View.INVISIBLE);
        }

        // Return the completed view to render on screen
        return convertView;
    }

    class ViewHolder {
        TextView categoryName;
        View categoryImageCheck;

        public ViewHolder(View convertView){
            categoryName = (TextView) convertView.findViewById(R.id.categoryName);
            categoryImageCheck = (View) convertView.findViewById(R.id.categoryImageCheck);
            convertView.setTag(this);//set a viewholder
        }
    }

    public void setSelectedToDoCategory(ToDoCategory selectedToDoCategory) {
        this.selectedToDoCategory = selectedToDoCategory;
    }
}
