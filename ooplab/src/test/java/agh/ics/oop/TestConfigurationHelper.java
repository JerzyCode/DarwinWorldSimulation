package agh.ics.oop;

import agh.ics.oop.model.configuration.*;

public class TestConfigurationHelper {

    private static AnimalConfiguration defaultAnimalConfiguration() {
        return AnimalConfiguration.builder()
                .startEnergy(15)
                .minimumMutationCount(0)
                .maximumMutationCount(0)
                .wellFedEnergy(10)
                .lossCopulateEnergy(10)
                .mutationVariant(MutationVariant.FULL_RANDOM)
                .genomeLength(7)
                .build();
    }

    private static AnimalConfiguration animalConfigurationWithNoCopulation() {
        return AnimalConfiguration.builder()
                .startEnergy(15)
                .minimumMutationCount(0)
                .maximumMutationCount(0)
                .wellFedEnergy(Integer.MAX_VALUE)
                .lossCopulateEnergy(10)
                .mutationVariant(MutationVariant.FULL_RANDOM)
                .genomeLength(7)
                .build();
    }

    private static WorldMapConfiguration defaultWorldMapConfiguration(WorldMapVariant mapVariant, int startPlantCount, int plantGrowth, int fireDuration, int fireFrequency) {
        return WorldMapConfiguration.builder()
                .height(36)
                .width(54)
                .energyGain(5)
                .plantGrowth(plantGrowth)
                .startPlantCount(startPlantCount)
                .fireDuration(fireDuration)
                .fireFrequency(fireFrequency)
                .mapVariant(mapVariant)
                .plantVariant(PlantVariant.FORESTED_EQUATORS)
                .build();
    }

    private static SimulationConfiguration defaultSimulationConfiguration(int daysCount, int startAnimalCount) {
        return SimulationConfiguration.builder()
                .daysCount(daysCount)
                .startAnimalCount(startAnimalCount)
                .build();
    }

    public static Configuration createFireConfiguration() {
        return Configuration.builder()
                .animalConfiguration(defaultAnimalConfiguration())
                .worldMapConfiguration(defaultWorldMapConfiguration(
                        WorldMapVariant.FIRE, 45, 7, 10, 3))
                .simulationConfiguration(defaultSimulationConfiguration(2000, 25))
                .build();
    }


    public static Configuration createEarthSimulationNoPlantsAndNoCopulation() {
        return Configuration.builder()
                .animalConfiguration(animalConfigurationWithNoCopulation())
                .worldMapConfiguration(defaultWorldMapConfiguration(
                        WorldMapVariant.EARTH, 0, 0, 0, 0))
                .simulationConfiguration(defaultSimulationConfiguration(2000, 45))
                .build();
    }

    public static Configuration createFireSimulationNoPlantsAndNoCopulation() {
        return Configuration.builder()
                .animalConfiguration(animalConfigurationWithNoCopulation())
                .worldMapConfiguration(defaultWorldMapConfiguration(
                        WorldMapVariant.FIRE, 0, 0, 10, 3))
                .simulationConfiguration(defaultSimulationConfiguration(2000, 45))
                .build();
    }

    public static Configuration createEarthConfigurationNoAnimals() {
        return Configuration.builder()
                .animalConfiguration(defaultAnimalConfiguration())
                .worldMapConfiguration(defaultWorldMapConfiguration(
                        WorldMapVariant.EARTH, 45, 7, 0, 0))
                .simulationConfiguration(defaultSimulationConfiguration(2000, 0))
                .build();
    }

    public static Configuration createFireConfigurationNoAnimalsNoFire() {
        return Configuration.builder()
                .animalConfiguration(defaultAnimalConfiguration())
                .worldMapConfiguration(defaultWorldMapConfiguration(
                        WorldMapVariant.FIRE, 45, 7, 0, 0))
                .simulationConfiguration(defaultSimulationConfiguration(2000, 0))
                .build();
    }

    public static Configuration createFireConfigurationNoPlantsNoFire() {
        return Configuration.builder()
                .animalConfiguration(animalConfigurationWithNoCopulation())
                .worldMapConfiguration(defaultWorldMapConfiguration(
                        WorldMapVariant.FIRE, 0, 0, 0, 0))
                .simulationConfiguration(defaultSimulationConfiguration(2000, 10))
                .build();
    }


}
