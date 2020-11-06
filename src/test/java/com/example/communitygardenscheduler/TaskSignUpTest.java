package com.example.communitygardenscheduler;

import com.example.communitygardenscheduler.classes.TaskSignUp;

import org.junit.Test;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This is a jUnit Test class for testing the methods in the TaskSignUp class
 */
public class TaskSignUpTest {

    public static final String WATER = "Water";
    public static final String ID = "ABCD";
    long today;
    TaskSignUp taskSignUp;
    final Calendar calendar = Calendar.getInstance();


    /**
     * Test to check whether the taskIsToday() method returns true when the task is scheduled
     * today.
     */
    @Test
    public void taskIsTodayTest1() {
        today = Calendar.getInstance().getTimeInMillis();
        taskSignUp = new TaskSignUp(today, WATER, ID);
        assert (taskSignUp.taskIsToday());

    }

    /**
     * Test to check whether the taskIsToday() method returns false when the task is scheduled
     * is overdue.
     */
    @Test
    public void taskIsTodayTest2() {
        calendar.add( Calendar.DATE,-1);
        long yesterday = calendar.getTimeInMillis();
        taskSignUp = new TaskSignUp(yesterday, WATER, ID);
        assertFalse(taskSignUp.taskIsToday());
    }

    /**
     * Test to check whether the taskIsToday() method returns false when the task is scheduled
     * in the future.
     */
    @Test
    public void taskIsTodayTest3() {
        calendar.add(Calendar.DATE, 2);
        long dayAfterTomorrow = calendar.getTimeInMillis();
        taskSignUp = new TaskSignUp(dayAfterTomorrow, WATER, ID);
        assertFalse(taskSignUp.taskIsToday());

    }

    /**
     * Test to check whether the getter fot String type is working properly.
     */
    @Test
    public void testGetType() {
        today = Calendar.getInstance().getTimeInMillis();
        taskSignUp = new TaskSignUp(today, WATER, ID);
        assertEquals(WATER, taskSignUp.getType());
    }

    /**
     * Test to check whether the getter fot String ID is working properly.
     */
    @Test
    public void testGetID() {
        today = Calendar.getInstance().getTimeInMillis();
        taskSignUp = new TaskSignUp(today, WATER, ID);
        assertEquals(ID, taskSignUp.getId());
    }

    /**
     * Test to check whether the getStringDate method is working properly.
     */
    @Test
    public void testGetStringDate() {
        today = Calendar.getInstance().getTimeInMillis();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String expected = format.format(today);

        taskSignUp = new TaskSignUp(today, WATER, ID);
        assertEquals(expected, taskSignUp.getStringDate());
    }
}
