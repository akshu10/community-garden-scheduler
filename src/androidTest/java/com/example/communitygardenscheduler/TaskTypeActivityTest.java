package com.example.communitygardenscheduler;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import com.example.communitygardenscheduler.activities.TaskTypeActivity;

import org.junit.Rule;
import org.junit.Test;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


public class TaskTypeActivityTest {

    @Rule
    public ActivityTestRule<TaskTypeActivity> rule =
            new ActivityTestRule<>(TaskTypeActivity.class);


    @Test
    public void testViews() {
        onView(withId(R.id.addButton)).check(matches(isDisplayed()));
        onView(withId(R.id.addTaskType)).check(matches(isDisplayed()));
        onView(withId(R.id.addTaskTypeInput)).check(matches(isDisplayed()));
    }


    @Test
    public void textViewIsEditable() {
        onView(withId(R.id.addTaskTypeInput)).perform(click()).perform(typeText("Parth"));
        Espresso.closeSoftKeyboard();
    }

    @Test
    public void buttonIsClickable() {
        onView(withId(R.id.addButton)).perform(click());
    }

    @Test
    public void errorIsDisplayed() {
        onView(withId(R.id.addButton)).perform(click());
        onView(withId(R.id.emptyFieldError)).check(matches(isDisplayed()));
    }

}
