package com.todolist.todomain;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.todolist.R;
import com.todolist.todomain.fragment.todo.ToDoFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ToDoFragmentScreenTest {

    @Rule
    public ActivityTestRule rule=new ActivityTestRule(ToDoMainActivity.class);

    @Test
    public void testToDoMainActivity() {
        onView(withId(R.id.tip_recycler))
                .check(matches(isDisplayed()));
        rule.getActivity().finish();
    }

    @Test
    public void testToDoMainActivityViewPage() {
//        onView(withId(R.id.viewPager))
//                .perform(swipeLeft());
//                .perform(swipeLeft())
//                .perform(swipeLeft());
        rule.getActivity().finish();
    }

    @Test
    public void testToDoMainActivityViewPageByClickTab() {
//        onView(ViewMatchers.withText("To Do"))
//                .perform(ViewActions.click());
//        onView(ViewMatchers.withText("Done"))
//                .perform(ViewActions.click());
//        onView(ViewMatchers.withText("To Do"))
//                .perform(ViewActions.click());
//        onView(ViewMatchers.withText("Done"))
//                .perform(ViewActions.click());
        rule.getActivity().finish();
    }


    public void testToDoFragment() {
//        //获取 Fragment 中的 text
//        ViewInteraction fragmentText = onView(withId(R.id.tv_test_fragment));
//        //验证 text 不存在
//        fragmentText.check(doesNotExist());
//        //点击按钮显示 fragment
//        onView(withId(R.id.btn_show_test_fragment)).perform(click());
//        //验证 text 已加载显示
//        fragmentText.check(matches(isDisplayed()));

    }

}
