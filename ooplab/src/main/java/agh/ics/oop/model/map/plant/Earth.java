package agh.ics.oop.model.map.plant;

import agh.ics.oop.model.AnimalBreeder;
import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.PlantVariant;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.event.EventCreator;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.simulation.SimulationWorldMap;
import agh.ics.oop.model.move.Move;
import agh.ics.oop.model.move.MoveAdjuster;
import agh.ics.oop.model.move.MoveDirection;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Earth extends AbstractPlantMap implements MoveAdjuster, SimulationWorldMap {
    private final Boundary boundary;
    private final Gardener gardener;
    private final AnimalBreeder breeder;


    public Earth(int width, int height, int plantGrowth, int startPlantCount, int energyGain, PlantVariant plantVariant, AnimalBreeder breeder) {
        super();
        boundary = new Boundary(new Vector2d(0, 0), new Vector2d(width - 1, height - 1));
        gardener = new Gardener(plantVariant, plantGrowth, energyGain);
        this.breeder = breeder;
        initPlants(startPlantCount);
    }

    @Override
    public void handleDayEnds(int currentDay) {
        consumePlants();
        copulateAnimals();
        growPlantsDaily();
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
        return Stream
                .concat(super.getElements().stream(), plants.values().stream())
                .collect(Collectors.toUnmodifiableSet());
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
            notifyListeners(EventCreator.createAnimalMovedEvent(animal.getPosition()));
        }
    }

    @Override
    public Optional<WorldElement> objectAt(Vector2d position) {
        return super.objectAt(position)
                .or(() -> Optional.ofNullable(plants.get(position)));
    }


    Set<Animal> getAnimalsAtPosition(Vector2d position) {
        if (animals.containsKey(position)) {
            return animals.get(position);
        }
        return new HashSet<>();
    }


    public int getPlantCount() {
        return plants.size();
    }

    public int getCountOfEmptyFields() {
        long countOfOccupiedFields = getElements().stream()
                .map(WorldElement::getPosition)
                .distinct()
                .count();

        return calculateSize() - (int) countOfOccupiedFields;
    }

    private void consumePlants() {
        getAnimals().forEach(this::handleAnimalStepOnPlant);
    }

    private void handleAnimalStepOnPlant(Animal animal) {
        var position = animal.getPosition();
        if (isPlantAtPosition(position)) {
            animal.eat(plants.get(position));
            removePlant(position);
        }
    }

    private void copulateAnimals() {
        var animalsWhichCanBreed = getAnimals()
                .stream()
                .filter(Animal::canMakeChild)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        while (animalsWhichCanBreed.size() >= 2) {
            var parent1 = animalsWhichCanBreed.removeFirst();
            var parent2 = animalsWhichCanBreed.removeLast();
            var child = breeder.breed(parent1, parent2);
            try {
                place(child);
            } catch (IncorrectPositionException e) {
                System.out.println("Child can not be placed: " + e.getMessage());
            }
        }
    }

    private void initPlants(int startPlantCount) {
        gardener.createPlants(plants.size(), calculateSize(), boundary, startPlantCount, plants.keySet())
                .forEach(plant -> {
                    try {
                        placePlant(plant);
                    } catch (IncorrectPositionException e) {
                        System.out.println("Couldn't place plant: " + e.getMessage());
                    }
                });
    }

    private void growPlantsDaily() {

        gardener.createPlantsDaily(plants.size(), calculateSize(), boundary, plants.keySet())
                .forEach(plant -> {
                    try {
                        placePlant(plant);
                    } catch (IncorrectPositionException e) {
                        System.out.println("Couldn't place plant: " + e.getMessage());
                    }
                });
    }

    private int calculateSize() {
        return (boundary.rightTopCorner().getX() + 1) * (boundary.rightTopCorner().getY() + 1);
    }

}

