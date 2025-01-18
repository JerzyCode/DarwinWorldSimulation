package agh.ics.oop.model.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Configuration {
    private final WorldMapConfiguration worldMapConfiguration;
    private final AnimalConfiguration animalConfiguration;
    private final SimulationConfiguration simulationConfiguration;

    public Configuration() {
        this.worldMapConfiguration = WorldMapConfiguration.builder()
                .height(26)
                .width(54)
                .energyGain(5)
                .plantGrowth(7)
                .startPlantCount(45)
                .fireDuration(0)
                .fireFrequency(0)
                .mapVariant(WorldMapVariant.EARTH)
                .plantVariant(PlantVariant.FORESTED_EQUATORS)
                .build();


        this.animalConfiguration = AnimalConfiguration.builder()
                .startEnergy(15)
                .minimumMutationCount(0)
                .maximumMutationCount(0)
                .wellFedEnergy(10)
                .lossCopulateEnergy(10)
                .mutationVariant(MutationVariant.FULL_RANDOM)
                .genomeLength(7)
                .build();

        this.simulationConfiguration = SimulationConfiguration.builder()
                .daysCount(2000)
                .startAnimalCount(45)
                .build();
    }

}
