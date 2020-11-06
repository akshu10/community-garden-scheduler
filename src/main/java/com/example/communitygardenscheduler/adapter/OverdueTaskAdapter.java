package com.example.communitygardenscheduler.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.communitygardenscheduler.R;
import com.example.communitygardenscheduler.classes.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OverdueTaskAdapter extends ArrayAdapter<Task> {

    private Context mContext;
    private List<Task> taskList;

    /**
     * @param context - The context this adapter will be used in.
     * @param list    - The list containing items to display on the listview.
     */
    public OverdueTaskAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<Task> list) {
        super(context, 0, list);
        mContext = context;
        taskList = list;
    }

    /**
     * @param position    - position on the list view of the Task that is selected by user.
     * @param convertView - The view to be returned after adding items to the list view.
     * @param parent      - The parent of the list view View.
     * @return - the convertedView displaying of listview of items from the taskList.
     */
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.layout_overdue_task, parent, false);
        }

        Task task = taskList.get(position);

        TextView name = listItem.findViewById(R.id.txtName);
        name.setText(task.getType());
        TextView time = listItem.findViewById(R.id.txtTime);
        time.setText(dateFormat.format(task.getCalendar().getTime()));
        TextView assignee = listItem.findViewById(R.id.txtAssignee);
        assignee.setText(task.getAssignee());

        TextView status = listItem.findViewById(R.id.txtStatus);
        if (task.isCompleted()) {
            status.setText("Completed!");
            status.setBackgroundColor(Color.parseColor("#CEF2C4"));
        } else if (task.isCanceled()) {
            status.setText("Canceled!");
            status.setBackgroundColor(Color.parseColor("#EDD012"));
        } else {
            status.setText("None!");
            status.setBackgroundColor(Color.LTGRAY);
        }
        return listItem;
    }
}
