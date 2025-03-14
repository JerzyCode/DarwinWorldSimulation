package agh.ics.oop.model.repository;

import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.exceptions.DeleteSaveException;
import agh.ics.oop.model.exceptions.LoadConfigurationException;
import agh.ics.oop.model.exceptions.SaveFailedException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonConfigurationRepositoryAdapter implements ConfigurationRepositoryPort {
    private final List<String> saveNames = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final File directory;
    public static final String JSON_EXTENSION = "%s.json";

    JsonConfigurationRepositoryAdapter(String path) {
        directory = DirectoryInitializer.getDirectory(path);
        loadSaveNames();
    }

    public JsonConfigurationRepositoryAdapter() {
        this("configuration");
    }

    @Override
    public void save(Configuration configuration, String saveName) throws SaveFailedException {
        if (doesSaveExist(saveName)) {
            throw new SaveFailedException(String.format("Configuration with name %s already exists", saveName));
        }

        try {
            var newSave = new File(directory, String.format(JSON_EXTENSION, saveName));
            mapper.writeValue(newSave, configuration);
            saveNames.add(saveName);
        } catch (IOException e) {
            throw new SaveFailedException(String.format("Couldn't save configuration: %s, error: %s", saveName, e.getMessage()));
        }
    }

    @Override
    public void delete(String saveName) throws DeleteSaveException {
        if (!doesSaveExist(saveName)) {
            throw new DeleteSaveException(String.format("Save %s does not exist.", saveName));
        }

        var saveToDelete = new File(directory, String.format(JSON_EXTENSION, saveName));
        var deleted = saveToDelete.delete();

        if (!deleted) {
            throw new DeleteSaveException(String.format("Couldn't delete save %s", saveName));
        } else {
            saveNames.remove(saveName);
        }
    }

    @Override
    public Configuration loadConfiguration(String saveName) throws LoadConfigurationException {
        if (!doesSaveExist(saveName)) {
            throw new LoadConfigurationException(String.format("Save %s no exists", saveName));
        }

        File savedConfiguration = new File(directory, String.format(JSON_EXTENSION, saveName));

        try {
            return mapper.readValue(savedConfiguration, Configuration.class);
        } catch (IOException e) {
            throw new LoadConfigurationException(String.format("Couldn't load save: %s, because: %s", saveName, e.getMessage()));
        }
    }

    @Override
    public List<String> getSaveNames() {
        return saveNames;
    }

    private boolean doesSaveExist(String saveName) {
        return saveNames.stream().anyMatch(el -> el.equals(saveName));
    }


    private void loadSaveNames() {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    saveNames.add(file.getName().replace(".json", ""));
                }
            }
        }
    }
}
