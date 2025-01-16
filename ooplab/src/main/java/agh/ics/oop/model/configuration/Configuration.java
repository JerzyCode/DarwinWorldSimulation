package agh.ics.oop.model.configuration;

public class Configuration {
    private final WorldMapConfiguration worldMapConfiguration;
    private final AnimalConfiguration animalConfiguration;
    private final SimulationConfiguration simulationConfiguration;

    public Configuration() {
        this.worldMapConfiguration = WorldMapConfigurationBuilder.create()
                .height(26)
                .width(54)
                .fireDuration(0)
                .fireFrequency(0)
                .mapVariant(WorldMapVariant.EARTH)
                .plantVariant(PlantVariant.FORESTED_EQUATORS)
                .build();


        this.animalConfiguration = AnimalConfigurationBuilder.create()
                .startEnergy(15)
                .minimumMutationCount(0)
                .maximumMutationCount(0)
                .mutationVariant(MutationVariant.FULL_RANDOM)
                .genomeLength(7)
                .build();

        this.simulationConfiguration = SimulationConfigurationBuilder.create()
                .daysCount(2000)
                .energyGain(5)
                .plantGrowth(7)
                .wellFedEnergy(10)
                .lossCopulateEnergy(10)
                .startAnimalCount(45)
                .startPlantCount(45)
                .build();
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
