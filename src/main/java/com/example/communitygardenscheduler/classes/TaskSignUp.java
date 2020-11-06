package com.example.communitygardenscheduler.classes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class is used with the adapter class and to store the id of the Task on the database.
 */
public class TaskSignUp extends Task {



    static Date today;
    //This variable is used to store the uniqueId from the database, so that it can be usefull
    //to update the database.
    private String Id;

    /**
     * This is the only constructor for this class.
     *
     * @param time - The time the task is designated for (mostly its the date)
     * @param type - The type of task the Task is of ie Prunning
     * @param id   - The Unique Id from the database.
     */
    public TaskSignUp(long time, String type, String id) {
        this.Id = id;
        super.type = type;
        this.time = time;
        today = Calendar.getInstance().getTime();
    }

    /**
     * This is a constructor useful for turning Task objects into TaskSignUp objects
     *
     * @param t     - Task object to be turned into TaskSignUp object
     * @param ID    - Unique firebase ID for the task
     */
    public TaskSignUp(Task t,String ID){
        super(t.getType(),t.getCalendar());
        statusList = t.statusList;
        assignee = t.assignee;
        this.Id = ID;
        today = Calendar.getInstance().getTime();
        this.completeTime = t.completeTime;
    }

    /**
     * @return - Method checks if a task is available to do today.
     */
    public boolean taskIsToday() {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(today);

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);

    }

    /**
     * @return - the string that hold the type of the task.
     */
    public String getType() {
        return type;
    }

    /**
     * @return - the Date of a particular Task.
     */
    public long getDate() {
        return time;
    }

    /**
     * @return - returns the String holding the id of this task so it can be used to update the
     * database.
     */
    public String getId() {
        return Id;
    }

    /**
     * @return - return the String format of the date for a particular Task.
     */
    public String getStringDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        return dateFormat.format(getDate());
    }

    public String getCompletedStringDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        if (completeTime == 0){
            return "N/A";
        }else {
            return dateFormat.format(completeTime);
        }
    }
}
