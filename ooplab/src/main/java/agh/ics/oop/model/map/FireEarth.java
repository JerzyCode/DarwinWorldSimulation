package agh.ics.oop.model.map;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Fire;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.exceptions.PositionOccupiedByWorldElementException;
import agh.ics.oop.model.exceptions.PositionOutOfMapBoundaryException;

import java.util.*;
import java.util.stream.Collectors;

public class FireEarth extends Earth implements FireWorldMap {
    private final HashMap<Vector2d, Fire> fires;
    private final int fireFrequency;
    private final int fireDuration;

    public FireEarth(int width, int height, int fireFrequency, int fireDuration) {
        super(width, height);
        this.fireFrequency = fireFrequency;
        this.fireDuration = fireDuration;
        this.fires = new HashMap<>();
    }

    @Override
    public void placePlant(Plant plant) throws IncorrectPositionException {
        var position = plant.getPosition();
        if (isFireAtPosition(position)) {
            throw new PositionOccupiedByWorldElementException(position, Plant.class.getSimpleName(), Fire.class.getSimpleName());
        }

        super.placePlant(plant);
    }

    @Override
    public Collection<WorldElement> getElements() {
        var elements = new ArrayList<WorldElement>();
        elements.addAll(super.getElements());
        elements.addAll(fires.values());

        return Collections.unmodifiableCollection(elements);
    }

    @Override
    public boolean isFireAtPosition(Vector2d position) {
        return fires.containsKey(position);
    }


    @Override
    public void decreaseFireRemainingLifetime() {
        fires.values().forEach(Fire::decreaseRemainingLifetime);
    }

    @Override
    public void removeFire(Fire fire) {
        fires.remove(fire.getPosition());
        notifyListeners("Fire was removed from position: " + fire.getPosition());
    }

    @Override
    public void spreadFire() {
        Set<Fire> newFires = new HashSet<>();
        for (Fire fire : fires.values()) {
            var newFirePositions = getNewFirePositions(fire);
            newFirePositions.forEach(firePosition -> {
                var newFire = new Fire(firePosition, fireDuration);
                newFires.add(newFire);
            });
        }

        newFires.forEach(fire -> {
            try {
                removePlant(fire.getPosition());
                placeFire(fire);
            } catch (IncorrectPositionException e) {
                System.out.println("Couldn't spread fire to new position: " + e.getMessage());
            }
        });
    }

    @Override
    public void removeBurnedFires() {
        fires.entrySet().removeIf(entry -> entry.getValue().isBurned());
    }

    @Override
    public void createFire() {
        var plantsPositions = new ArrayList<>(plants.keySet());
        Collections.shuffle(plantsPositions);

        boolean isFirePlaced = false;
        for (Vector2d position : plantsPositions) {
            try {
                removePlant(position);
                var newFire = new Fire(position, fireDuration);
                placeFire(newFire);
                isFirePlaced = true;

            } catch (IncorrectPositionException e) {
                System.out.println("Couldn't create fire: " + e.getMessage());
            }
            if (isFirePlaced) {
                return;
            }
        }

    }

    @Override
    public void handleDayEnds() {
    }

    void placeFire(Fire fire) throws IncorrectPositionException {
        var position = fire.getPosition();

        if (!isPositionWithinMapBoundary(position)) {
            throw new PositionOutOfMapBoundaryException(position);
        }
        if (isPlantAtPosition(position)) {
            throw new PositionOccupiedByWorldElementException(position, Plant.class.getSimpleName());
        }
        if (isFireAtPosition(position)) {
            throw new PositionOccupiedByWorldElementException(position, Fire.class.getSimpleName());
        }
        fires.put(position, fire);
        notifyListeners("Fire was placed at position: " + position);
    }

    Fire getFireAtPosition(Vector2d position) {
        return fires.get(position);
    }


    private Set<Vector2d> getNewFirePositions(Fire fire) {
        var adjacentFields = getAdjacentFields(fire.getPosition());
        return adjacentFields
                .stream()
                .filter(position -> !isFireAtPosition(position))
                .filter(this::isPlantAtPosition)
                .collect(Collectors.toSet());
    }

    private List<Vector2d> getAdjacentFields(Vector2d position) {
        var x = position.getX();
        var y = position.getY();

        int rightX = (x + 1) <= getCurrentBounds().rightTopCorner().getX() ? (x + 1) : getCurrentBounds().leftBottomCorner().getX();
        int upperY = (y + 1) <= getCurrentBounds().rightTopCorner().getY() ? (y + 1) : getCurrentBounds().leftBottomCorner().getY();
        int leftX = (x - 1) >= 0 ? (x - 1) : getCurrentBounds().rightTopCorner().getX();
        int downY = (y - 1) >= 0 ? (y - 1) : getCurrentBounds().rightTopCorner().getY();

        Vector2d neighbour1 = new Vector2d(x, upperY);
        Vector2d neighbour2 = new Vector2d(x, downY);
        Vector2d neighbour3 = new Vector2d(leftX, y);
        Vector2d neighbour4 = new Vector2d(rightX, y);

        return List.of(neighbour1, neighbour2, neighbour3, neighbour4);
    }


}
