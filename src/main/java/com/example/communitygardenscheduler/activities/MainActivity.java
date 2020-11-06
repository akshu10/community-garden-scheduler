package com.example.communitygardenscheduler.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.communitygardenscheduler.classes.PseudoServer;
import com.example.communitygardenscheduler.R;
import com.example.communitygardenscheduler.api.WeatherTaskScheduler;
import com.example.communitygardenscheduler.activities.auth.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Importing DB reference
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    final String fileName = "ExpGardenerList.txt";
    private File experiencedGardeners;

    // Adding views
    CardView taskCreatorBtn, gardenInformationBtn, calendarBtn, taskListBtn,
            overdueTasksBtn, addTaskType, plantInformation, addPlantType;
    BottomNavigationView bottomNavigationView;
    TextView welcomeMessage;


    // This variable saves the state of a User on the app.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Setting views.
        welcomeMessage = findViewById(R.id.welcomeMessage);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        taskCreatorBtn = findViewById(R.id.taskCreator);
        gardenInformationBtn = findViewById(R.id.gardenInformation);
        calendarBtn = findViewById(R.id.calendarButton);
        taskListBtn = findViewById(R.id.taskList);
        overdueTasksBtn = findViewById(R.id.overDueTasks);
        addTaskType = findViewById(R.id.addTaskType);
        plantInformation = findViewById(R.id.plantInfo);
        addPlantType = findViewById(R.id.addPlantType);


        // Set - OnClickListeners to Buttons
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onNavClick(item);
                return true;
            }
        });
        taskCreatorBtn.setOnClickListener(this);
        gardenInformationBtn.setOnClickListener(this);
        calendarBtn.setOnClickListener(this);
        taskListBtn.setOnClickListener(this);
        overdueTasksBtn.setOnClickListener(this);
        addTaskType.setOnClickListener(this);
        plantInformation.setOnClickListener(this);
        addPlantType.setOnClickListener(this);


        // Reading file.
        readFile();

        // Authentication Stuff.
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        manageSession();
        necessaryObjectRun();
    }

    private void readFile() {
        experiencedGardeners = new File(this.getApplicationContext().getFilesDir(), fileName);

        // Create a new file if one doesn't exist.
        if (!experiencedGardeners.exists()) {
            createFile();
        }
    }

    private void onNavClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                mAuth.signOut();
                // Prevents back key after user has logged out.
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
            case R.id.calendar:
                startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * This method creates a new ExperiencedGardenersFile.
     */
    private void createFile() {

        try {
            experiencedGardeners.createNewFile();
            PseudoServer server = new PseudoServer(experiencedGardeners);
            server.appendString("4kYXezI5v6fBemYYlRHSd3vHUXj2");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method manages User Session on this activity.
     */
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    private void manageSession() {

        if (user == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

        } else {
            String username = user.getDisplayName() == null ? "" : user.getDisplayName();

            try {

                //Do not display views if user is not experienced.
                if (!isExperienced(user.getUid())) {
                    addTaskType.setVisibility(View.GONE);
                    addPlantType.setVisibility(View.GONE);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // Display welcome message.
            welcomeMessage.setText("Welcome  " + username);
        }
    }

    /**
     * @param userId - UserID of this User.
     * @return - true if user is a Experienced User.
     * <p>
     * This method checks whether a user is an Experienced Gardener.
     * </p>
     */
    public boolean isExperienced(String userId) throws FileNotFoundException {

        PseudoServer server = new PseudoServer(experiencedGardeners);
        return server.isAuthorized(userId);
    }

    void necessaryObjectRun() {
        WeatherTaskScheduler.checkUpdates(this.getApplicationContext(), "Halifax");
    }

    /**
     * This method is an Onclick Listener for all the buttons on this activity.
     *
     * @param v - View of the MainActivity
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            // TaskCreator Activity
            case R.id.taskCreator:

                startActivity(new Intent(getApplicationContext(), TaskCreatorActivity.class));
                break;
            // GardenTaskInformation.
            case R.id.gardenInformation:

                startActivity(new Intent(getApplicationContext(), GardenTaskInformationActivity.class));
                break;

            // TaskSignUpActivity
            case R.id.calendarButton:

                startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                break;

            // DailyTaskActivity
            case R.id.taskList:

                startActivity(new Intent(getApplicationContext(), DailyTasksActivity.class));
                break;

            // OverDueTasks Activity
            case R.id.overDueTasks:

                startActivity(new Intent(getApplicationContext(), OverdueTasksActivity.class));
                break;

            // TaskTypeActivity
            case R.id.addTaskType:

                startActivity(new Intent(getApplicationContext(), TaskTypeActivity.class));
                break;

            // Add Plant to DB
            case R.id.plantInfo:
                startActivity(new Intent(getApplicationContext(), PlantListActivity.class));
                break;

            // Add new Plant type Activity
            case R.id.addPlantType:

                startActivity(new Intent(getApplicationContext(), AddPlantToDbActivity.class));
                break;

            default:
                break;
        }
    }
}
