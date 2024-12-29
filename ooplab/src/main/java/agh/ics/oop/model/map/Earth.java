package agh.ics.oop.model.map;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.elements.Animal;
import agh.ics.oop.model.map.elements.WorldElement;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class Earth extends AbstractWorldMap {
  @Override
  public void place(Animal animal) throws IncorrectPositionException {

  }

  @Override
  public void move(Animal animal, MoveDirection direction) {

  }

  @Override
  public boolean isOccupied(Vector2d position) {
    return false;
  }

  @Override
  public WorldElement objectAt(Vector2d position) {
    return null;
  }

  @Override
  public Collection<WorldElement> getElements() {
    return List.of();
  }

  @Override
  public Boundary getCurrentBounds() {
    return null;
  }

  @Override
  public UUID getId() {
    return null;
  }

  @Override
  public boolean canMoveTo(Vector2d position) {
    return false;
  }
}
