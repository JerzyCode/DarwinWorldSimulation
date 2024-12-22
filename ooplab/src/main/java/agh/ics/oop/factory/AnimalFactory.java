package agh.ics.oop.factory;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.AnimalConfiguration;
import agh.ics.oop.model.map.elements.Animal;

public class AnimalFactory {
  private final AnimalConfiguration animalConfiguration;

  public AnimalFactory(AnimalConfiguration animalConfiguration) {
    this.animalConfiguration = animalConfiguration;
  }

  public Animal createAnimal(Vector2d vector) {
    //TODO po dodaniu dodatkowych pól do animal
    return new Animal(vector);
  }
}
