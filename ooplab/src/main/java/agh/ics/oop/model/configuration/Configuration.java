package agh.ics.oop.model.configuration;

public class Configuration {
  private final WorldMapConfiguration worldMapConfiguration;
  private final AnimalConfiguration animalConfiguration;
  private final SimulationConfiguration simulationConfiguration;

  public Configuration() {
    this.worldMapConfiguration = new WorldMapConfiguration();
    this.animalConfiguration = new AnimalConfiguration();
    this.simulationConfiguration = new SimulationConfiguration();
  }

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

  @Override
  public String toString() {
    return "Configuration{" +
        "worldMapConfiguration=" + worldMapConfiguration +
        ", animalConfiguration=" + animalConfiguration +
        ", simulationConfiguration=" + simulationConfiguration +
        '}';
  }

}
