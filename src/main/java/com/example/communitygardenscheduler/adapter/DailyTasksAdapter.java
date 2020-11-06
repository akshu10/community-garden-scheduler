package com.example.communitygardenscheduler.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.communitygardenscheduler.R;
import com.example.communitygardenscheduler.classes.Task;
import com.example.communitygardenscheduler.classes.TaskSignUp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is an adapter that is useful to customizing our ListView in the TaskListActivity
 */
public class DailyTasksAdapter extends ArrayAdapter<TaskSignUp> {

    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    private Context mContext;
    private List<TaskSignUp> taskList;

    /**
     * @param context - The context this adapter will be used in.
     * @param list    - The list containing items to display on the listview.
     */
    public DailyTasksAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<TaskSignUp> list) {
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
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.daily_tasks_row_file, parent, false);
        }
        // get Task from list
        final TaskSignUp taskSignUp = taskList.get(position);

        // Set task info in list item
        TextView name = listItem.findViewById(R.id.dailyTaskName);
        name.setText(taskSignUp.getType());
        TextView assignee = listItem.findViewById(R.id.assigneeText);
        assignee.setText(taskSignUp.assignee);
        TextView dueDate = listItem.findViewById(R.id.dueDateText);
        dueDate.setText(taskSignUp.getStringDate());

        // set completion date text field
        TextView completeDate = listItem.findViewById(R.id.completeDateText);
        if (taskSignUp.completeTime == 0) {
            // if task is incomplete set text as "N/A"
            completeDate.setText("N/A");
        } else {
            completeDate.setText(taskSignUp.getCompletedStringDate());
        }

        // Set statusTitle text and colour
        TextView statusTitle = listItem.findViewById(R.id.statusText);
        if (taskSignUp.isCompleted()) {
            statusTitle.setText("Complete");
            statusTitle.setBackgroundColor(Color.parseColor("#CEF2C4"));
        } else if (taskSignUp.isCanceled()) {
            statusTitle.setText("Cancelled");
            statusTitle.setBackgroundColor(Color.parseColor("#EDD012"));
        } else if (taskSignUp.isOverdue()) {
            statusTitle.setText("OverDue");
            statusTitle.setBackgroundColor(Color.parseColor("#FF776D"));
        } else if (taskSignUp.isAssigned()) {
            statusTitle.setText("Assigned");
            statusTitle.setBackgroundColor(Color.LTGRAY);
        } else {
            statusTitle.setText("Unassigned");
            statusTitle.setBackgroundColor(Color.parseColor("#81A4DA"));
        }

        // set cancel button text
        Button cancelBtn = listItem.findViewById(R.id.cancelButton);
        if (taskSignUp.isCanceled()) {
            cancelBtn.setText("UnCancel Task");
        } else {
            cancelBtn.setText("Cancel Task");
        }
        // set cancel button listener
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if task is cancelled, uncancel it; if task is uncancelled, cancel it.
                if (taskSignUp.isCanceled()) {
                    unCancelTask(taskSignUp);
                } else {
                    cancelTask(taskSignUp);
                }
                // notify adapter of data change
                DailyTasksAdapter.super.notifyDataSetChanged();
            }
        });

        // set complete button text
        Button completeBtn = listItem.findViewById(R.id.completeButton);
        if (taskSignUp.isCompleted()) {
            completeBtn.setText("unmark complete");
        } else {
            completeBtn.setText("mark complete");
        }
        // set complete button listener
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if task is completed, uncomplete it; if task is uncompleted, complete it.
                if (taskSignUp.isCompleted()) {
                    unCompleteTask(taskSignUp);
                } else {
                    String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                    if (taskSignUp.assignee.equals("") || taskSignUp.assignee.equals(name)) {
                        completeTask(taskSignUp);
                    } else {
                        String message = "Task already assigned to someone else.\n" +
                                "Assignee: " + taskSignUp.assignee + "\n" +
                                "Change assignee to self and set complete?";
                        createDialog("Task already assigned", message, taskSignUp);
                    }
                }
                // notify adapter of data change
                DailyTasksAdapter.super.notifyDataSetChanged();
            }
        });

        // set assign button text
        Button assignBtn = listItem.findViewById(R.id.assignButton);
        if (taskSignUp.isAssigned()) {
            assignBtn.setText("Unassign to me");
        } else {
            assignBtn.setText("Assign to me");
        }
        // disable assign button if task is already assigned to someone else
        if (!taskSignUp.assignee.equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                && !taskSignUp.assignee.equals("")) {
            assignBtn.setEnabled(false);
            assignBtn.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
        } else {
            assignBtn.setEnabled(true);
            assignBtn.getBackground().setColorFilter(null);
        }
        // set assign button listener
        assignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if task is assigned, unassign it; if task is unassigned, assign it.
                if (taskSignUp.isAssigned()) {
                    unAssignTask(taskSignUp);
                } else {
                    assignTask(taskSignUp);
                }
                // notify adapter of data change
                DailyTasksAdapter.super.notifyDataSetChanged();
            }
        });

        return listItem;
    }

    // Set task as complete, set current user as assignee and update DB
    private void completeTask(TaskSignUp task) {
        String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        task.setAssignee(name);
        task.setCompleted();

        Task newTask = new Task(task);
        myRef.child("Tasks").child(task.getId()).setValue(newTask);
    }

    // Set task as complete, leave assignee the same and update DB
    private void completeTaskNoAssigneeChange(TaskSignUp task) {
        task.setCompleted();

        Task newTask = new Task(task);
        myRef.child("Tasks").child(task.getId()).setValue(newTask);
    }

    // Set task as incomplete and update DB
    private void unCompleteTask(TaskSignUp task) {
        task.unSetCompleted();
        Task newTask = new Task(task);
        myRef.child("Tasks").child(task.getId()).setValue(newTask);
    }

    // Set task as cancelled and update DB
    private void cancelTask(TaskSignUp task) {
        task.cancelTask();
        Task newTask = new Task(task);
        myRef.child("Tasks").child(task.getId()).setValue(newTask);
    }

    // Set task as uncancelled and update DB
    private void unCancelTask(TaskSignUp task) {
        task.unCancelTask();
        Task newTask = new Task(task);
        myRef.child("Tasks").child(task.getId()).setValue(newTask);
    }

    // Set tasks assignee to current user and update DB
    private void assignTask(TaskSignUp task) {
        String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        task.setAssignee(name);
        Task newTask = new Task(task);
        myRef.child("Tasks").child(task.getId()).setValue(newTask);
    }

    // Set task as unassigned and update DB
    private void unAssignTask(TaskSignUp task) {
        task.unAssign();
        Task newTask = new Task(task);
        myRef.child("Tasks").child(task.getId()).setValue(newTask);
    }

    // create a dialog for completing already assigned tasks
    private void createDialog(String title, String message, final TaskSignUp task) {
        // create dialog builder and set title and message from method values
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);

        // set OK button to exit the dialog
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Don't Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                completeTaskNoAssigneeChange(task);
                DailyTasksAdapter.super.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        // set button to mark task as complete
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                completeTask(task);
                DailyTasksAdapter.super.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        // create and show the dialog from the builder
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
