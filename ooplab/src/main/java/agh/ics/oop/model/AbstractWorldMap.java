package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

abstract class AbstractWorldMap implements WorldMap {
  private final MapVisualizer mapVisualizer;
  protected final Map<Vector2d, Animal> animals;

  protected AbstractWorldMap() {
    this.mapVisualizer = new MapVisualizer(this);
    this.animals = new HashMap<>();
  }

  @Override
  public void place(Animal animal) throws IncorrectPositionException {
    var position = animal.getPosition();
    if (!canMoveTo(position)) {
      throw new IncorrectPositionException(position);
    }

    animals.put(position, animal);
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
    return !isOccupied(position);
  }

  public abstract Boundary getCurrentBounds();

  @Override
  public final String toString() {
    var bounds = getCurrentBounds();
    return mapVisualizer.draw(bounds.leftBottomCorner(), bounds.rightTopCorner());
  }
}
