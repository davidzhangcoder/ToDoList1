package com.todolist;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

//public class ToDoFragmentAdapter extends FragmentPagerAdapter {
//    private List<Fragment> fragmentList = new ArrayList();
//    private String[] titles;
//
//    public ToDoFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
//        super(fm);
//        this.fragmentList.clear();
//        this.fragmentList.addAll(fragmentList);
//    }
//
//    public ToDoFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] titles) {
//        super(fm);
//        this.fragmentList.clear();
//        this.fragmentList.addAll(fragmentList);
//        this.titles = titles;
//    }
//
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        // 把 Object 强转为 View，然后将 view 从 ViewGroup 中清除
//        container.removeView((View) object);
//    }
//
//    public CharSequence getPageTitle(int position) {
//        return (CharSequence)(this.titles != null && this.titles.length > position ? this.titles[position] : super.getPageTitle(position));
//    }
//
//    public Fragment getItem(int position) {
//        return (Fragment)this.fragmentList.get(position);
//    }
//
//    public int getCount() {
//        return this.fragmentList.size();
//    }
//}

public class ToDoFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragmentList;
    private FragmentManager mFragmentManager;

    public ToDoFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.mFragmentManager = fm;
        mFragmentList = new ArrayList<>();
        for ( Fragment fragment : fragmentList )
        {
            mFragmentList.add( fragment );
        }
        setFragments(mFragmentList);
    }

    public void updateData(List<Fragment> fragmentList) {
        ArrayList<Fragment> fragments = new ArrayList<>();
        for ( Fragment fragment : fragmentList ) {
//            Log.e("FPagerAdapter1", dataList.get(i).toString());
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
}

