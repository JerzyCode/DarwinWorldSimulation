package agh.ics.oop;

import agh.ics.oop.factory.AnimalFactory;
import agh.ics.oop.factory.WorldMapFactory;
import agh.ics.oop.listener.MapChangeListener;
import agh.ics.oop.listener.SimulationFinishedListener;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.AbstractWorldMap;
import agh.ics.oop.model.map.simulation.SimulationWorldMap;
import agh.ics.oop.model.move.MoveDirection;
import agh.ics.oop.model.statistics.SimulationStatisticsCalculator;
import agh.ics.oop.model.statistics.Statistics;
import agh.ics.oop.model.statistics.StatisticsDataProvider;
import agh.ics.oop.model.util.RandomPositionGenerator;
import lombok.Getter;

import java.util.*;

public class SimulationContext implements StatisticsDataProvider {
    private final Configuration configuration;
    private final AnimalFactory animalFactory;
    @Getter
    private final SimulationWorldMap worldMap;
    private int currentDay;
    private final Set<Animal> deadAnimals;
    private final List<SimulationFinishedListener> listeners = new ArrayList<>();
    private final Statistics simulationStatistics = new Statistics();
    private final SimulationStatisticsCalculator statisticsCalculator = new SimulationStatisticsCalculator(this);


    public SimulationContext(Configuration configuration) {
        this.configuration = configuration;
        this.animalFactory = new AnimalFactory(configuration.getAnimalConfiguration());
        WorldMapFactory worldMapFactory = new WorldMapFactory(configuration.getWorldMapConfiguration(), this::breedAnimals);
        this.worldMap = worldMapFactory.createWorldMap();
        deadAnimals = new HashSet<>();
        currentDay = 1;

        initAnimals();
    }

    public void handleDayEnds() {
        worldMap.clearDeadAnimals();
        worldMap.getAnimals().forEach(this::handleAnimalDayEnds);
        worldMap.handleDayEnds(currentDay);
        worldMap.sendDayHasEndedNotification(currentDay);
        updateStatistics();
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
                boundary.rightTopCorner().getY(),
                Set.of());

        for (Vector2d position : randomizer) {
            var animal = animalFactory.createAnimal(position, currentDay);
            try {
                worldMap.place(animal);
            } catch (IncorrectPositionException e) {
                System.out.println("createAnimals(), animal not placed: message=" + e.getMessage());
            }
        }
    }

    private void handleAnimalDayEnds(Animal animal) {
        animal.decreaseEnergy(1);
        worldMap.move(animal, MoveDirection.FORWARD);

        if (animal.isDead()) {
            deadAnimals.add(animal);
            animal.setEndDay(currentDay);
        }
    }

    void notifySimulationFinished() {
        listeners.forEach(SimulationFinishedListener::onSimulationFinished);
    }


    public void addMapChangedListener(MapChangeListener listener) {
        ((AbstractWorldMap) worldMap).addListener(listener);
    }

    public void addSimulationFinishedListener(SimulationFinishedListener simulationFinishedListener) {
        listeners.add(simulationFinishedListener);
    }


    @Override
    public Set<Animal> getDeadAnimals() {
        return Collections.unmodifiableSet(deadAnimals);
    }

    @Override
    public Set<Animal> getAliveAnimals() {
        return worldMap.getAnimals();
    }

    @Override
    public Set<WorldElement> getMapElements() {
        return (Set<WorldElement>) worldMap.getElements();
    }

    public Statistics getStatistics() {
        return simulationStatistics;
    }

    public String getWorldMapUuid() {
        return worldMap.getId().toString();
    }

    private void updateStatistics() {
        simulationStatistics.setCurrentDay(currentDay);
        simulationStatistics.setAnimalCount(statisticsCalculator.getAnimalCount());
        simulationStatistics.setPlantCount((statisticsCalculator.getPlantCount()));
        simulationStatistics.setFreeFieldsCount(statisticsCalculator.getEmptyFieldsCount(worldMap.getCurrentBounds()));
        simulationStatistics.setAverageEnergy(statisticsCalculator.getAverageAnimalEnergy().orElse(0));
        simulationStatistics.setMostPopularGenotype(statisticsCalculator.getMostPopularGenotype().orElse(new ArrayList<>()));
        simulationStatistics.setAverageLifespan(statisticsCalculator.getAverageDeadAnimalTimeLife().orElse(0));
        simulationStatistics.setAverageChildren(statisticsCalculator.getAverageAnimalCountOfChildren().orElse(0));
    }
}
