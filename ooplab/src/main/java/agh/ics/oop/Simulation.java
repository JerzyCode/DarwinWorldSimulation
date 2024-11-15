package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

  private final List<Animal> animals;
  private final List<MoveDirection> moveDirections;
  private final WorldMap worldMap;

  public Simulation(List<Vector2d> animalsPositions, List<MoveDirection> moveDirections, WorldMap worldMap) {
    this.worldMap = worldMap;
    this.animals = createAndPlaceAnimals(animalsPositions);
    this.moveDirections = moveDirections;
  }

  public void run() {
    System.out.println(worldMap);
    int animalCount = animals.size();
    int directionsSize = moveDirections.size();

    for (int i = 0; i < directionsSize; i++) {
      var index = i % animalCount;
      var animal = animals.get(index);
      worldMap.move(animal, moveDirections.get(i));
      System.out.println(worldMap);
    }

  }

  private List<Animal> createAndPlaceAnimals(List<Vector2d> animalsPositions) {
    List<Animal> animals = new ArrayList<>();
    animalsPositions.forEach(position -> {
      var animal = new Animal(position);
      if (worldMap.place(animal)) {
        animals.add(animal);
      }

    });
    return animals;
  }

  List<Animal> getAnimals() {
    return animals;
  }
}
