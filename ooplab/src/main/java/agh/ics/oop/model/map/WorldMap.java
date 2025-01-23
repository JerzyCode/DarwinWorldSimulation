package agh.ics.oop.model.map;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.move.MoveDirection;
import agh.ics.oop.model.move.MoveValidator;

import java.util.Collection;
import java.util.UUID;

public interface WorldMap extends MoveValidator {

    void place(Animal animal) throws IncorrectPositionException;

    void move(Animal animal, MoveDirection direction);

    boolean isOccupied(Vector2d position);

    WorldElement objectAt(Vector2d position);

    Collection<WorldElement> getElements();

    Boundary getCurrentBounds();

    UUID getId();
}