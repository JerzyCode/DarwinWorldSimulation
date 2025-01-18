package agh.ics.oop;

import agh.ics.oop.factory.AnimalFactory;
import agh.ics.oop.factory.WorldMapFactory;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Gen;
import agh.ics.oop.model.elements.Genome;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.AbstractWorldMap;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.map.simulation.SimulationWorldMap;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class SimulationContext {
    private final Configuration configuration;
    private final AnimalFactory animalFactory;
    private final SimulationWorldMap worldMap;
    private int currentDay;

    public SimulationContext(Configuration configuration) {
        this.configuration = configuration;
        this.animalFactory = new AnimalFactory(configuration.getAnimalConfiguration());
        WorldMapFactory worldMapFactory = new WorldMapFactory(configuration.getWorldMapConfiguration(), this::breedAnimals);
        this.worldMap = worldMapFactory.createWorldMap();
        currentDay = 1;

        initAnimals();
    }

    public void handleDayEnds() {
        System.out.println("Current day: " + currentDay + ", animalsCount=" + getAnimalCount());
        worldMap.handleDayEnds(currentDay);
        currentDay++;
    }


    private Animal breedAnimals(Animal parent1, Animal parent2) {
        return animalFactory.birthAnimal(parent1, parent2, currentDay);
    }


    private void initAnimals() {
        var boundary = worldMap.getCurrentBounds();
        var randomizer = new RandomPositionGenerator(
                configuration.getSimulationConfiguration().getStartAnimalCount(),
                boundary.rightTopCorner().getX(),
                boundary.rightTopCorner().getY());

        for (Vector2d position : randomizer) {
            var animal = animalFactory.createAnimal(position, currentDay);
            try {
                worldMap.place(animal);
            } catch (IncorrectPositionException e) {
                System.out.println("createAnimals(), animal not placed: message=" + e.getMessage());
            }
        }
    }


    public void setMapChangeListener(MapChangeListener listener) {
        ((AbstractWorldMap) worldMap).addListener(listener);
    }

    public WorldMap getWorldMap() {
        return worldMap;
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

    // TODO: nie wiem czy to chodzi o cały Genom czy to mogą być podciągi z Genomu też, ale wtedy najpopularniejszy genotyp to byłby zawsze jakiś jeden gen
    public List<Gen> getMostPopularGenotype() {
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
                .findFirst()
                .orElse(null);
    }
}
