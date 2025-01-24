package agh.ics.oop.model.repository;

import agh.ics.oop.model.exceptions.DirectoryNotCreatedException;

import java.io.File;

class DirectoryHelper {
    static File getDirectory(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            var createdDirectory = directory.mkdirs();
            if (createdDirectory) {
                System.out.println("Created directory");
            } else {
                System.out.println("Failed to create directory");
                throw new DirectoryNotCreatedException("Could not create directory");
            }
        }

        return directory;
    }
}
