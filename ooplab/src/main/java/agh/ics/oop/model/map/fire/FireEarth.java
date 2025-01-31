package agh.ics.oop.model.map.fire;

import agh.ics.oop.model.AnimalBreeder;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.PlantVariant;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Fire;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.event.EventCreator;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.exceptions.PositionOccupiedByWorldElementException;
import agh.ics.oop.model.map.plant.Earth;
import agh.ics.oop.model.move.MoveDirection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FireEarth extends Earth implements FireWorldMap, FireValidator {
    private final Map<Vector2d, Fire> fires;
    private final int fireFrequency;
    private final FireSpreader fireSpreader;

    public FireEarth(int width, int height, int fireFrequency, int fireDuration, int plantGrowth, int startPlantCount, int energyGain, PlantVariant plantVariant, AnimalBreeder breeder) {
        super(width, height, plantGrowth, startPlantCount, energyGain, plantVariant, breeder);
        this.fireFrequency = fireFrequency;
        this.fires = new ConcurrentHashMap<>();
        this.fireSpreader = new FireSpreader(fireDuration);
    }

    @Override
    public void handleDayEnds(int currentDay) {
        super.handleDayEnds(currentDay);
        spreadFire();
        decreaseFireRemainingLifetime();
        removeBurnedFires();
        if (fireFrequency > 0 && currentDay % fireFrequency == 0) {
            createFire();
        }
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        super.move(animal, direction);
        if (isFireAtPosition(animal.getPosition())) {
            animal.kill();
        }
    }

    @Override
    public boolean placeFire(Fire fire) { // public?
        var position = fire.getPosition();
        if (canPlaceFire(position)) {
            fires.put(position, fire);
            removePlant(position);
            notifyListeners(EventCreator.createFirePlacedEvent(fire.getPosition()));
            return true;
        }

        return false;
    }


    @Override
    public boolean canPlaceFire(Vector2d position) {
        return isPositionWithinMapBoundary(position) &&
                !isFireAtPosition(position) &&
                isPlantAtPosition(position);
    }


    @Override
    public void placePlant(Plant plant) throws IncorrectPositionException { // public?
        var position = plant.getPosition();
        if (isFireAtPosition(position)) {
            throw new PositionOccupiedByWorldElementException(position, Plant.class.getSimpleName(), Fire.class.getSimpleName());
        }

        super.placePlant(plant);
    }

    @Override
    public Collection<WorldElement> getElements() {
        return Stream
                .concat(super.getElements().stream(), fires.values().stream())
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public boolean isFireAtPosition(Vector2d position) {
        return fires != null && fires.containsKey(position);
    }

    private void decreaseFireRemainingLifetime() {
        fires.values().forEach(Fire::decreaseRemainingLifetime);
    }

    private void spreadFire() {
        var newFires = fireSpreader.getSpreadFires(fires.values(), getCurrentBounds(), this);
        newFires.forEach(this::placeFire);
    }

    private void removeBurnedFires() {
        fires.entrySet().removeIf(entry -> entry.getValue().isBurned());
    }

    private void createFire() {
        var plantsPositions = new ArrayList<>(plants.keySet());
        var newFire = fireSpreader.createNewFire(plantsPositions, this);
        newFire.ifPresent(this::placeFire);
    }

}
