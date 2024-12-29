package agh.ics.oop;

import agh.ics.oop.model.map.elements.Animal;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.exceptions.IncorrectPositionException;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
  private final List<Animal> animals;
  private final List<MoveDirection> moveDirections;
  private final WorldMap worldMap;

  public Simulation(List<Vector2d> animalsPositions, List<MoveDirection> moveDirections, WorldMap worldMap) {
    this.worldMap = worldMap;
    this.animals = createAndPlaceAnimals(animalsPositions);
    this.moveDirections = moveDirections;
  }

  @Override
  public void run() {
    int animalCount = animals.size();
    int directionsSize = moveDirections.size();

    for (int i = 0; i < directionsSize; i++) {
      var index = i % animalCount;
      var animal = animals.get(index);
      try {
        Thread.sleep(0);
        worldMap.move(animal, moveDirections.get(i));
        // TODO: coś nie tak z tym sleep jest
      }
      catch (InterruptedException e) {
        System.out.println("Simulation was interrupted!!");
        return;
      }
    }
  }

  private List<Animal> createAndPlaceAnimals(List<Vector2d> animalsPositions) {
    List<Animal> animals = new ArrayList<>();
    animalsPositions.forEach(position -> {
      var animal = new Animal(position);
      try {
        worldMap.place(animal);
        animals.add(animal);
      }
      catch (IncorrectPositionException e) {
        System.out.println("createAndPlaceAnimals(), animal not placed: message=" + e.getMessage());
      }

    });
    return animals;
  }

  List<Animal> getAnimals() {
    return animals;
  }
}
