package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

abstract class AbstractWorldMap implements WorldMap {
  protected final MapVisualizer mapVisualizer;
  protected final Map<Vector2d, Animal> animals;
  protected final Vector2d leftBotCorner;
  protected Vector2d rightTopCorner;

  protected AbstractWorldMap() {
    this.mapVisualizer = new MapVisualizer(this);
    this.animals = new HashMap<>();
    this.leftBotCorner = new Vector2d(0, 0);
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
  public WorldElement objectAt(Vector2d position) {
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
}
