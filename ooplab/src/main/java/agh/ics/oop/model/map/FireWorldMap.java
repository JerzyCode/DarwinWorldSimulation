package agh.ics.oop.model.map;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Fire;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.exceptions.PositionOccupiedByFireException;

import java.util.Set;

public interface FireWorldMap extends WorldMap {

    boolean isFireAtPosition(Vector2d position);
    void placeFire(Fire fire) throws IncorrectPositionException, PositionOccupiedByFireException;
    void decreaseFireRemainingLifetime();
    void removeFire(Fire fire);
    Set<Fire> getBurnedFires();

}
