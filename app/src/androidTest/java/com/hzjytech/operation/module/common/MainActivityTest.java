package com.hzjytech.operation.module.common;

import android.hardware.Camera;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzjytech.operation.R;
import com.hzjytech.operation.module.data.DataFragment;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by hehongcan on 2017/10/10.
 * 测试内容：更新 点击扫一扫 新建任务  底部tablayout切换（）
 */
@RunWith(AndroidJUnit4.class)
@LargeTest()
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule
            = new ActivityTestRule<>(MainActivity.class);
    private MainActivity mActivity;

    @Before
    public void setActivity() {
        mActivity = mActivityRule.getActivity();
    }
    @Test
    public void testTabLayoutSwich(){
        //点击指定数据所在的 Item
        onView(allOf(withText("任务"), isDescendantOfA(withId(R.id.main_tablayout))))
                .perform(click())
                .check(matches(isDisplayed()));
        // Then I'd like to check that the tab text (test2) matches the current fragment title
        onView(allOf(withText("待处理任务"),isDescendantOfA(withId(R.id.titleBar)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(allOf(withText("数据"), isDescendantOfA(withId(R.id.main_tablayout))))
                .perform(click())
                .check(matches(isDisplayed()));
        // Then I'd like to check that the tab text (test2) matches the current fragment title
        onView(allOf(withText("数据统计"),isDescendantOfA(withId(R.id.titleBar)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(allOf(withText("咖啡机"), isDescendantOfA(withId(R.id.main_tablayout))))
                .perform(click())
                .check(matches(isDisplayed()));
        // Then I'd like to check that the tab text (test2) matches the current fragment title
        onView(allOf(withText("单机管理"),isDescendantOfA(withId(R.id.titleBar)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(allOf(withText("我"), isDescendantOfA(withId(R.id.main_tablayout))))
                .perform(click())
                .check(matches(isDisplayed()));
        // Then I'd like to check that the tab text (test2) matches the current fragment title
        onView(allOf(withText("我"),isDescendantOfA(withId(R.id.titleBar)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(allOf(withText("首页"), isDescendantOfA(withId(R.id.main_tablayout))))
                .perform(click())
                .check(matches(isDisplayed()));
        // Then I'd like to check that the tab text (test2) matches the current fragment title
        onView(allOf(withText("运营"),isDescendantOfA(withId(R.id.titleBar)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.rv_home)).perform(swipeUp());
    }
    @Test
    public void testClickPlus(){
        onView(allOf(is(instanceOf(ImageView.class)),isDescendantOfA(withId(R.id.titleBar)))).perform(click());
        onView(allOf(withId(R.id.ll_scan))).perform(click());
        pressBack();
        onView(allOf(is(instanceOf(ImageView.class)),isDescendantOfA(withId(R.id.titleBar)))).perform(click());
        onView(allOf(withId(R.id.ll_new_task))).perform(click());
    }
}