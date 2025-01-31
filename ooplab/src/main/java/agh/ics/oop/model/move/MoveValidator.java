package agh.ics.oop.model.move;

import agh.ics.oop.model.Vector2d;

@FunctionalInterface
public interface MoveValidator { // czy to jest używane?
    boolean canMoveTo(Vector2d position);
}
