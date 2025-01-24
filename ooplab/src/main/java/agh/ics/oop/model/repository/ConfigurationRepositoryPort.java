package agh.ics.oop.model.repository;

import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.exceptions.DeleteSaveException;
import agh.ics.oop.model.exceptions.LoadConfigurationException;
import agh.ics.oop.model.exceptions.SaveFailedException;

import java.util.List;

public interface ConfigurationRepositoryPort {

    void save(Configuration configuration, String saveName) throws SaveFailedException;

    void delete(String saveName) throws DeleteSaveException;

    Configuration loadConfiguration(String saveName) throws LoadConfigurationException;

    List<String> getSaveNames();
}
