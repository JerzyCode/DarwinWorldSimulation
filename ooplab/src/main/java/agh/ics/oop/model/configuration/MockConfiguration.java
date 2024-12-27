package agh.ics.oop.model.configuration;

public class MockConfiguration {

  public static Configuration get() {
    var animalConfiguration = new AnimalConfiguration(
        0,
        0,
        0,
        MutationVariant.FULL_RANDOM,
        0
    );

    var simulationConfiguration = new SimulationConfiguration(
        0,
        1,
        PlantVariant.FORESTED_EQUATORS,
        0,
        0,
        0,
        5,
        3
    );

    var mapConfiguration = new WorldMapConfiguration(
        5,
        5,
        WorldMapVariant.GRASS_FIELD
    );

    return new Configuration(
        mapConfiguration,
        animalConfiguration,
        simulationConfiguration
    );
  }
}
