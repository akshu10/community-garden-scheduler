/**
 * WeatherTaskScheduler.java
 * Ravikumar Patel(B00840678)
 * This class contains methods for canceling the task within 12 hours of particular event happening.
 */
package com.example.communitygardenscheduler.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.toolbox.StringRequest;
import com.example.communitygardenscheduler.classes.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class WeatherTaskScheduler {
    static private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    static private HashMap<String,String> weatherToTaskType = new HashMap<>();
    static private DataSnapshot dataSnap;

    /**
     * The constructor for adding listener to cancel task when particular event occur.
     */
    public WeatherTaskScheduler(Context context, String city){
        checkUpdates(context, city);
    }

    public static void checkUpdates(Context context, String city){
        APIRequestMaker.getInstance(context);

        StringRequest stringRequest = APIResponse.getStringRequest(city);
        APIRequestMaker.getInstance(context).addQueue(stringRequest);

        weatherToTaskType.put("Rain","Watering");
        myRef.child("Tasks").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnap = dataSnapshot;

                if(APIResponse.getTemperature() != Double.MAX_VALUE)
                    update();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error
            }
        });
    }

    /**
     * This method is used to update the tasks, if needed.
     */
    static void update(){
        if(needUpdate())
            cancelTask();

        if(APIResponse.getTemperature() >= 35.0)
            createTask();
    }

    /**
     * This method is used to add watering task for today.
     */
    private static void createTask(){
        Task t = new Task(weatherToTaskType.get("Rain"), Calendar.getInstance());
        myRef.child("Tasks").push().setValue(t);
    }

    /**
     * This method is used to check whether task update need or not
     *
     * @return boolean -- it returns boolean value representing update need or not
     */
    private static boolean needUpdate(){
        return weatherToTaskType.containsKey(APIResponse.getWeatherMain());
    }

    /**
     * This method is used to cancel the task if update requires
     */
    private static void cancelTask(){
        String type = weatherToTaskType.get(APIResponse.getWeatherMain());
        if(dataSnap == null) {
            return;
        }
        for (DataSnapshot postSnapShot : dataSnap.getChildren()) {

            Task task = postSnapShot.getValue(Task.class);

            // check if task is within a day and type is matched with current weather activity
            assert type != null;
            if (type.equals(task.getType()) && task.withInADay()) {
                DatabaseReference reference = myRef.child("Tasks").child(postSnapShot.getKey());
                reference.child("statusList").child("3").setValue(true);
            }

        }
    }
}
