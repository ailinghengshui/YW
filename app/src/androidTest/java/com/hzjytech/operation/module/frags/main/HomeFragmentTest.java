package com.hzjytech.operation.module.frags.main;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hzjytech.operation.R;
import com.hzjytech.operation.entity.MachineInfo;
import com.hzjytech.operation.module.common.MainActivity;
import com.hzjytech.operation.module.group.DetailGroupActivity;
import com.hzjytech.operation.module.home.SearchActivity;
import com.hzjytech.operation.module.machine.DetailMachineActivity;
import com.hzjytech.operation.module.menu.MenuActivity;
import com.hzjytech.operation.module.task.NewTaskActivity;
import com.hzjytech.operation.scan.activity.CaptureActivity;
import com.hzjytech.operation.widgets.TitleBar;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by hehongcan on 2017/10/11.
 */
public class HomeFragmentTest {
    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule<>(
            MainActivity.class,false,false);
    private Activity mActivity;
    private Instrumentation.ActivityMonitor am;

    @Before
    public void setUp(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent = new Intent(context,MainActivity.class);
        activityRule.launchActivity(intent);
        mActivity = activityRule.getActivity();
        assertNotNull("Cannot start test since target Activity is NULL!", mActivity);
        onView(allOf(withText("运营"),isDescendantOfA(withId(R.id.titleBar)))).check(matches(
                ViewMatchers.isDisplayed()));
    }

    /**
     * 测试下拉框跳转
     */
   @Test
   public void testClickPlus(){
       am = getInstrumentation().addMonitor(CaptureActivity.class.getName(), null, false);
       onView(allOf(is(instanceOf(ImageView.class)),isDescendantOfA(withId(R.id.titleBar)))).perform(click());
       onView(allOf(withId(R.id.ll_scan))).perform(click());
       //设定等待满足要求的活动创建成功，最多等待5s
       Activity captureActivity = am.waitForActivityWithTimeout(5000);
       assertNotNull("Result CaptureActivity should NOT be null!", captureActivity );
       pressBack();
       if(am!=null){
           getInstrumentation().removeMonitor(am);
           am = null;
       }
       am = getInstrumentation().addMonitor(NewTaskActivity.class.getName(), null, false);
       onView(allOf(withText("运营"),isDescendantOfA(withId(R.id.titleBar)))).check(matches(
               ViewMatchers.isDisplayed()));
       onView(allOf(is(instanceOf(ImageView.class)),isDescendantOfA(withId(R.id.titleBar)))).perform(click());
       onView(allOf(withId(R.id.ll_new_task))).perform(click());
       //设定等待满足要求的活动创建成功，最多等待5s
       Activity newTaskActivity= am.waitForActivityWithTimeout(5000);
       assertNotNull("Result NewTaskActivity should NOT be null!", newTaskActivity );
   }

    /**
     * 点击搜索框
     * @throws Exception
     */
    @Test
    public void testClickSearchView(){
        if(am!=null){
            getInstrumentation().removeMonitor(am);
            am = null;
        }
        am = getInstrumentation().addMonitor(SearchActivity.class.getName(), null, false);
     /*   onView(withId(R.id.rv_home)).perform(RecyclerViewActions.actionOnItem(
                hasDescendant(withId(R.id.rl_item_search)), click()));*/
        onView(withId(R.id.rv_home))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Activity searchActivity= am.waitForActivityWithTimeout(5000);
        assertNotNull("Result SearchActivity should NOT be null!", searchActivity);
    }
    @Test
    public void testClickCircleButtonView(){
        onView(withId(R.id.rv_home)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, MyViewAction.clickChildViewWithId(R.id.item_machines_error)));
        onView(allOf(withText("咖啡机错误"),isDescendantOfA(withId(R.id.titleBar)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        pressBack();
        onView(withId(R.id.rv_home)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, MyViewAction.clickChildViewWithId(R.id.item_machines_shortage)));
        onView(allOf(withText("余料不足"),isDescendantOfA(withId(R.id.titleBar)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        pressBack();
        onView(withId(R.id.rv_home)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, MyViewAction.clickChildViewWithId(R.id.item_machines_offline)));
        onView(allOf(withText("离线状态"),isDescendantOfA(withId(R.id.titleBar)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        pressBack();
        onView(withId(R.id.rv_home)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, MyViewAction.clickChildViewWithId(R.id.item_machines_lock)));
        onView(allOf(withText("已锁定"),isDescendantOfA(withId(R.id.titleBar)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        pressBack();
        onView(withId(R.id.rv_home)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, MyViewAction.clickChildViewWithId(R.id.item_machines_unoperation)));
        onView(allOf(withText("未运营"),isDescendantOfA(withId(R.id.titleBar)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    }

    /**
     * 测试条目点击
     */
    @Test
    public void testClickMachineItem(){
        if(am!=null){
            getInstrumentation().removeMonitor(am);
            am = null;
        }
        am = getInstrumentation().addMonitor(DetailMachineActivity.class.getName(), null, false);
     /*   onView(withId(R.id.rv_home)).perform(RecyclerViewActions.actionOnItem(
                hasDescendant(withId(R.id.rl_item_search)), click()));*/
        onView(withId(R.id.rv_home))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        Activity machineActivity= am.waitForActivityWithTimeout(5000);
        assertNotNull("Result machineActivity should NOT be null!", machineActivity);
        pressBack();
        if(am!=null){
            getInstrumentation().removeMonitor(am);
            am = null;
        }
        am = getInstrumentation().addMonitor(DetailMachineActivity.class.getName(), null, false);
        onView(withId(R.id.rv_home)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, MyViewAction.clickChildViewWithId(R.id.tv_machines_number)));
        Activity machine2Activity= am.waitForActivityWithTimeout(5000);
        assertNotNull("Result machineActivity should NOT be null!", machine2Activity);
        pressBack();
        if(am!=null){
            getInstrumentation().removeMonitor(am);
            am = null;
        }
        am = getInstrumentation().addMonitor(DetailGroupActivity.class.getName(), null, false);
        try {
            onView(withId(R.id.rv_home)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(2, MyViewAction.clickChildViewWithId(R.id.tv_machine_group)));
            Activity groupActivity= am.waitForActivityWithTimeout(5000);
            assertNotNull("Result groupActivity should NOT be null!", groupActivity);
            pressBack();
        }catch (NullPointerException e){
            Log.e("e","group is null");
        }finally {
            if(am!=null){
                getInstrumentation().removeMonitor(am);
                am = null;
            }
            am = getInstrumentation().addMonitor(MenuActivity.class.getName(), null, false);
            try {
                onView(withId(R.id.rv_home)).perform(
                        RecyclerViewActions.actionOnItemAtPosition(2, MyViewAction.clickChildViewWithId(R.id.tv_machine_menu)));
                Activity menuActivity= am.waitForActivityWithTimeout(5000);
                assertNotNull("Result groupActivity should NOT be null!", menuActivity);
            } catch (NullPointerException e) {
                Log.e("e","menu is null");
            }
        }

    }

    /**
     * 测试回到顶部图标
     */
    @Test
    public void testImageButton(){

        try {
            onView(withId(R.id.rv_home)).perform(
                    RecyclerViewActions.scrollToPosition(10));
            onView(allOf(withId(R.id.iv_to_top))).check(matches(isDisplayed()));
            onView(allOf(withId(R.id.iv_to_top))).perform(click());
            onView(withId(R.id.tv_home_search)).check(matches(isDisplayed()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static class MyViewAction {

        public  static ViewAction clickChildViewWithId(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return "Click on a child view with specified id.";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    View v = view.findViewById(id);
                    v.performClick();
                }
            };
        }

    }
   @After
    public void tearDown() throws Exception {
        if(mActivity != null) {
            mActivity.finish();
            mActivity = null;
        }
        if(am != null) {
            getInstrumentation().removeMonitor(am);
            am = null;
        }
    }
}