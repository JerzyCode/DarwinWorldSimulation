package agh.ics.oop.model.map.plant;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.event.EventCreator;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.exceptions.PositionOccupiedByWorldElementException;
import agh.ics.oop.model.exceptions.PositionOutOfMapBoundaryException;
import agh.ics.oop.model.map.simulation.SimulationAbstractWorldMap;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPlantMap extends SimulationAbstractWorldMap implements PlantWorldMap {
    protected final Map<Vector2d, Plant> plants;

    public AbstractPlantMap() {
        super();
        this.plants = new HashMap<>();
    }

    @Override
    public void placePlant(Plant plant) throws IncorrectPositionException {
        if (!isPositionWithinMapBoundary(plant.getPosition())) {
            throw new PositionOutOfMapBoundaryException(plant.getPosition());
        }
        if (isPlantAtPosition(plant.getPosition())) {
            throw new PositionOccupiedByWorldElementException(plant.getPosition(), Plant.class.getSimpleName());
        }

        plants.put(plant.getPosition(), plant);
        notifyListeners(EventCreator.createPlantPlacedEvent(plant.getPosition()));
    }

    @Override
    public void removePlant(Vector2d position) {
        if (isPlantAtPosition(position)) {
            plants.remove(position);
        }
    }

    @Override
    public boolean isPlantAtPosition(Vector2d position) {
        return plants.containsKey(position);
    }
}
