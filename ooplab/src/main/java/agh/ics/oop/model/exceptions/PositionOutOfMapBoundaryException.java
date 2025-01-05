package agh.ics.oop.model.exceptions;

import agh.ics.oop.model.Vector2d;

public class PositionOutOfMapBoundaryException extends IncorrectPositionException {
    private static final String MESSAGE = "Position %s is out of map boundary.";

    public PositionOutOfMapBoundaryException(Vector2d position) {
        super(String.format(MESSAGE, position));
    }
}
