package agh.ics.oop.model.map.plant;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.WorldMap;

public interface PlantWorldMap extends WorldMap {
    void placePlant(Plant plant) throws IncorrectPositionException;

    void removePlant(Vector2d position);

    boolean isPlantAtPosition(Vector2d position);

    int getPlantCount();
}
