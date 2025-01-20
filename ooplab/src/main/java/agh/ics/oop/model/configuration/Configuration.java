package agh.ics.oop.model.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Configuration {
    private final WorldMapConfiguration worldMapConfiguration;
    private final AnimalConfiguration animalConfiguration;
    private final SimulationConfiguration simulationConfiguration;

    @JsonCreator
    public Configuration(
            @JsonProperty("worldMapConfiguration") WorldMapConfiguration worldMapConfiguration,
            @JsonProperty("animalConfiguration") AnimalConfiguration animalConfiguration,
            @JsonProperty("simulationConfiguration") SimulationConfiguration simulationConfiguration) {
        this.worldMapConfiguration = worldMapConfiguration;
        this.animalConfiguration = animalConfiguration;
        this.simulationConfiguration = simulationConfiguration;
    }

    public static Configuration getDefaultConfig() {
        var worldMapConfiguration = WorldMapConfiguration.builder()
                .height(36)
                .width(54)
                .energyGain(5)
                .plantGrowth(7)
                .startPlantCount(45)
                .fireDuration(0)
                .fireFrequency(0)
                .mapVariant(WorldMapVariant.EARTH)
                .plantVariant(PlantVariant.FORESTED_EQUATORS)
                .build();


        var animalConfiguration = AnimalConfiguration.builder()
                .startEnergy(15)
                .minimumMutationCount(0)
                .maximumMutationCount(0)
                .wellFedEnergy(10)
                .lossCopulateEnergy(10)
                .mutationVariant(MutationVariant.FULL_RANDOM)
                .genomeLength(7)
                .build();

        var simulationConfiguration = SimulationConfiguration.builder()
                .daysCount(2000)
                .startAnimalCount(45)
                .build();

        return Configuration.builder()
                .worldMapConfiguration(worldMapConfiguration)
                .animalConfiguration(animalConfiguration)
                .simulationConfiguration(simulationConfiguration)
                .build();
    }


}
