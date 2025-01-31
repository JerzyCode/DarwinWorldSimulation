package agh.ics.oop;

import agh.ics.oop.factory.AnimalFactory;
import agh.ics.oop.factory.WorldMapFactory;
import agh.ics.oop.listener.MapChangeListener;
import agh.ics.oop.listener.SimulationFinishedListener;
import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.AbstractWorldMap;
import agh.ics.oop.model.map.simulation.SimulationWorldMap;
import agh.ics.oop.model.move.MoveDirection;
import agh.ics.oop.model.statistics.GraphData;
import agh.ics.oop.model.statistics.SimulationStatisticsCalculator;
import agh.ics.oop.model.statistics.Statistics;
import agh.ics.oop.model.statistics.StatisticsDataProvider;
import lombok.Getter;

import java.util.*;

public class SimulationContext implements StatisticsDataProvider {
    private final Configuration configuration;
    private final AnimalFactory animalFactory;
    @Getter
    private final SimulationWorldMap worldMap;
    private int currentDay;
    private final Set<Animal> deadAnimals;
    private final Random random = new Random(); // static?
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
        handleDeadAnimals();
        worldMap.clearDeadAnimals();
        worldMap.getAnimals().forEach(this::handleAnimalDayEnds);
        worldMap.handleDayEnds(currentDay);
        worldMap.sendDayHasEndedNotification(currentDay);
        simulationStatistics.updateStatistics(currentDay, statisticsCalculator);
        currentDay++;
    }


    public void addMapChangedListener(MapChangeListener listener) {
        ((AbstractWorldMap) worldMap).addListener(listener); // rzutowanie? czemu takie pośrednictwo? nie lepiej dodawać wprost do mapy?
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
        return (Set<WorldElement>) worldMap.getElements(); // rzutowanie?
    }

    @Override
    public Boundary getCurrentBoundary() {
        return worldMap.getCurrentBounds();
    }

    public List<GraphData> getGraphData() {
        return simulationStatistics.getHistory();
    }

    public Statistics getStatistics() {
        return simulationStatistics;
    }

    void notifySimulationFinished() {
        listeners.forEach(SimulationFinishedListener::onSimulationFinished);
    }

    private Animal breedAnimals(Animal parent1, Animal parent2) {
        return animalFactory.birthAnimal(parent1, parent2, currentDay);
    }

    private void initAnimals() {
        var boundary = worldMap.getCurrentBounds();

        for (int i = 0; i < configuration.getSimulationConfiguration().getStartAnimalCount(); i++) {
            var animal = animalFactory.createAnimal(getRandomAnimalPosition(boundary), currentDay);
            try {
                worldMap.place(animal);
            } catch (IncorrectPositionException e) { // czy to się może zdarzyć?
                System.out.println("createAnimals(), animal not placed: message=" + e.getMessage());
            }
        }
    }

    private Vector2d getRandomAnimalPosition(Boundary boundary) {
        var x = random.nextInt(boundary.rightTopCorner().getX() + 1);
        var y = random.nextInt(boundary.rightTopCorner().getY() + 1);

        return new Vector2d(x, y);
    }

    private void handleAnimalDayEnds(Animal animal) {
        worldMap.move(animal, MoveDirection.FORWARD);
        animal.decreaseEnergy(1);
    }

    private void handleDeadAnimals() {
        worldMap.getAnimals().stream()
                .filter(Animal::isDead)
                .forEach(animal -> {
                    deadAnimals.add(animal);
                    animal.setEndDay(currentDay);
                });
    }

}
