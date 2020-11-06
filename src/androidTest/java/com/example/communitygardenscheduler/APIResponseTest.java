/**
 * APIResponseTest.java
 * Ravikumar Patel(B00840678)
 * This class contains the junit code for validating the StringReuest object value.
 */
package com.example.communitygardenscheduler;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.android.volley.toolbox.StringRequest;
import com.example.communitygardenscheduler.activities.MainActivity;
import com.example.communitygardenscheduler.api.APIResponse;

import org.junit.Test;
import org.junit.Rule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class APIResponseTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    private APIResponse apiResponse = new APIResponse();

    /*
     * Test to make sure stringRequest is building correctly.
     * Type: black box
     * Outcome: pass / fail
     */
    @Test
    public void testGetStringRequest() throws InterruptedException {

        StringRequest stringRequest = apiResponse.getStringRequest("Halifax");
        String url = "https://api.openweathermap.org/data/2.5/weather?q=Halifax&appid=d2f6b57f3221fd50be633880bb970124&units=metric";

        assertNotEquals("String request didn't retrieve correctly.", null, stringRequest.getUrl());
        assertNotEquals("String request didn't retrieve correctly.", "", stringRequest.getUrl());
        assertEquals("Url not matching", url, stringRequest.getUrl());

    }


}


