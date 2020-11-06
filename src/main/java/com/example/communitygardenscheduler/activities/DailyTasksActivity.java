package com.example.communitygardenscheduler.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.communitygardenscheduler.adapter.DailyTasksAdapter;
import com.example.communitygardenscheduler.R;
import com.example.communitygardenscheduler.classes.Task;
import com.example.communitygardenscheduler.classes.TaskSignUp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class DailyTasksActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();

    ListView dailyTaskList;

    ArrayList<TaskSignUp> taskArray = new ArrayList<>();
    DailyTasksAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_tasks);

        dailyTaskList = findViewById(R.id.tasks);

        getTasks();
    }

    // get tasks from database and display the required ones in the listvew
    public void getTasks(){
        myRef.child("Tasks").orderByChild("time").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // reset array
                taskArray.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()){
                    // Get task from database
                    Task t1 = postSnapShot.getValue(Task.class);
                    // Get task's unique ID
                    String key = postSnapShot.getKey();
                    // Create TaskSignUp object from Task object
                    TaskSignUp task1 = new TaskSignUp(t1,key);
                    // TaskSignUP object to ArrayList

                    if (task1.taskIsToday() || (task1.isOverdue() && !task1.isCompleted()
                            && !task1.isCanceled())){
                        taskArray.add(task1);
                    }
                }
                // reverse the array so it is in reverse chronological order
                Collections.reverse(taskArray);

                // set the listview adapter
                adapter = new DailyTasksAdapter(DailyTasksActivity.this, taskArray);
                adapter.notifyDataSetChanged();
                dailyTaskList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error
            }
        });
    }


}