package com.example.communitygardenscheduler.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.communitygardenscheduler.classes.Plant;
import com.example.communitygardenscheduler.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.widget.Toast.makeText;

public class AddPlantToDbActivity extends AppCompatActivity {

    public static final String ADDED = "Added ";
    public static final String TO_DATABASE = " to database.";
    public static final String ERROR = "Error, ";
    public static final String DATABASE = " already exists in database.";
    public static final String SUCCESSFULLY_ADDED = "Successfully added ";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    ArrayList<Plant> plantList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant_to_db);

        Button submitBtn = findViewById(R.id.submitPlantBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText plantName = findViewById(R.id.editTextPlantName);
                Spinner seasonSpinner = findViewById(R.id.seasonSpinner);
                EditText plantingInstructions = findViewById(R.id.editTextPlantInstructions);
                EditText plantInformation = findViewById(R.id.editTextPlantInformation);

                String name = plantName.getText().toString();
                String season = seasonSpinner.getSelectedItem().toString();
                String instructions = plantingInstructions.getText().toString();
                String information = plantInformation.getText().toString();

                if (!isDuplicate(name, season)) {
                    addPlant(name, season, instructions, information);
                    Toast toast = Toast.makeText(getApplicationContext(), ADDED + name + TO_DATABASE, Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), ERROR + name + DATABASE, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    // Fills arraylist plantList with all current plants listed in the firebase DB
    public void getPlants() {
        myRef.child("Plants").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                plantList.clear();
                for (DataSnapshot plantSnapshot : dataSnapshot.getChildren()) {
                    Plant plant = plantSnapshot.getValue(Plant.class);
                    plantList.add(plant);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /* Returns true if given plant already exists in firebase DB */
    private boolean isDuplicate(String name, String season) {
        getPlants();
        for (Plant p : plantList)
            if (p.getName().equals(name) && p.getSeason().equals(season))
                return true;
        return false;
    }

    /* add plant along with description to firebase */
    private void addPlant(String name, String season, String instructions, String information) {
        Plant plant = new Plant(name, season, instructions, information);
        myRef.child("Plants").push().setValue(plant);
        Toast toast = Toast.makeText(getApplicationContext(), SUCCESSFULLY_ADDED + name + TO_DATABASE, Toast.LENGTH_LONG);
        toast.show();
    }
}