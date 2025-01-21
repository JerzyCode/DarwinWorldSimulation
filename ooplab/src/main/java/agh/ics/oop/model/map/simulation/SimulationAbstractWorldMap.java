package agh.ics.oop.model.map.simulation;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.exceptions.PositionOutOfMapBoundaryException;
import agh.ics.oop.model.map.AbstractWorldMap;
import agh.ics.oop.model.move.MoveDirection;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

abstract public class SimulationAbstractWorldMap extends AbstractWorldMap implements SimulationWorldMap {
    protected final Map<Vector2d, Set<Animal>> animals;
    private final Set<Animal> deadAnimals;


    public SimulationAbstractWorldMap() {
        this.animals = new ConcurrentHashMap<>();
        this.deadAnimals = new HashSet<>();
    }

    @Override
    public void handleDayEnds(int currentDay) {
        clearDeadAnimals();
        var allAnimals = animals.values()
                .stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        allAnimals.forEach(animal -> handleAnimalDayEnds(animal, currentDay));
    }

    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        var position = animal.getPosition();
        if (!isPositionWithinMapBoundary(animal.getPosition())) {
            throw new PositionOutOfMapBoundaryException(position);
        }

        placeAnimalAtNewPosition(animal);
        notifyListeners("Animal was placed at position: " + position);
    }

    @Override
    public void removeAnimal(Animal animal) {
        var animalsAtPosition = animals.get(animal.getPosition());
        animalsAtPosition.remove(animal);
        notifyListeners("Animal was removed from position: " + animal.getPosition());
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return !animals.get(position).isEmpty();
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return animals.get(position).iterator().next();
    }

    @Override
    public Set<Animal> getAnimals() { //TODO nie wiem czy git ten getter
        return animals.values()
                .stream()
                .flatMap(Set::stream)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<Animal> getDeadAnimals() {
        return Collections.unmodifiableSet(deadAnimals);
    }

    protected void placeAnimalAtNewPosition(Animal animal) {
        var animalsAtPosition = animals.computeIfAbsent(animal.getPosition(), k -> new HashSet<>());
        animalsAtPosition.add(animal);
    }

    private void clearDeadAnimals() {
        animals.values().forEach(animalSet -> animalSet.removeIf(Animal::isDead));
    }

    private void handleAnimalDayEnds(Animal animal, int currentDay) {
        animal.decreaseEnergy(1);
        move(animal, MoveDirection.FORWARD);

        if (animal.isDead()) {
            deadAnimals.add(animal);
            animal.setEndDay(currentDay);
        }
    }
}
