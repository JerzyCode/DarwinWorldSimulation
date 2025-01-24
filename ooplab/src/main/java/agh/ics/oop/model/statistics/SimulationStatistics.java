package agh.ics.oop.model.statistics;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Gen;
import agh.ics.oop.model.elements.Genome;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class SimulationStatistics {
    private final StatisticsDataProvider statisticsDataProvider;

    public SimulationStatistics(StatisticsDataProvider animalDataProvider) {
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

    public int getCurrentDay() {
        return statisticsDataProvider.getCurrentDay();
    }

}

