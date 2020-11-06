package com.example.communitygardenscheduler;

import com.example.communitygardenscheduler.classes.Plant;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class PlantTest {

    // constructors succesfully create object (no exception thrown)
    @Test
    public void testPlantConstructor() {
        new Plant();

        Plant plant = new Plant("Daffodil", "Summer", "It's a weed.", "Test soil");
        assertEquals("Daffodil", plant.getName());
        assertEquals("Summer", plant.getSeason());
        assertEquals("It's a weed.", plant.getPlantingInstructions());
        assertEquals("Test soil", plant.getInformation());
    }
}
