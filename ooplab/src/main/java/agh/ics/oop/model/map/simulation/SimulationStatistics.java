package agh.ics.oop.model.map.simulation;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Gen;
import agh.ics.oop.model.elements.Genome;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class SimulationStatistics {
    private final SimulationWorldMap worldMap;

    public SimulationStatistics(SimulationWorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public int getAnimalCount() {
        return worldMap.getAnimals().size();
    }

    public OptionalDouble getAverageAnimalEnergy() {
        return worldMap.getAnimals().stream()
                .mapToDouble(Animal::getEnergy)
                .average();
    }

    public OptionalDouble getAverageDeadAnimalTimeLife() {
        return worldMap.getDeadAnimals().stream()
                .mapToDouble(animal -> animal.getEndDay() - animal.getStartDay())
                .average();
    }

    public OptionalDouble getAverageAnimalCountOfChildren() {
        return worldMap.getAnimals().stream()
                .mapToInt(Animal::getCountOfChildren)
                .average();
    }

    public Optional<List<Gen>> getMostPopularGenotype() {
        Map<List<Gen>, Long> genotypeCount = worldMap.getAnimals().stream()
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

