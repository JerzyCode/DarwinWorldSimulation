package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public class RectangularMap implements WorldMap {
  private final Map<Vector2d, Animal> animals;
  private final Vector2d leftBotCorner;
  private final Vector2d rightTopCorner;
  private final MapVisualizer mapVisualizer;

  public RectangularMap(int width, int height) {
    this.mapVisualizer = new MapVisualizer(this);
    this.animals = new HashMap<>();
    this.leftBotCorner = new Vector2d(0, 0);
    this.rightTopCorner = new Vector2d(width - 1, height - 1);
  }

  @Override
  public boolean place(Animal animal) {
    var position = animal.getPosition();
    if (!canMoveTo(position)) {
      return false;
    }

    animals.put(position, animal);
    return true;
  }

  @Override
  public void move(Animal animal, MoveDirection direction) {
    if (animals.containsValue(animal)) {
      animals.remove(animal.getPosition());
      animal.move(direction, this);
      animals.put(animal.getPosition(), animal);
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
  public Collection<WorldElement> getElements() {
    return Collections.unmodifiableCollection(new ArrayList<>(animals.values()));
  }

  @Override
  public boolean canMoveTo(Vector2d position) {
    return !isOccupied(position) && position.follows(leftBotCorner) && position.precedes(rightTopCorner);
  }

  @Override
  public String toString() {
    return mapVisualizer.draw(leftBotCorner, rightTopCorner);
  }

  //for testing purposes
  Vector2d getRightTopCorner() {
    return rightTopCorner;
  }
}
