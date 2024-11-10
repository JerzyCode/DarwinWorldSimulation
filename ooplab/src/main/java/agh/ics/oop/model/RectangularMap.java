package agh.ics.oop.model;

import java.util.HashMap;
import java.util.Map;

public class RectangularMap implements WorldMap {
  private final Map<Vector2d, Animal> animals;
  private final Vector2d leftCorner;
  private final Vector2d rightCorner;

  public RectangularMap(int width, int height) {
    this.animals = new HashMap<>();
    this.leftCorner = new Vector2d(0, 0);
    this.rightCorner = new Vector2d(width - 1, height - 1);
  }

  @Override
  public boolean place(Animal animal) {
    var position = animal.getPosition();
    if (isOccupied(position) || !isAllowedPosition(position)) {
      return false;
    }

    animals.put(position, animal);
    return true;
  }

  @Override
  public void move(Animal animal, MoveDirection direction) {
    if (animals.containsValue(animal)) { //TODO validate move
      animal.move(direction);
    }
  }

  @Override
  public boolean isOccupied(Vector2d position) {
    return animals.containsKey(position);
  }

  @Override
  public Animal objectAt(Vector2d position) {
    return animals.get(position);
  }

  @Override
  public boolean canMoveTo(Vector2d position) {
    return !isOccupied(position);
  }

  private boolean isAllowedPosition(Vector2d position) {
    return position.follows(leftCorner) && position.precedes(rightCorner);
  }

}
