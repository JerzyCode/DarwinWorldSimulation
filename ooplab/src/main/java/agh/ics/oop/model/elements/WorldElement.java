package agh.ics.oop.model.elements;

import agh.ics.oop.model.Vector2d;

@FunctionalInterface
public interface WorldElement {
    Vector2d getPosition();
}
