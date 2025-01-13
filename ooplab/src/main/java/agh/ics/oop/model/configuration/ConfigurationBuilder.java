package agh.ics.oop.model.configuration;

public class ConfigurationBuilder {
    private AnimalConfiguration animalConfiguration;
    private SimulationConfiguration simulationConfiguration;
    private WorldMapConfiguration worldMapConfiguration;


    private ConfigurationBuilder() {
    }

    public static ConfigurationBuilder create() {
        return new ConfigurationBuilder();
    }

    public ConfigurationBuilder animalConfiguration(AnimalConfiguration animalConfiguration) {
        this.animalConfiguration = animalConfiguration;
        return this;
    }

    public ConfigurationBuilder simulationConfiguration(SimulationConfiguration simulationConfiguration) {
        this.simulationConfiguration = simulationConfiguration;
        return this;
    }

    public ConfigurationBuilder worldMapConfiguration(WorldMapConfiguration worldMapConfiguration) {
        this.worldMapConfiguration = worldMapConfiguration;
        return this;
    }

    public Configuration build() {
        return new Configuration(worldMapConfiguration, animalConfiguration, simulationConfiguration);
    }
}
