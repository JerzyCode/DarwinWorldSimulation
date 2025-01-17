package agh.ics.oop.model.map.fire;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Fire;
import agh.ics.oop.model.map.plant.PlantWorldMap;

public interface FireWorldMap extends PlantWorldMap {
    boolean isFireAtPosition(Vector2d position);

    boolean placeFire(Fire fire);
}
