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
import agh.ics.oop.model.util.AnimalComparator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Earth extends AbstractPlantMap implements MoveAdjuster, SimulationWorldMap {
    private final Boundary boundary;
    private final Gardener gardener;
    private final AnimalBreeder breeder;
    private final AnimalComparator animalComparator = new AnimalComparator();


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
        animals.keySet().stream()
                .filter(this::isPlantAtPosition)
                .map(pos -> getAnimalsAtPosition(pos)
                        .stream()
                        .max(animalComparator))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(this::handleAnimalStepOnPlant);
    }

    private void handleAnimalStepOnPlant(Animal animal) {
        var position = animal.getPosition();
        animal.eat(plants.get(position));
        removePlant(position);
    }

    private void copulateAnimals() {
        animals.keySet().stream()
                .filter(animalsAtPosition -> animals.get(animalsAtPosition).size() >= 2)
                .map(pos -> getAnimalsAtPosition(pos)
                        .stream()
                        .filter(Animal::canMakeChild)
                        .sorted(animalComparator.reversed())
                        .limit(2)
                        .collect(Collectors.toList()))
                .filter(animals -> animals.size() == 2)
                .forEach(animalsToBreed -> {
                    var parent1 = animalsToBreed.getFirst();
                    var parent2 = animalsToBreed.getLast();
                    breedChild(parent1, parent2);
                });
    }

    private void breedChild(Animal parent1, Animal parent2) {
        var child = breeder.breed(parent1, parent2);
        try {
            place(child);
        } catch (IncorrectPositionException e) {
            System.out.println("Child can not be placed: " + e.getMessage());
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

