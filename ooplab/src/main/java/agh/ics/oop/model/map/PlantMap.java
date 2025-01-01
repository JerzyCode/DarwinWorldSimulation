package agh.ics.oop.model.map;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.elements.Plant;

public interface PlantMap extends WorldMap {
  void placePlant(Plant plant) throws IncorrectPositionException;

  void removePlant(Vector2d position);

  boolean isPlantAtPosition(Vector2d position);
}
