package com.hzjytech.operation.module.common;

import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;
import android.widget.EditText;

import com.hzjytech.operation.R;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest()
public class LoginActivityTest {
    //设置初始启动测试Activity，并启动
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule
            = new ActivityTestRule<>(LoginActivity.class);
    private LoginActivity mActivity = null;

    @Before
    public void setActivity() {
        mActivity = mActivityRule.getActivity();
    }

    @Test
   public void testEmptyButtonClickable(){
       onView(ViewMatchers.withId(R.id.metLoginTel)).perform(typeText(""));
       onView(ViewMatchers.withId(R.id.metLoginTel)).perform(typeText(""));
       onView(ViewMatchers.withId(R.id.btnLogin)).check(matches(not(ViewMatchers.isEnabled())));
   }
   @Test
   public void testButtonClickable(){
       onView(ViewMatchers.withId(R.id.metLoginTel)).perform(clearText()).perform(typeText("hhc"));
       onView(ViewMatchers.withId(R.id.metLoginPsd)).perform(clearText()).perform(typeText("hhc123456"));
       onView(ViewMatchers.withId(R.id.btnLogin)).check(matches((ViewMatchers.isEnabled())));
       onView(ViewMatchers.withId(R.id.btnLogin)).perform(click());
       pressBack();
   }
    @Test
    public void testTipClick(){
        onView(ViewMatchers.withId(R.id.btnLoginFgpsd)).perform(click());
        onView(withText("请用钉钉联系管理员"))
                .inRoot(withDecorView(not(Matchers.is(mActivity.getWindow().getDecorView()))))
                .check(matches(ViewMatchers.isDisplayed()));
    }

}
