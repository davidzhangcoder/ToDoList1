package com.todolist.todomain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

@RunWith(AndroidJUnit4.class)
public class ToDoFragmentScreenTest {

    @Rule
    public ActivityTestRule rule=new ActivityTestRule(ToDoMainActivity.class);

    @Test
    public void testToDoMainActivity() {
        ((ToDoMainActivity)rule.getActivity()).getSupportFragmentManager().getFragments().clear();
//        onView(withId(R.id.tip_recycler))
//                .check(matches(isDisplayed()));
////        rule.getActivity().finish(); ((ToDoMainActivity)rule.getActivity()).getSupportFragmentManager().getFragments().clear();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void testToDoMainActivityViewPage() {
//        ((ToDoMainActivity)rule.getActivity()).getSupportFragmentManager().getFragments().clear();
////        onView(withId(R.id.viewPager))
////                .perform(swipeLeft());
////                .perform(swipeLeft())
////                .perform(swipeLeft());
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        rule.getActivity().finish();
//    }

//    @Test
//    public void testToDoMainActivityViewPageByClickTab() {
////        onView(ViewMatchers.withText("To Do"))
////                .perform(ViewActions.click());
////        onView(ViewMatchers.withText("Done"))
////                .perform(ViewActions.click());
////        onView(ViewMatchers.withText("To Do"))
////                .perform(ViewActions.click());
////        onView(ViewMatchers.withText("Done"))
////                .perform(ViewActions.click());
//        rule.getActivity().finish();
//    }
//
//
//    public void testToDoFragment() {
////        //获取 Fragment 中的 text
////        ViewInteraction fragmentText = onView(withId(R.id.tv_test_fragment));
////        //验证 text 不存在
////        fragmentText.check(doesNotExist());
////        //点击按钮显示 fragment
////        onView(withId(R.id.btn_show_test_fragment)).perform(click());
////        //验证 text 已加载显示
////        fragmentText.check(matches(isDisplayed()));
//
//    }

}
