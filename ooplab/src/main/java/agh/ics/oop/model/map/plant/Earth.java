package agh.ics.oop.model.map.plant;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.SimulationWorldMap;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.PlantVariant;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.exceptions.PositionOutOfMapBoundaryException;
import agh.ics.oop.model.map.AbstractPlantMap;
import agh.ics.oop.model.move.Move;
import agh.ics.oop.model.move.MoveAdjuster;
import agh.ics.oop.model.move.MoveDirection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Earth extends AbstractPlantMap implements MoveAdjuster, SimulationWorldMap {
    private final Boundary boundary;
    private final Map<Vector2d, Set<Animal>> animals;
    private final Gardener gardener;

    public Earth(int width, int height, int plantGrowth, int startPlantCount, PlantVariant plantVariant) {
        boundary = new Boundary(new Vector2d(0, 0), new Vector2d(width - 1, height - 1));
        animals = new ConcurrentHashMap<>();
        gardener = new Gardener(plantVariant, plantGrowth);
        growPlantsStart(startPlantCount);
    }

    @Override
    public void handleDayEnds(int currentDay) {
        growPlantsDaily();
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
    public Boundary getCurrentBounds() {
        return boundary;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        var topRightMaxMove = boundary.rightTopCorner().add(new Vector2d(1, 1));
        var bottomLeftMaxMove = boundary.leftBottomCorner().add(new Vector2d(-1, -1));
        return position.follows(bottomLeftMaxMove) && position.precedes(topRightMaxMove);
    }

    @Override
    public Collection<WorldElement> getElements() {
        var elements = new ArrayList<WorldElement>(plants.values());
        for (var entry : animals.entrySet()) {
            elements.addAll(entry.getValue());
        }

        return Collections.unmodifiableCollection(elements);
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
    public Move adjustMove(Move move) {
        var position = move.getToPosition();
        var orientation = move.getOrientation();

        if (position.isUnder(boundary.leftBottomCorner())) {
            position = new Vector2d(position.getX(), boundary.leftBottomCorner().getY());
            orientation = orientation.rotateRightAngleClockwise().rotateRightAngleClockwise();
        }
        if (position.isAbove(boundary.rightTopCorner())) {
            position = new Vector2d(position.getX(), boundary.rightTopCorner().getY());
            orientation = orientation.rotateRightAngleClockwise().rotateRightAngleClockwise();
        }
        if (position.isOnTheLeft(boundary.leftBottomCorner())) {
            position = new Vector2d(boundary.rightTopCorner().getX(), position.getY());
        }
        if (position.isOnTheRight(boundary.rightTopCorner())) {
            position = new Vector2d(boundary.leftBottomCorner().getX(), position.getY());
        }

        return new Move(position, orientation);
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        var animalsAtPosition = animals.get(animal.getPosition());
        if (animalsAtPosition.contains(animal)) {
            animalsAtPosition.remove(animal);
            animal.move(this, this);
            placeAnimalAtNewPosition(animal);
            notifyListeners("Animal moved to position: " + animal.getPosition());
        }

        handleAnimalStepOnPlant(animal);
    }


    public Set<Animal> getAnimalsAtPosition(Vector2d position) {
        if (animals.containsKey(position)) {
            return animals.get(position);
        }
        return new HashSet<>();
    }

    private void placeAnimalAtNewPosition(Animal animal) {
        var animalsAtPosition = animals.computeIfAbsent(animal.getPosition(), k -> new HashSet<>());
        animalsAtPosition.add(animal);
    }


    private void handleAnimalStepOnPlant(Animal animal) {
        var position = animal.getPosition();
        if (isPlantAtPosition(position)) {
            animal.increaseEnergy(10); //TODO po poprawkach mateusza git bedzie
            removePlant(position);
        }
    }

    private void growPlantsStart(int startPlantCount) {
        gardener.createPlants(plants.size(), getSize(), boundary, startPlantCount)
                .forEach(plant -> {
                    try {
                        placePlant(plant);
                    } catch (Exception e) {
                        //TODO zastanowic sie czy place plant powinno wyjatkiem walic
                    }
                });
    }

    private void growPlantsDaily() {
        gardener.createPlantsDaily(plants.size(), getSize(), boundary)
                .forEach(plant -> {
                    try {
                        placePlant(plant);
                    } catch (Exception e) {
                        //TODO zastanowic sie czy place plant powinno wyjatkiem walic
                    }
                });
    }


}

