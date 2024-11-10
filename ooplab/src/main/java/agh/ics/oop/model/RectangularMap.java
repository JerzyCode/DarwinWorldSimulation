package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.HashMap;
import java.util.Map;

public class RectangularMap implements WorldMap {
  private final Map<Vector2d, Animal> animals;
  private final Vector2d leftBotCorner;
  private final Vector2d rightTopCorner;

  public RectangularMap(int width, int height) {
    this.animals = new HashMap<>();
    this.leftBotCorner = new Vector2d(0, 0);
    this.rightTopCorner = new Vector2d(width - 1, height - 1);
  }

  @Override
  public boolean place(Animal animal) {
    var position = animal.getPosition();
    if (canMoveTo(position)) {
      return false;
    }

    animals.put(position, animal);
    return true;
  }

  @Override
  public void move(Animal animal, MoveDirection direction) {
    if (animals.containsValue(animal)) {
      animal.move(direction, this);
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
    return !isOccupied(position) && position.follows(leftBotCorner) && position.precedes(rightTopCorner);
  }

  @Override
  public String toString() {
    var mapVisualizer = new MapVisualizer(this);
    return mapVisualizer.draw(leftBotCorner, rightTopCorner);
  }

}
