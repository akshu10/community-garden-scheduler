package com.example.communitygardenscheduler.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.communitygardenscheduler.R;
import com.example.communitygardenscheduler.adapter.GardenTaskAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GardenTaskInformationActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();

    ArrayList<String> taskTypeArray = new ArrayList<>();
    ArrayList<String> taskDescArray = new ArrayList<>();
    GardenTaskAdapter adapter;

    ListView gardenTaskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_task_information);
        setTitle("Garden Task Information");

        gardenTaskList = findViewById(R.id.taskDescriptions);

        getTasks();
    }

    // get tasks from database and display the required ones in the listvew
    public void getTasks(){
        myRef.child("TaskTypes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // reset array
                taskTypeArray.clear();
                taskDescArray.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()){
                    // Get task type from database
                    taskTypeArray.add(postSnapShot.child("Type").getValue().toString());
                    taskDescArray.add(postSnapShot.child("Description").getValue().toString());
                }


                // set the listview adapter
                adapter = new GardenTaskAdapter(GardenTaskInformationActivity.this, taskTypeArray, taskDescArray);
                adapter.notifyDataSetChanged();
                gardenTaskList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error
            }
        });
    }

}