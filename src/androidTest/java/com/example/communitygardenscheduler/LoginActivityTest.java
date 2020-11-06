package com.example.communitygardenscheduler;


import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.communitygardenscheduler.activities.auth.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void loginActivityTest() throws InterruptedException {

        try {
            login();

        } catch (NoMatchingViewException e) {
            ViewInteraction bottomNavigationItemView = onView(
                    allOf(withId(R.id.logout), withContentDescription("Logout"),
                            isDisplayed()));
            bottomNavigationItemView.perform(click());
            login();
        } finally {
            Thread.sleep(1000);
            onView(withId(R.id.welcomeMessage)).check(matches(isDisplayed()));
        }
    }

    private void login() {
        onView(withId(R.id.emailField)).perform(click()).perform(typeText("b@s.com"));
        onView(withId(R.id.passwordField)).perform(click()).perform(typeText("test123"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.loginButton)).perform(click());
    }

}
