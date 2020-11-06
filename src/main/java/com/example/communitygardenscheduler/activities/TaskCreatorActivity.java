package com.example.communitygardenscheduler.activities;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.communitygardenscheduler.R;
import com.example.communitygardenscheduler.classes.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskCreatorActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    EditText repeatOffsetText, repeatNumberText;
    DatePicker datePicker;
    Spinner taskSpinner;
    Boolean repeats = false;
    TextView offsetError,numberError,errorStar;

    ArrayList<Task> newTasks = new ArrayList<>();
    ArrayList<Task> taskList = new ArrayList<>();

    List<String> taskTypes = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creator);

        // get a list of the types of tasks that can be created
        getTaskTypes();

        // set views by id
        repeatOffsetText = findViewById(R.id.taskrepeatOffset);
        repeatNumberText = findViewById(R.id.taskrepeatNumber);
        datePicker = findViewById(R.id.datePicker);
        taskSpinner = findViewById(R.id.spinner);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, taskTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskSpinner.setAdapter(adapter);

        errorStar = findViewById(R.id.errorMessageText);
        numberError = findViewById(R.id.repeatNumberError);
        offsetError = findViewById(R.id.repeatOffsetError);

        // set error messages to invisible
        errorStar.setVisibility(View.INVISIBLE);
        numberError.setVisibility(View.INVISIBLE);
        offsetError.setVisibility(View.INVISIBLE);

        // Read repeatSwitch and set repeats Boolean to switch value
        Switch repeatSwitch = findViewById(R.id.repeatSwitch);
        repeatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                repeats = isChecked;
            }
        });

        // When button is pushed read input data and call addTask()
        Button createBtn = findViewById(R.id.createTaskButton);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar date;
                int year,month,day;
                String taskType;

                // Get date info from picker
                year = datePicker.getYear();
                month = datePicker.getMonth();
                day = datePicker.getDayOfMonth();

                // get task type from spinner
                taskType = taskSpinner.getSelectedItem().toString();

                // Create Calender instance and set its date
                date = Calendar.getInstance();
                date.set(year,month,day);

                // Load all current tasks
                getTasks();

                // If the task is to repeat:
                if (repeats){
                    boolean errorFree = true;
                    // check for empty repeat offset text and enable error text
                    if(repeatOffsetText.getText().toString().equals("")){
                        errorStar.setVisibility(View.VISIBLE);
                        offsetError.setVisibility(View.VISIBLE);
                        errorFree = false;
                    }else {
                        // disable repeat offset error text
                        offsetError.setVisibility(View.INVISIBLE);
                    }
                    // check for empty repeat number text and enable error text
                    if (repeatNumberText.getText().toString().equals("")){
                        errorStar.setVisibility(View.VISIBLE);
                        numberError.setVisibility(View.VISIBLE);
                        errorFree = false;
                    }else {
                        // disable repeat number error text
                        numberError.setVisibility(View.INVISIBLE);
                    }
                    // if no errors were found
                    if(errorFree){
                        // Disable error message
                        errorStar.setVisibility(View.INVISIBLE);

                        // Read in how often the task happens
                        int n = Integer.parseInt(repeatOffsetText.getText().toString());
                        int j = Integer.parseInt(repeatNumberText.getText().toString());
                        // Create j copies of the task at different dates
                        int i;
                        for (i = 0;i < j;i++){
                            addTask(taskType,date);
                            date.add(Calendar.DATE,n);
                        }

                        // Check for and remove duplicates and save the number removed
                        int duplicates = checkDuplicates();

                        // create confirmation message
                        String message =    "Type: " + taskType +
                                            "\nFirst Date: " + year + "/" + (month+1) + "/" + day +
                                            "\nNumber created: " + (i-duplicates) +
                                            "\nEvery " + n + " days" +
                                            "\nDuplicates: "  + duplicates;
                        createDialog("Task(s) Created",message);
                    }
                }else {
                    // Otherwise create a single task
                    addTask(taskType,date);

                    // Check for and remove duplicates and save the number removed
                    int duplicates = checkDuplicates();

                    // create confirmation message
                    String message =    "Type: " + taskType +
                                        "\nDate: " + year + "/" + (month+1) + "/" + day +
                                        "\nNumber created: " + (1-duplicates);
                    // display different message title based on if task is duplicate
                    if (duplicates > 0){
                        createDialog("Duplicate Task",message);
                    }else {
                        createDialog("Task Created",message);
                    }

                }
                // send tasks to firebase
                pushTasks();
            }
        });

        // When button is pushed go to Main menu
        Button exitBtn = findViewById(R.id.exitButton);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // close this activity
                finish();
            }
        });
    }

    // create a task and add the new task to newTasks arraylist
    private void addTask(String type, Calendar date){
        Task task1 = new Task(type,date);
        newTasks.add(task1);
    }

    // create a dialog popup
    private void createDialog(String title, String message){
        // create dialog builder and set title and message from method values
        AlertDialog.Builder builder = new AlertDialog.Builder(TaskCreatorActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);

        // set OK button to exit the dialog
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // create and show the dialog from the builder
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Retrieve a list of tasks from the firebase into taskList
    public void getTasks(){
        myRef.child("Tasks").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                taskList.clear();
                for (DataSnapshot snaps : dataSnapshot.getChildren()){
                    Task t1 = snaps.getValue(Task.class);
                    taskList.add(t1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Check for tasks in newTask that are already in the firebase
    public int checkDuplicates(){
        int duplicates = 0;

        // temp ArrayList to avoid error on removal of items
        ArrayList<Task> temp = new ArrayList<>(newTasks);

        for (Task newTask : newTasks){
            for (Task t: taskList){
                if(newTask.getTime() == t.getTime() && newTask.getType().equals(t.getType())){
                    temp.remove(newTask);
                    duplicates++;
                    // break if a task has one duplicate in the firebase, no need to see if it has more
                    break;
                }
            }
        }
        // update newTasks arrayList to not include any duplicate tasks
        newTasks = temp;

        return duplicates;
    }

    // send the tasks to the firebase and clear the ArrayList
    public void pushTasks(){
        for (Task t : newTasks){
            myRef.child("Tasks").push().setValue(t);
        }
        newTasks.clear();
    }

    // get a list of the types of tasks tat can be created from firebase
    private void getTaskTypes(){
        myRef.child("TaskTypes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snaps : dataSnapshot.getChildren()){
                    taskTypes.add(snaps.child("Type").getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
