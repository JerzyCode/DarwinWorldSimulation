package agh.ics.oop.model.configuration;

public class Configuration {
  private final WorldMapConfiguration worldMapConfiguration;
  private final AnimalConfiguration animalConfiguration;
  private final SimulationConfiguration simulationConfiguration;

  public Configuration(WorldMapConfiguration worldMapConfiguration, AnimalConfiguration animalConfiguration, SimulationConfiguration simulationConfiguration) {
    this.worldMapConfiguration = worldMapConfiguration;
    this.animalConfiguration = animalConfiguration;
    this.simulationConfiguration = simulationConfiguration;
  }

  public WorldMapConfiguration getWorldMapConfiguration() {
    return worldMapConfiguration;
  }

  public AnimalConfiguration getAnimalConfiguration() {
    return animalConfiguration;
  }

  public SimulationConfiguration getSimulationConfiguration() {
    return simulationConfiguration;
  }
}
