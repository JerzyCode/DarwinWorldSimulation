package agh.ics.oop.model.configuration;

import agh.ics.oop.model.exceptions.WrongConfigurationParameterException;

public class ConfigurationValidator {
    static final int MAX_HEIGHT = 200;
    static final int MAX_WIDTH = 400;
    static final int MIN_HEIGHT = 5;
    static final int MIN_WIDTH = 5;
    static final int MAX_PLANT_GROWTH = 500;
    static final int MAX_GENOME_LENGTH = 100;
    static final int MAX_START_ANIMAL_COUNT = 1000;

    public void validate(Configuration configuration) throws WrongConfigurationParameterException {
        validateWorldMapConfiguration(configuration.getWorldMapConfiguration());
        validateAnimalConfiguration(configuration.getAnimalConfiguration());
        validateSimulationConfiguration(configuration.getSimulationConfiguration());
    }

    private void validateAnimalConfiguration(AnimalConfiguration animalConfiguration) throws WrongConfigurationParameterException {
        if (animalConfiguration.getGenomeLength() > MAX_GENOME_LENGTH) {
            throw new WrongConfigurationParameterException(String.format("Genome length cannot be greater than %s", MAX_GENOME_LENGTH));
        }
        if (animalConfiguration.getStartEnergy() <= 0) {
            throw new WrongConfigurationParameterException("Start energy must be greater than 0");
        }
    }

    private void validateWorldMapConfiguration(WorldMapConfiguration worldMapConfiguration) throws WrongConfigurationParameterException {
        if (worldMapConfiguration.getPlantGrowth() > MAX_PLANT_GROWTH) {
            throw new WrongConfigurationParameterException(String.format("Plant daily growth cannot be greater than %s", MAX_PLANT_GROWTH));
        }
        if (worldMapConfiguration.getWidth() > MAX_WIDTH) {
            throw new WrongConfigurationParameterException(String.format("Width cannot be greater than %s", MAX_WIDTH));
        }
        if (worldMapConfiguration.getWidth() < MIN_WIDTH) {
            throw new WrongConfigurationParameterException(String.format("Width cannot be less than %s", MIN_WIDTH));
        }
        if (worldMapConfiguration.getHeight() > MAX_HEIGHT) {
            throw new WrongConfigurationParameterException(String.format("Height cannot be greater than %s", MAX_HEIGHT));
        }
        if (worldMapConfiguration.getHeight() < MIN_HEIGHT) {
            throw new WrongConfigurationParameterException(String.format("Height cannot be less than %s", MIN_HEIGHT));
        }
    }

    private void validateSimulationConfiguration(SimulationConfiguration simulationConfiguration) throws WrongConfigurationParameterException {
        if (simulationConfiguration.getStartAnimalCount() > MAX_START_ANIMAL_COUNT) {
            throw new WrongConfigurationParameterException(String.format("Start animal count cannot be greater than %s", MAX_START_ANIMAL_COUNT));
        }
    }
}
