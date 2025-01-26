package agh.ics.oop.model.exceptions;

import agh.ics.oop.model.Vector2d;

public class PositionOccupiedByWorldElementException extends IncorrectPositionException {
    private static final String MESSAGE_DIFFERENT_CLASSES = "Can't place %s - position %s is already occupied by %s.";

    public PositionOccupiedByWorldElementException(Vector2d position, String elementType) {
        this(position, elementType, elementType);
    }

    public PositionOccupiedByWorldElementException(Vector2d position, String elementType, String occupiedByElementType) {
        super(String.format(MESSAGE_DIFFERENT_CLASSES, elementType, position, occupiedByElementType));
    }

}
