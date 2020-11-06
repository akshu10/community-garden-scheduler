package com.example.communitygardenscheduler.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.communitygardenscheduler.classes.Plant;
import com.example.communitygardenscheduler.R;
import com.example.communitygardenscheduler.adapter.PlantingSeasonAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PlantListActivity extends AppCompatActivity {

    ListView listViewSeasonalPlants;
    DatabaseReference databasePlants = FirebaseDatabase.getInstance().getReference("Plants");
    List<Plant> plantList;
    PlantingSeasonAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_season);
        listViewSeasonalPlants = (ListView) findViewById(R.id.listViewSeasonalPlants);
        plantList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        /* Get plants from firebase and display to our listview via PlantSeasonList adapter */
        ValueEventListener valueEventListener = databasePlants.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                plantList.clear();
                for (DataSnapshot plantSnapshot : dataSnapshot.getChildren()) {
                    Plant plant = plantSnapshot.getValue(Plant.class);
                    plantList.add(plant);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        adapter = new PlantingSeasonAdapter(PlantListActivity.this, plantList);
        listViewSeasonalPlants.setAdapter(adapter);
    }
}