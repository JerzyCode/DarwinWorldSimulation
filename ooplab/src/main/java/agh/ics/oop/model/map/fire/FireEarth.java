package agh.ics.oop.model.map.fire;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.PlantVariant;
import agh.ics.oop.model.elements.Fire;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.exceptions.PositionOccupiedByWorldElementException;
import agh.ics.oop.model.map.FireWorldMap;
import agh.ics.oop.model.map.plant.Earth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class FireEarth extends Earth implements FireWorldMap, FireValidator {
    private final HashMap<Vector2d, Fire> fires;
    private final int fireFrequency;
    private final FireSpreader fireSpreader;

    public FireEarth(int width, int height, int fireFrequency, int fireDuration, int plantGrowth, int startPlantCount, PlantVariant plantVariant) {
        super(width, height, plantGrowth, startPlantCount, plantVariant);
        this.fireFrequency = fireFrequency;
        this.fires = new HashMap<>();
        this.fireSpreader = new FireSpreader(fireDuration);
    }

    @Override
    public void handleDayEnds(int currentDay) {
        super.handleDayEnds(currentDay);
        decreaseFireRemainingLifetime();
        spreadFire();
        removeBurnedFires();
        if (fireFrequency > 0 && currentDay % fireFrequency == 0) {
            createFire();
        }
    }


    @Override
    public boolean placeFire(Fire fire) {
        var position = fire.getPosition();
        if (canPlaceFire(position)) {
            fires.put(position, fire);
            removePlant(position);
            notifyListeners("Fire was placed at position: " + position);
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
        notifyListeners("Burned fires were removed from position.");
    }

    private void createFire() {
        var plantsPositions = new ArrayList<>(plants.keySet());
        var newFire = fireSpreader.createNewFire(plantsPositions, this);
        newFire.ifPresent(this::placeFire);
    }

}
