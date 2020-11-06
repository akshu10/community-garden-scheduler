package com.example.communitygardenscheduler.classes;

import java.util.ArrayList;
import java.util.Calendar;

public class Task {

    public String type, assignee;
    public long time, completeTime;

    public ArrayList<Boolean> statusList = new ArrayList<>(4);
    /* List of Task statuses
        [0] Assigned
        [1] Complete
        [2] OverDue
        [3] Cancelled
     */

    Task() {

    }

    public Task(String type, Calendar date) {
        this.type = type;
        resetAt12(date);
        time = date.getTimeInMillis();
        completeTime = 0;
        assignee = "";
        statusList.add(false);   // Assigned
        statusList.add(false);   // Complete
        statusList.add(false);   // OverDue
        statusList.add(false);   // Cancelled
    }

    // new constructor to allow casting of TaskSignUp objects to firebase
    public Task(TaskSignUp oldTask) {
        this.time = oldTask.time;
        this.statusList = oldTask.statusList;
        this.assignee = oldTask.assignee;
        this.type = oldTask.type;
        this.completeTime = oldTask.completeTime;
    }

    /**
     * This method is used to set the task as completed
     */
    public void setCompleted() {
        statusList.set(1, true);
        completeTime = resetAt12(Calendar.getInstance()).getTimeInMillis();
    }

    public void unSetCompleted() {
        statusList.set(1, false);
        completeTime = 0;
    }

    public void setCompleteTime(long t) {
        completeTime = t;
    }

    /**
     * This method is used to set the task as overdue
     */
    public void setOverdueTask() {
        statusList.set(2, true);
    }

    public void unSetOverdueTask() {
        statusList.set(2, true);
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
        if (assignee.equals("")) {
            statusList.set(0, false);
        } else {
            statusList.set(0, true);
        }
    }

    public void unAssign() {
        statusList.set(0, false);
        assignee = "";
    }

    /**
     * This method is used to set the task as cancelled
     */
    public void cancelTask() {
        statusList.set(3, true);
    }

    public void unCancelTask() {
        statusList.set(3, false);
    }

    /**
     * This method is used to return assignee name
     *
     * @return String -- representing assignee name
     */
    public String getAssignee() {
        return assignee;
    }

    /**
     * This method is used to return if the task is assigned
     *
     * @return boolean -- representing task is Assigned or not
     */
    public boolean isAssigned() {
        return statusList.get(0);
    }

    /**
     * This method is used to return if the task is Completed
     *
     * @return boolean -- representing task is Completed or not
     */
    public boolean isCompleted() {
        return statusList.get(1);
    }

    /**
     * This method is used to return if the task is overdue
     *
     * @return boolean -- representing task is overdue or not
     */
    public boolean isOverdue() {
        return statusList.get(2);
    }

    /**
     * This method is used to return if the task is canceled
     *
     * @return boolean -- representing task is canceled or not
     */
    public boolean isCanceled() {
        return statusList.get(3);
    }

    public long getTime() {
        return time;
    }

    /**
     * This method is used to return the type of the task
     *
     * @return String -- representing task type
     */
    public String getType() {
        return type;
    }

    /**
     * This method is used to get calendar instance of the task time
     *
     * @return Calendar -- represent the calendar object of task time
     */
    public Calendar getCalendar() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        return c;
    }

    /**
     * This method is used to reset the given calendar object to 12 o'clock
     *
     * @param c -- calendar object that needs to reset to 12 o'clock
     * @return calendar -- object that is reset to 12 o'clock
     */
    public static Calendar resetAt12(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    /**
     * This method is used to check whether the task is for today or not
     *
     * @return boolean -- it returns boolean value representing whether the task is for today or not
     */
    public boolean withInADay() {
        Calendar rightNow = resetAt12(Calendar.getInstance());
        Calendar task_time = resetAt12(this.getCalendar());

        return rightNow.compareTo(task_time) == 0;
    }

    /**
     * This method is used to check whether the task is overdue
     *
     * @return boolean -- it returns boolean value representing whether the task is overdue
     */
    public boolean overdue() {
        Calendar rightNow = resetAt12(Calendar.getInstance());
        Calendar task_time = resetAt12(this.getCalendar());

        return task_time.compareTo(rightNow) < 0;
    }
}
