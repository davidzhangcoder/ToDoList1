package com.todolist.todomain.fragment.todo;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class ToDoFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragmentList;
    private FragmentManager mFragmentManager;
    private String[] titles;

    public ToDoFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] titles) {
        super(fm);
        this.mFragmentManager = fm;
        mFragmentList = new ArrayList<>();
        this.titles = titles;
        for ( Fragment fragment : fragmentList )
        {
            mFragmentList.add( fragment );
        }
        setFragments(mFragmentList);
    }

    public void updateData(List<Fragment> fragmentList) {
        ArrayList<Fragment> fragments = new ArrayList<>();
        for ( Fragment fragment : fragmentList ) {
            fragments.add( fragment );
        }
        setFragments(fragments);
    }

    private void setFragments(ArrayList<Fragment> mFragmentList) {
        if(this.mFragmentList != null){
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            for(Fragment f:this.mFragmentList){
                fragmentTransaction.remove(f);
            }
            fragmentTransaction.commit();
            mFragmentManager.executePendingTransactions();
        }
        this.mFragmentList = mFragmentList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.mFragmentList.size();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return (CharSequence)(this.titles != null && this.titles.length > position ? this.titles[position] : super.getPageTitle(position));
    }
}

