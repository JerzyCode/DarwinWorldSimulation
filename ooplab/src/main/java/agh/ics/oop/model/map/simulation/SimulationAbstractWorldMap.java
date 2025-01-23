package agh.ics.oop.model.map.simulation;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.event.EventCreator;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.exceptions.PositionOutOfMapBoundaryException;
import agh.ics.oop.model.map.AbstractWorldMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

abstract public class SimulationAbstractWorldMap extends AbstractWorldMap implements SimulationWorldMap {
    protected final Map<Vector2d, Set<Animal>> animals;

    public SimulationAbstractWorldMap() {
        this.animals = new ConcurrentHashMap<>();
    }

    @Override
    public abstract void handleDayEnds(int currentDay);

    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        var position = animal.getPosition();
        if (!isPositionWithinMapBoundary(animal.getPosition())) {
            throw new PositionOutOfMapBoundaryException(position);
        }

        placeAnimalAtNewPosition(animal);
        notifyListeners(EventCreator.createAnimalPlacedEvent(animal.getPosition()));
    }

    //TODO do wywalenia chyba
    @Override
    public void removeAnimal(Animal animal) {
        var animalsAtPosition = animals.get(animal.getPosition());
        animalsAtPosition.remove(animal);
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
    public Set<Animal> getAnimals() {
        return animals.values()
                .stream()
                .flatMap(Set::stream)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public void clearDeadAnimals() {
        animals.values().forEach(animalSet -> animalSet.removeIf(Animal::isDead));
    }

    @Override
    public final void sendDayHasEndedNotification(int currentDay) {
        notifyListeners(EventCreator.createDayEndsEvent(currentDay));
    }

    protected void placeAnimalAtNewPosition(Animal animal) {
        var animalsAtPosition = animals.computeIfAbsent(animal.getPosition(), k -> new HashSet<>());
        animalsAtPosition.add(animal);
    }


}
