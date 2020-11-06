/**
 * TaskClassTest.java
 * Ravikumar Patel(B00840678)
 * This class contains the junit code for Task class.
 */
package com.example.communitygardenscheduler;

import com.example.communitygardenscheduler.classes.Task;

import org.junit.Test;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TaskClassTest {

    /*
     * Test to make sure we get the correct the type of task.
     * Type: black box
     * Outcome: pass / fail
     */
    @Test
    public void type_checking() {
        Task task = new Task("TestType", Calendar.getInstance());

        assertNotEquals("Empty string error!", "",  task.getType());
        assertEquals("Correct type!","TestType", task.getType());
    }

    /*
     * Test to make sure we get the correct the calender object.
     * Type: black box
     * Outcome: pass / fail
     */
    @Test
    public void time_checking() {
        Calendar now = Calendar.getInstance();
        Task task = new Task("TestType", now);

        assertNotEquals("Null time!", null,  task.getCalendar());
        assertEquals("Correct time!",now, task.getCalendar());
    }

    /*
     * Test to make sure we get the correct the response from withInADay.
     * Type: black box
     * Outcome: pass / fail
     */
    @Test
    public void withInADay_checking() {
        Calendar c = Calendar.getInstance();
        long after25Hours = TimeUnit.HOURS.toMillis(25);
        c.setTimeInMillis(c.getTimeInMillis()+ after25Hours);
        Task task = new Task("TestType", c);

        assertFalse("Tomorrow's time!",  task.withInADay());

        c = Calendar.getInstance();
        task = new Task("TestType", c);
        assertTrue("Today's time!", task.withInADay());

        c = Calendar.getInstance();
        c.setTimeInMillis(c.getTimeInMillis()- after25Hours);
        task = new Task("TestType", c);
        assertFalse("Yesterday's time!",  task.withInADay());
    }

    /*
     * Test to make sure we get the correct the response from overdue.
     * Type: black box
     * Outcome: pass / fail
     */
    @Test
    public void overdue_checking() {
        Calendar c = Calendar.getInstance();
        long after25Hours = TimeUnit.HOURS.toMillis(25);

        c.setTimeInMillis(c.getTimeInMillis()+ after25Hours);
        Task task = new Task("TestType", c);
        assertFalse("Tomorrow's time!",  task.overdue());

        c = Calendar.getInstance();
        task = new Task("TestType", c);
        assertFalse("Today's time!", task.overdue());

        c = Calendar.getInstance();
        c.setTimeInMillis(c.getTimeInMillis()- after25Hours);
        task = new Task("TestType", c);
        assertTrue("Yesterday's time!",  task.overdue());
    }

    /*
     * Test to make sure we get the correct the response from getStatus.
     * Type: black box
     * Outcome: pass / fail
     */
    @Test
    public void getStatus_checking() {
        Calendar c = Calendar.getInstance();
        Task task = new Task("TestType", c);

        task.setOverdueTask();
        assertTrue("Wrong overdue status!", task.isOverdue());
        assertFalse("Wrong status after setOverdueTask()!", task.isCanceled());
        assertFalse("Wrong status after setOverdueTask()!", task.isCompleted());

        task.cancelTask();
        assertTrue("Wrong status after cancelTask()!", task.isCanceled());
        assertFalse("Wrong status after cancelTask()!", task.isCompleted());

        task.setCompleted();
        assertTrue("Wrong status after setCompleted()!", task.isCompleted());
    }

    /*
     * Test to make sure we get the correct the response from getAssignee.
     * Type: black box
     * Outcome: pass / fail
     */
    @Test
    public void getAssignee_checking() {
        Calendar c = Calendar.getInstance();
        Task task = new Task("TestType", c);

        assertEquals("Empty assignee name!","", task.getAssignee());

        task.setAssignee("Test");

        assertEquals("Assignee name error!","Test", task.getAssignee());
    }

}
