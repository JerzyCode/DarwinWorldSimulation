package agh.ics.oop.model.map;

import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.elements.Animal;
import agh.ics.oop.model.map.elements.WorldElement;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

abstract class AbstractWorldMap implements WorldMap {
  private final MapVisualizer mapVisualizer;
  private final List<MapChangeListener> mapChangeListeners;
  private final UUID id;
  protected final Map<Vector2d, Animal> animals;

  protected AbstractWorldMap() {
    this.mapVisualizer = new MapVisualizer(this);
    this.mapChangeListeners = new ArrayList<>();
    this.animals = new HashMap<>();
    this.id = UUID.randomUUID();
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public void place(Animal animal) throws IncorrectPositionException {
    var position = animal.getPosition();
    if (!canMoveTo(position)) {
      throw new IncorrectPositionException(position);
    }

    animals.put(position, animal);
    notifyListeners("Animal was placed at position: " + position);
  }

  @Override
  public void move(Animal animal, MoveDirection direction) {
    if (animals.containsValue(animal)) {
      animals.remove(animal.getPosition());
      animal.move(direction, this);
      animals.put(animal.getPosition(), animal);
      notifyListeners("Animal moved to position: " + animal.getPosition());
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

  public void addListener(MapChangeListener listener) {
    mapChangeListeners.add(listener);
  }

  public void removeListener(MapChangeListener listener) {
    mapChangeListeners.remove(listener);
  }

  protected void notifyListeners(String message) {
    mapChangeListeners.forEach(listener -> listener.mapChanged(this, message));
  }
}
