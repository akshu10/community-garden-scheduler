package com.example.communitygardenscheduler.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.communitygardenscheduler.R;

import java.util.ArrayList;
import java.util.List;

public class GardenTaskAdapter extends ArrayAdapter<String> {


    private Context mContext;
    private List<String> taskTypeList, taskDescList;

    /**
     * @param context  - The context this adapter will be used in.
     * @param typeList - The list containing the task types.
     * @param descList - The list containing the tasks descriptions.
     */
    public GardenTaskAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage")
    @LayoutRes ArrayList<String> typeList, ArrayList<String> descList) {
        super(context, 0, typeList);
        mContext = context;
        taskTypeList = typeList;
        taskDescList = descList;
    }

    /**
     * @param position    - position on the list view of the Task that is selected by user.
     * @param convertView - The view to be returned after adding items to the list view.
     * @param parent      - The parent of the list view View.
     * @return - the convertedView displaying of listview of items from the taskList.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.garden_task_row_file, parent, false);
        }

        TextView type = listItem.findViewById(R.id.taskType);
        type.setText(taskTypeList.get(position));
        type.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        TextView desc = listItem.findViewById(R.id.taskDesc);
        desc.setText(taskDescList.get(position));

        return listItem;
    }
}
