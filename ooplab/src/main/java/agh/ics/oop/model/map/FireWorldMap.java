package agh.ics.oop.model.map;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Fire;

public interface FireWorldMap extends PlantMap {
    boolean isFireAtPosition(Vector2d position);

    boolean placeFire(Fire fire);
}
