package agh.ics.oop.model.exceptions;

import agh.ics.oop.model.Vector2d;

public class PositionOccupiedByFireException extends Exception {
    public PositionOccupiedByFireException(Vector2d position) {
        super(String.format("Position %s is occupied by fire.", position));
    }
}
