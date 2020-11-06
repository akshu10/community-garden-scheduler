package com.example.communitygardenscheduler;

import android.content.Context;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.communitygardenscheduler.activities.TaskCreatorActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class TaskCreatorTest {

    @Rule
    public ActivityTestRule<TaskCreatorActivity> activityRule =
            new ActivityTestRule<>(TaskCreatorActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.example.communitygardenscheduler", appContext.getPackageName());
    }

    // test for correct dialog output given the creation of a single task
    @Test
    public void singleTaskTest(){
        String expected =   "Type: " + "Weeding" +
                            "\nDate: " + 2020 + "/" + 7 + "/" + 11 +
                            "\nNumber created: 1";
        onView(withId(R.id.spinner)).perform(click());
        onData(hasToString("Weeding")).perform(click());
        onView(withId(R.id.datePicker)).perform(PickerActions.setDate(2020,7,11));
        onView(withId(R.id.createTaskButton)).perform(click());

        onView(withText(expected)).check(matches(isDisplayed()));
    }

    // test for the correct error text displaying given a blank repeat number field
    @Test
    public void invalidRepeatNumber(){
        onView(withId(R.id.spinner)).perform(click());
        onData(hasToString("Weeding")).perform(click());
        onView(withId(R.id.taskrepeatOffset)).perform(typeText("2"));
        onView(withId(R.id.datePicker)).perform(PickerActions.setDate(2020,7,11));
        closeSoftKeyboard();
        onView(withId(R.id.repeatSwitch)).perform(click());
        onView(withId(R.id.createTaskButton)).perform(click());

        onView(withId(R.id.errorMessageText)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.repeatNumberError)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    // test for the correct error text displaying given a blank repeat offset field
    @Test
    public void invalidRepeatOffset(){
        onView(withId(R.id.spinner)).perform(click());
        onData(hasToString("Weeding")).perform(click());
        onView(withId(R.id.taskrepeatNumber)).perform(typeText("2"));
        onView(withId(R.id.datePicker)).perform(PickerActions.setDate(2020,7,11));
        closeSoftKeyboard();
        onView(withId(R.id.repeatSwitch)).perform(click());
        onView(withId(R.id.createTaskButton)).perform(click());

        onView(withId(R.id.errorMessageText)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.repeatOffsetError)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    // test for correct dialog output given the creation of 0 repeat tasks
    @Test
    public void validRepeatTest(){
        String expected = "Type: " + "Weeding" +
                        "\nFirst Date: " + 2020 + "/" + 7 + "/" + 11 +
                        "\nNumber created: " + 0 +
                        "\nEvery " + 0 + " days"  +
                        "\nDuplicates: "  + 0;
        onView(withId(R.id.spinner)).perform(click());
        onData(hasToString("Weeding")).perform(click());
        onView(withId(R.id.taskrepeatNumber)).perform(typeText("0"));
        closeSoftKeyboard();
        onView(withId(R.id.taskrepeatOffset)).perform(typeText("0"));
        onView(withId(R.id.datePicker)).perform(PickerActions.setDate(2020,7,11));
        closeSoftKeyboard();
        onView(withId(R.id.repeatSwitch)).perform(click());
        onView(withId(R.id.createTaskButton)).perform(click());

        onView(withText(expected)).check(matches(isDisplayed()));
    }





}
