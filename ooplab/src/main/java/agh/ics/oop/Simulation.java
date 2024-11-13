package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.WorldMap;

import java.util.ArrayList;
import java.util.List;

public class Simulation<T, P> {

  private final List<T> objects;
  private final List<MoveDirection> moveDirections;
  private final WorldMap<T, P> worldMap;

  public Simulation(List<T> objects, List<MoveDirection> moveDirections, WorldMap<T, P> worldMap) {
    this.worldMap = worldMap;
    this.objects = placeObjectsOnMap(objects);
    this.moveDirections = moveDirections;
  }

  public void run() {
    System.out.println(worldMap);
    int objectsSize = objects.size();
    int directionsSize = moveDirections.size();

    for (int i = 0; i < directionsSize; i++) {
      var index = i % objectsSize;
      var object = objects.get(index);
      worldMap.move(object, moveDirections.get(i));
      System.out.println(worldMap);
    }

  }

  private List<T> placeObjectsOnMap(List<T> objects) {
    List<T> result = new ArrayList<>();
    objects.forEach(object -> {
      if (worldMap.place(object)) {
        result.add(object);
      }
    });
    return result;
  }

  List<T> getObjects() {
    return objects;
  }
}
