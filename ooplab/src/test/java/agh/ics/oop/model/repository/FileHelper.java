package agh.ics.oop.model.repository;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class FileHelper {

    static void deleteTestDirectory(String path) {
        File directory = new File(path);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.delete()) {
                        System.out.println("Couldn't delete file: " + file.getAbsolutePath());
                    }
                }
            }

            if (!directory.delete()) {
                System.out.println("Couldn't delete directory: " + directory.getAbsolutePath());
            }
        }
        assertFalse(directory.exists(), "The directory should be deleted.");
    }
}
