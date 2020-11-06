package com.example.communitygardenscheduler.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.communitygardenscheduler.R;
import com.example.communitygardenscheduler.adapter.DailyTasksAdapter;
import com.example.communitygardenscheduler.classes.Task;
import com.example.communitygardenscheduler.classes.TaskSignUp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    Resources res;

    ArrayList<TaskSignUp> taskArray = new ArrayList<>();

    ArrayList<TaskSignUp> daysTasks = new ArrayList<>();
    ArrayList<TaskSignUp> weeksTasks = new ArrayList<>();
    ArrayList<TaskSignUp> completedTasks = new ArrayList<>();
    ArrayList<TaskSignUp> cancelledTasks = new ArrayList<>();
    ArrayList<TaskSignUp> overdueTasks = new ArrayList<>();
    ArrayList<TaskSignUp> assignedTasks = new ArrayList<>();
    ArrayList<TaskSignUp> unassignedTasks = new ArrayList<>();

    DailyTasksAdapter adapter;
    ListView dailyTaskList;

    String[] days = {"sunday","monday","tuesday","wednesday","thursday","friday","saturday"};

    Calendar cal;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        datePicker = findViewById(R.id.datePicker);

        cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        Task.resetAt12(cal);

        dailyTaskList = findViewById(R.id.tasks);

        res = getResources();
        getTasks();


        // When button is pushed go to Main menu
        Button exitBtn = findViewById(R.id.exitButton2);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // close this activity
                finish();
            }
        });

        // When button is pushed go to Main menu
        Button updateBtn = findViewById(R.id.updateButton);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year,month,day;

                // Get date info from picker
                year = datePicker.getYear();
                month = datePicker.getMonth();
                day = datePicker.getDayOfMonth();

                cal.set(year,month,day);
                cal.getTimeInMillis();

                getWeeksTasks();
            }
        });

//        // When button is pushed go to Main menu
//        Button infoBtn = findViewById(R.id.infoButton);
//        infoBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

    private int getIdByName(String name){
        return res.getIdentifier(name,"id", getApplicationContext().getPackageName());
    }

    private void updateDay(String day){
        for (int i = 1;i<=5;i++){
            TextView box = findViewById(getIdByName(day+"Data"+i));
            //box.setText("test"+i);
            switch (i){
                case 1:
                    //completed
                    box.setText(completedTasks.size() + "");
                    break;
                case 2:
                    //cancelled
                    box.setText(cancelledTasks.size() + "");
                    break;
                case 3:
                    //overdue
                    box.setText(overdueTasks.size() + "");
                    break;
                case 4:
                    //assigned
                    box.setText(assignedTasks.size() + "");
                    break;
                case 5:
                    //unassigned
                    box.setText(unassignedTasks.size() + "");
                    break;
            }
        }
    }


    // get tasks from database and add  them  to the taskarray
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

                    taskArray.add(task1);
                }
                getWeeksTasks();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error
            }
        });
    }

    private void getWeeksTasks(){
        cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);

        weeksTasks.clear();

        for (int i = 0;i<7;i++){
            clearArrays();
            for (TaskSignUp t : taskArray){
                if (t.getDate() == cal.getTimeInMillis()){
                    daysTasks.add(t);
                    weeksTasks.add(t);
                }
            }
            sortTasks();
            setDate(cal,i);
            updateDay(days[i]);
            cal.add(Calendar.DATE,1);
        }
        // set the listview adapter
        adapter = new DailyTasksAdapter(CalendarActivity.this, weeksTasks);
        adapter.notifyDataSetChanged();
        dailyTaskList.setAdapter(adapter);
    }

    private void sortTasks(){
        for (TaskSignUp t : daysTasks){
            if(t.isCompleted()){
                completedTasks.add(t);
            }else if(t.isCanceled()){
                cancelledTasks.add(t);
            }else if(t.isOverdue()){
                overdueTasks.add(t);
            }else if(t.isAssigned()){
                assignedTasks.add(t);
            }else {
                unassignedTasks.add(t);
            }
        }

    }

    private void clearArrays(){
        daysTasks.clear();
        completedTasks.clear();
        cancelledTasks.clear();
        overdueTasks.clear();
        assignedTasks.clear();
        unassignedTasks.clear();
    }

    private void setDate(Calendar c, int pos){
        int id = getIdByName(days[pos]+"Date");
        TextView dateText = findViewById(id);
        SimpleDateFormat format = new SimpleDateFormat("dd", Locale.CANADA);
        String day = format.format(c.getTimeInMillis());

        // append proper ordinal indicator suffix
        if (day.equals("01") || day.equals("21") || day.equals("31")){
            dateText.setText(day + "st");
        }else if (day.equals("02") || day.equals("22")){
            dateText.setText(day + "nd");
        }else if (day.equals("03") || day.equals("23")){
            dateText.setText(day + "rd");
        }else{
            dateText.setText(day + "th");
        }
    }

}