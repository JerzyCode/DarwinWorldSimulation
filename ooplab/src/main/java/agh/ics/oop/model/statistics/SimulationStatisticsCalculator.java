package agh.ics.oop.model.statistics;

import agh.ics.oop.model.elements.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class SimulationStatisticsCalculator {
    private final StatisticsDataProvider statisticsDataProvider;

    public SimulationStatisticsCalculator(StatisticsDataProvider animalDataProvider) {
        this.statisticsDataProvider = animalDataProvider;
    }

    public int getAnimalCount() {
        return statisticsDataProvider.getAliveAnimals().size();
    }

    public OptionalDouble getAverageAnimalEnergy() {
        return statisticsDataProvider.getAliveAnimals().stream()
                .mapToDouble(Animal::getEnergy)
                .average();
    }

    public OptionalDouble getAverageDeadAnimalTimeLife() {
        return statisticsDataProvider.getDeadAnimals().stream()
                .mapToDouble(animal -> animal.getEndDay() - animal.getStartDay() + 1)
                .average();
    }

    public OptionalDouble getAverageAnimalCountOfChildren() {
        return statisticsDataProvider.getAliveAnimals().stream()
                .mapToInt(Animal::getCountOfChildren)
                .average();
    }

    public Optional<List<Gen>> getMostPopularGenotype() {
        return statisticsDataProvider.getAliveAnimals().stream()
                .map(Animal::getGenome)
                .map(Genome::getGens)
                .collect(Collectors.groupingBy(genome -> genome, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    public int getPlantCount() {
        return (int) statisticsDataProvider.getMapElements().stream()
                .filter(element -> element instanceof Plant)
                .count();
    }


    public int getEmptyFieldsCount() {
        var boundary = statisticsDataProvider.getCurrentBoundary();
        int countOfOccupiedFields = (int) statisticsDataProvider.getMapElements().stream()
                .map(WorldElement::getPosition)
                .distinct()
                .count();

        return (boundary.rightTopCorner().getX() + 1) * (boundary.rightTopCorner().getY() + 1) - countOfOccupiedFields;
    }

}

