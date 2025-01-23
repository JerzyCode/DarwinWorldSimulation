package agh.ics.oop.model.map.plant;

import agh.ics.oop.model.AnimalBreeder;
import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.PlantVariant;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.simulation.SimulationWorldMap;
import agh.ics.oop.model.move.Move;
import agh.ics.oop.model.move.MoveAdjuster;
import agh.ics.oop.model.move.MoveDirection;

import java.util.*;
import java.util.stream.Collectors;

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
        var elements = new ArrayList<WorldElement>(plants.values());
        for (var entry : animals.entrySet()) {
            elements.addAll(entry.getValue());
        }

        return Collections.unmodifiableCollection(elements);
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

        return getSize() - (int) countOfOccupiedFields;
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
        gardener.createPlants(plants.size(), getSize(), boundary, startPlantCount, plants.keySet())
                .forEach(plant -> {
                    try {
                        placePlant(plant);
                    } catch (Exception e) {
                        //TODO zastanowic sie czy place plant powinno wyjatkiem walic
                    }
                });
    }

    private void growPlantsDaily() {

        gardener.createPlantsDaily(plants.size(), getSize(), boundary, plants.keySet())
                .forEach(plant -> {
                    try {
                        placePlant(plant);
                    } catch (Exception e) {
                        //TODO zastanowic sie czy place plant powinno wyjatkiem walic
                    }
                });
    }

}

