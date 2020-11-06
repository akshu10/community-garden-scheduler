package com.example.communitygardenscheduler.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PseudoServer {
    private File gardenerFile;
    private ArrayList<String> ExperiencedGardeners;

    public PseudoServer(File file) throws FileNotFoundException {
        gardenerFile = file;
        ExperiencedGardeners = new ArrayList<>();
        Scanner scan = new Scanner(gardenerFile);
        while (scan.hasNextLine()) {
            String uid = scan.nextLine();
            ExperiencedGardeners.add(uid);
        }
    }

    public boolean isAuthorized(String uid) {
        return ExperiencedGardeners.contains(uid);
    }

    public ArrayList<String> getExperiencedGardeners() {
        return  ExperiencedGardeners;
    }

    public File getGardenerFile() {
        return  gardenerFile;
    }

    public void appendString(String uid) throws IOException {
        ExperiencedGardeners.add(uid);
        FileWriter writer = new FileWriter(gardenerFile, true);
        writer.append(uid);
        writer.append("\n");
        writer.close();
    }
}
