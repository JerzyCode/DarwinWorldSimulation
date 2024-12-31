package agh.ics.oop.model.map;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.exceptions.PlantIsAlreadyGrownException;
import agh.ics.oop.model.map.elements.Plant;

import java.util.HashMap;
import java.util.Map;

public class AbstractPlantMap extends AbstractWorldMap implements PlantMap {
  protected final Map<Vector2d, Plant> plants;

  public AbstractPlantMap() {
    this.plants = new HashMap<>();
  }

  @Override
  public Boundary getCurrentBounds() {
    return null;
  }

  @Override
  public void placePlant(Plant plant) throws PlantIsAlreadyGrownException {
    if (isPlantAtPosition(plant.getPosition())) {
      throw new PlantIsAlreadyGrownException(plant.getPosition());
    }
    plants.put(plant.getPosition(), plant);
    notifyListeners("Plant was placed on position: " + plant.getPosition());
  }

  @Override
  public void removePlant(Vector2d position) {
    if (isPlantAtPosition(position)) {
      plants.remove(position);
      notifyListeners("Plant was removed from position: " + position);
    }
  }

  @Override
  public boolean isPlantAtPosition(Vector2d position) {
    return plants.containsKey(position);
  }
}
