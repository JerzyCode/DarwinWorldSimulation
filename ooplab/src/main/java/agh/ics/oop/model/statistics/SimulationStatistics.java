package agh.ics.oop.model.statistics;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Gen;
import agh.ics.oop.model.elements.Genome;

import java.util.*;
import java.util.stream.Collectors;

public class SimulationStatistics {
    private final AnimalDataProvider animalDataProvider;

    public SimulationStatistics(AnimalDataProvider animalDataProvider) {
        this.animalDataProvider = animalDataProvider;
    }

    public int getAnimalCount() {
        return animalDataProvider.getAliveAnimals().size();
    }

    public OptionalDouble getAverageAnimalEnergy() {
        return animalDataProvider.getAliveAnimals().stream()
                .mapToDouble(Animal::getEnergy)
                .average();
    }

    public OptionalDouble getAverageDeadAnimalTimeLife() {
        return animalDataProvider.getDeadAnimals().stream()
                .mapToDouble(animal -> animal.getEndDay() - animal.getStartDay() + 1)
                .average();
    }

    public OptionalDouble getAverageAnimalCountOfChildren() {
        return animalDataProvider.getAliveAnimals().stream()
                .mapToInt(Animal::getCountOfChildren)
                .average();
    }

    public Optional<List<Gen>> getMostPopularGenotype() {
        Map<List<Gen>, Long> genotypeCount = animalDataProvider.getAliveAnimals().stream()
                .map(Animal::getGenome)
                .map(Genome::getGens)
                .collect(Collectors.groupingBy(genome -> genome, Collectors.counting()));

        long maxCount = genotypeCount.values().stream()
                .max(Long::compareTo)
                .orElse(0L);

        return genotypeCount.entrySet().stream()
                .filter(entry -> entry.getValue() == maxCount)
                .map(Map.Entry::getKey)
                .findFirst();
    }

}

