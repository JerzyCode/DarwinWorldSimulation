package agh.ics.oop;

import agh.ics.oop.factory.AnimalFactory;
import agh.ics.oop.factory.WorldMapFactory;
import agh.ics.oop.listener.MapChangeListener;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.AbstractWorldMap;
import agh.ics.oop.model.map.simulation.SimulationWorldMap;
import agh.ics.oop.model.move.MoveDirection;
import agh.ics.oop.model.util.RandomPositionGenerator;
import lombok.Getter;

import java.util.*;

public class SimulationContext {
    private final Configuration configuration;
    private final AnimalFactory animalFactory;
    @Getter
    private final SimulationWorldMap worldMap;
    private int currentDay;
    private final Set<Animal> deadAnimals;


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


    public void addMapChangedListener(MapChangeListener listener) {
        ((AbstractWorldMap) worldMap).addListener(listener);
    }

    public SimulationWorldMap getWorldMap() {
        return worldMap;
    }

}
