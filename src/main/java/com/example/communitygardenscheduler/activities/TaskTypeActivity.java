package com.example.communitygardenscheduler.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.communitygardenscheduler.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TaskTypeActivity extends AppCompatActivity {

    Button addButton;
    EditText editText, descriptionText;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private List<String> myTaskTypeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_type);

        //Set views
        addButton = findViewById(R.id.addButton);
        editText = findViewById(R.id.addTaskTypeInput);
        descriptionText = findViewById(R.id.addTaskDescriptionBox);
        findViewById(R.id.emptyFieldError).setVisibility(View.INVISIBLE);
        findViewById(R.id.emptyFieldError2).setVisibility(View.INVISIBLE);

        //Get already defined task types from database.
        accessDatabase();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String taskType = editText.getText().toString();
                String taskDescription = descriptionText.getText().toString();

                if (taskType.equals("")) {
                    findViewById(R.id.emptyFieldError).setVisibility(View.VISIBLE);
                } else if (taskDescription.equals("")){
                    findViewById(R.id.emptyFieldError2).setVisibility(View.VISIBLE);
                } else {
                    if (isDuplicate(taskType)) {
                        findViewById(R.id.emptyFieldError).setVisibility(View.INVISIBLE);
                        findViewById(R.id.emptyFieldError2).setVisibility(View.INVISIBLE);
                        Toast toast = Toast.makeText(getApplicationContext(), "Task already exists", Toast.LENGTH_SHORT);
                        toast.show();

                    } else {
                        findViewById(R.id.emptyFieldError).setVisibility(View.INVISIBLE);
                        findViewById(R.id.emptyFieldError2).setVisibility(View.INVISIBLE);
                        pushTasks(taskType,taskDescription);
                        Toast toast = Toast.makeText(getApplicationContext(), "Task Added", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                editText.setText("");
            }
        });
    }

    private void pushTasks(String taskType, String desc) {
        String key = myRef.child("TaskTypes").push().getKey();
        myRef.child("TaskTypes").child(key).child("Type").setValue(taskType);
        myRef.child("TaskTypes").child(key).child("Description").setValue(desc);
    }


    private void accessDatabase() {

        myRef.child("TaskTypes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String taskType = postSnapshot.child("Type").getValue(String.class);
                    myTaskTypeList.add(taskType);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error
            }
        });
    }


    private boolean isDuplicate(String taskName) {

        for (String taskType : myTaskTypeList) {

            if (taskType.equalsIgnoreCase(taskName)) {
                return true;
            }
        }
        return false;
    }


}