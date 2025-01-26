package agh.ics.oop.model.map.fire;

import agh.ics.oop.model.Vector2d;

@FunctionalInterface
public interface FireValidator {
    boolean canPlaceFire(Vector2d position);
}
