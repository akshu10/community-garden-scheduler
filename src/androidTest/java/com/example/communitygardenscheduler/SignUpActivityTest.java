package com.example.communitygardenscheduler;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.communitygardenscheduler.activities.auth.SignUpActivity;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

public class SignUpActivityTest {

    @Rule
    public ActivityTestRule<SignUpActivity> rule = new ActivityTestRule<>(SignUpActivity.class);


    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.communitygardenscheduler", appContext.getPackageName());
    }

    @Test
    public void viewsAreDisplayed() {
        onView(withId(R.id.usernameInputField)).check(matches(isDisplayed()));
        onView(withId(R.id.emailInputField)).check(matches(isDisplayed()));
        onView(withId(R.id.passwordInputField)).check(matches(isDisplayed()));
        onView(withId(R.id.confirmPasswordInputField)).check(matches(isDisplayed()));
        onView(withId(R.id.signUpButton)).check(matches(isDisplayed()));
    }

    @Test
    public void textViewsAreEditable() {
        onView(withId(R.id.usernameInputField)).perform(click()).perform(typeText("Parth"));
        onView(withId(R.id.emailInputField)).perform(click()).perform(typeText("test123"));
        onView(withId(R.id.passwordInputField)).perform(typeText("password"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirmPasswordInputField)).perform(click()).perform(typeText("password"));
        Espresso.closeSoftKeyboard();
    }


    @Test
    public void completeTest() {
        onView(withId(R.id.signUpButton)).perform(click());

    }


    @Test
    public void confirmPasswordErrorDisplayedTest() {
        onView(withId(R.id.emailInputField)).perform(click()).perform(typeText("test123@gmail.com"));
        onView(withId(R.id.passwordInputField)).perform(typeText("password"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.confirmPasswordInputField)).perform(click()).perform(typeText("password2"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.signUpButton)).perform(click());
        onView(withId(R.id.confirmPasswordInputField)).check(matches(hasErrorText("Password doesn't match")));
    }
}
