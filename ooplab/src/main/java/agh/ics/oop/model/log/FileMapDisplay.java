package agh.ics.oop.model.log;

import agh.ics.oop.model.MapChangeEventData;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.WorldMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class FileMapDisplay implements MapChangeListener {
    private static final String LOGS_PATH = "logs";

    public FileMapDisplay() {
        initializeLogDirectory();
    }


    @Override
    public void mapChanged(WorldMap worldMap, MapChangeEventData data) {
        writeLog(worldMap.getId(), data.getMessage());
    }

    private void initializeLogDirectory() {
        File logDirectory = new File(LOGS_PATH);
        if (!logDirectory.exists()) {
            boolean created = logDirectory.mkdirs();
            if (!created) {
                System.out.println("Failed to create logs directory.");
            }
        }
    }


    private void writeLog(UUID mapId, String message) {
        String path = String.format("%s/map_%s.log", LOGS_PATH, mapId.toString());
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, true))) {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.out.println("An error occurred saving log: " + e.getMessage());
        }
    }

}
