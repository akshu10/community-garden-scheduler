package com.example.communitygardenscheduler.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.communitygardenscheduler.adapter.OverdueTaskAdapter;
import com.example.communitygardenscheduler.R;
import com.example.communitygardenscheduler.classes.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OverdueTasksActivity extends AppCompatActivity {

    private ListView overdueTaskUIList;
    private ArrayList<Task> overdueTasks;
    private OverdueTaskAdapter adapter;

    //Getting a reference to the Database.
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overdue_tasks);

        overdueTaskUIList = (ListView) findViewById(R.id.overdueList);
        overdueTasks = new ArrayList<>();

        updateOverdueTasks();
    }

    /**
     * This method is called everytime a user visits this activity and it's purpose is to have a
     * a listener on the database so we know when data has been changed.
     */
    private void updateOverdueTasks() {
        myRef.child("Tasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setOverdueTasks(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error
            }
        });
    }

    void setOverdueTasks(DataSnapshot dataSnapshot){
        // clear the list
        overdueTasks.clear();

        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

            // Get task from database
            Task task = postSnapShot.getValue(Task.class);

            assert task != null;

            //Don't make completed/canceled task overdue.
            if(task.isOverdue()){
                overdueTasks.add(task);
            }
            else if (!(task.isCanceled() || task.isCompleted()) && task.overdue()) {
                overdueTasks.add(task);
                task.setOverdueTask();
                DatabaseReference reference = myRef.child("Tasks").child(postSnapShot.getKey());
                reference.child("statusList").child("2").setValue(true);
            }
        }

        adapter = new OverdueTaskAdapter(this, overdueTasks);
        adapter.notifyDataSetChanged();
        overdueTaskUIList.setAdapter(adapter);
    }
}