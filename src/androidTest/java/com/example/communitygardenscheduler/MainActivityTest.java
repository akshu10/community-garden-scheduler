package com.example.communitygardenscheduler;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;

import com.example.communitygardenscheduler.activities.AddPlantToDbActivity;
import com.example.communitygardenscheduler.activities.CalendarActivity;
import com.example.communitygardenscheduler.activities.DailyTasksActivity;
import com.example.communitygardenscheduler.activities.GardenTaskInformationActivity;
import com.example.communitygardenscheduler.activities.MainActivity;
import com.example.communitygardenscheduler.activities.OverdueTasksActivity;
import com.example.communitygardenscheduler.activities.PlantListActivity;
import com.example.communitygardenscheduler.activities.TaskCreatorActivity;
import com.example.communitygardenscheduler.activities.TaskTypeActivity;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);


    LoginActivityTest test = new LoginActivityTest();

    public LoginActivityTest getTest() {
        return test;
    }


    @Test
    public void viewsAreDisplayed() throws InterruptedException {
        test.loginActivityTest();
        onView(withId(R.id.welcomeMessage)).check(matches(isDisplayed()));
        logout();
        ViewInteraction viewInteraction = onView(allOf(withId(R.id.taskCreator)));
        viewInteraction = onView(allOf(withId(R.id.gardenInformation)));
        viewInteraction = onView(allOf(withId(R.id.calendarButton)));
        viewInteraction = onView(allOf(withId(R.id.taskList)));
        viewInteraction = onView(allOf(withId(R.id.overDueTasks)));
        viewInteraction = onView(allOf(withId(R.id.addTaskType)));
        viewInteraction = onView(allOf(withId(R.id.plantInfo)));
        viewInteraction = onView(allOf(withId(R.id.addPlantType)));

    }

    private void logout() {
        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.logout), withContentDescription("Logout"),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());
    }

    @Test
    public void viewsAreClickable() throws InterruptedException {
        test.loginActivityTest();

        ViewInteraction viewInteraction;

        viewInteraction = onView(allOf(withId(R.id.taskCreator)));
        viewInteraction.perform(click());
        Espresso.pressBack();

        onView(withId(R.id.gardenInformation)).perform(scrollTo());

        viewInteraction = onView(allOf(withId(R.id.gardenInformation)));
        viewInteraction.perform(click());
        Espresso.pressBack();

        viewInteraction = onView(allOf(withId(R.id.calendarButton)));
        viewInteraction.perform(click());
        Espresso.pressBack();

        viewInteraction = onView(allOf(withId(R.id.taskList)));
        viewInteraction.perform(click());
        Espresso.pressBack();

        viewInteraction = onView(allOf(withId(R.id.overDueTasks)));
        viewInteraction.perform(click());
        Espresso.pressBack();

        viewInteraction = onView(allOf(withId(R.id.plantInfo)));
        viewInteraction.perform(click());
        Espresso.pressBack();

        viewInteraction = onView(allOf(withId(R.id.addTaskType)));
        viewInteraction.perform(scrollTo()).perform(click());
        Espresso.pressBack();

        viewInteraction = onView(allOf(withId(R.id.addPlantType)));
        viewInteraction.perform(scrollTo()).perform(click());
        Espresso.pressBack();


        logout();
    }

    @Test
    public void respectiveActivitiesAreLaunched() throws InterruptedException {

        test.loginActivityTest();

        ViewInteraction viewInteraction;

        viewInteraction = onView(allOf(withId(R.id.taskCreator)));
        viewInteraction.perform(click());
        isChecked().matches(TaskCreatorActivity.class);
        Espresso.pressBack();

        onView(withId(R.id.gardenInformation)).perform(scrollTo());

        viewInteraction = onView(allOf(withId(R.id.gardenInformation)));
        viewInteraction.perform(click());
        isChecked().matches(GardenTaskInformationActivity.class);
        Espresso.pressBack();

        viewInteraction = onView(allOf(withId(R.id.calendarButton)));
        viewInteraction.perform(click());
        isChecked().matches(CalendarActivity.class);
        Espresso.pressBack();

        viewInteraction = onView(allOf(withId(R.id.taskList)));
        viewInteraction.perform(click());
        isChecked().matches(DailyTasksActivity.class);
        Espresso.pressBack();

        viewInteraction = onView(allOf(withId(R.id.overDueTasks)));
        viewInteraction.perform(click());
        isChecked().matches(OverdueTasksActivity.class);
        Espresso.pressBack();

        viewInteraction = onView(allOf(withId(R.id.plantInfo)));
        viewInteraction.perform(click());
        isChecked().matches(PlantListActivity.class);
        Espresso.pressBack();

        viewInteraction = onView(allOf(withId(R.id.addTaskType)));
        viewInteraction.perform(scrollTo()).perform(click());
        isChecked().matches(TaskTypeActivity.class);
        Espresso.pressBack();

        viewInteraction = onView(allOf(withId(R.id.addPlantType)));
        viewInteraction.perform(scrollTo()).perform(click());
        isChecked().matches(AddPlantToDbActivity.class);
        Espresso.pressBack();

        logout();

    }
}