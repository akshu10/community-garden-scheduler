package com.example.communitygardenscheduler;

import com.example.communitygardenscheduler.classes.PseudoServer;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

public class PseudoServerTest {
    File file;

    public  PseudoServerTest() {
        file = new File("src/main/java/com/example/communitygardenscheduler/ExpGardenerList.txt");
    }


    @Test
    public void isAuthorized() throws FileNotFoundException {
        PseudoServer tserver = new PseudoServer(file);
        assertTrue(tserver.isAuthorized("4kYXezI5v6fBemYYlRHSd3vHUXj2"));
        assertFalse(tserver.isAuthorized(null));
        assertFalse(tserver.isAuthorized(""));
    }

    @Test
    public void appendString() throws IOException {
        PseudoServer tServer = new PseudoServer(file);
        ArrayList<String> experiencedGardeners = tServer.getExperiencedGardeners();
        ArrayList<String> oldValues= new ArrayList<>();
        for (String s : experiencedGardeners
             ) {
            oldValues.add(s);
        }
        File gardenerFile = tServer.getGardenerFile();
        tServer.appendString("test");
        Scanner tScan = new Scanner(gardenerFile);
        String line = tScan.nextLine();
        assertEquals(line,"4kYXezI5v6fBemYYlRHSd3vHUXj2" );
        while (tScan.hasNextLine()) {
            line = tScan.nextLine();
        }
        assertEquals(line, "test");
        FileWriter writer = new FileWriter(gardenerFile);
        for (String s : oldValues
             ) {
            writer.append(s);
            writer.append("\n");
        }
        writer.close();
    }
}