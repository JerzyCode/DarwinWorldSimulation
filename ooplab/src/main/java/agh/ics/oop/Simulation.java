package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

  private final List<Animal> animals;
  private final List<MoveDirection> moveDirections;

  public Simulation(List<Vector2d> animalsPositions, List<MoveDirection> moveDirections) {
    this.animals = createAnimals(animalsPositions);
    this.moveDirections = moveDirections;
  }

  public void run() {
    int animalCount = animals.size();
    int directionsSize = moveDirections.size();

    for (int i = 0; i < directionsSize; i++) {
      var index = i % animalCount;
      var animal = animals.get(index);
      animal.move(moveDirections.get(i));
      System.out.printf("Zwierzę: %d, %s%n", index, animal);
    }

  }

  private List<Animal> createAnimals(List<Vector2d> animalsPositions) {
    List<Animal> animals = new ArrayList<>();
    animalsPositions.forEach(position -> animals.add(new Animal(position)));
    return animals;
  }

  public List<Animal> getAnimals() {
    return animals;
  }
}
