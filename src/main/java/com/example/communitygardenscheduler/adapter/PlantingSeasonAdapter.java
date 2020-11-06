package com.example.communitygardenscheduler.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.communitygardenscheduler.classes.Plant;
import com.example.communitygardenscheduler.R;

import java.util.List;

/* Simple adapter to show plant from DB with its respective preferred season and planting instructions */
public class PlantingSeasonAdapter extends ArrayAdapter<Plant> {
    private Activity context;
    private List<Plant> plantList;

    public PlantingSeasonAdapter(Activity context, List<Plant> plantList) {
        super(context, R.layout.plant_list_layout, plantList);
        this.context = context;
        this.plantList = plantList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.plant_list_layout, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewPlantName);
        TextView textViewSeason = (TextView) listViewItem.findViewById(R.id.textViewPlantSeason);
        TextView textViewInstructions = (TextView) listViewItem.findViewById(R.id.textViewPlanting);
        TextView textViewInformation = (TextView) listViewItem.findViewById(R.id.textViewPlantInformation);

        Plant plant = plantList.get(position);
        textViewName.setText(plant.getName());
        textViewName.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        textViewSeason.setText(plant.getSeason());
        textViewInstructions.setText(plant.getPlantingInstructions());
        textViewInformation.setText(plant.getInformation());

        return listViewItem;
    }
}
