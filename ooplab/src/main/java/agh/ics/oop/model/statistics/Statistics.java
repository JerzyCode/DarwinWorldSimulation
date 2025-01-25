package agh.ics.oop.model.statistics;

import agh.ics.oop.model.elements.Gen;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Statistics {
    private int currentDay;
    private int animalCount;
    private int plantCount;
    private int freeFieldsCount;
    private double averageEnergy;
    @Builder.Default
    private List<Gen> mostPopularGenotype = new ArrayList<>();
    private double averageLifespan;
    private double averageChildren;

    private final List<GraphData> history = new ArrayList<>();

    public void updateStatistics(int currentDay, SimulationStatisticsCalculator calculator) {
        this.currentDay = currentDay;
        this.animalCount = calculator.getAnimalCount();
        this.plantCount = calculator.getPlantCount();
        this.freeFieldsCount = calculator.getEmptyFieldsCount();
        this.averageEnergy = calculator.getAverageAnimalEnergy().orElse(0.0);
        this.mostPopularGenotype = calculator.getMostPopularGenotype().orElse(new ArrayList<>());
        this.averageLifespan = calculator.getAverageDeadAnimalTimeLife().orElse(0.0);
        this.averageChildren = calculator.getAverageAnimalCountOfChildren().orElse(0.0);
        history.add(new GraphData(currentDay, animalCount, plantCount));
    }

}
