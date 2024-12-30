package agh.ics.oop.factory;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.AnimalConfiguration;
import agh.ics.oop.model.map.elements.Animal;
import agh.ics.oop.model.map.elements.Gen;
import agh.ics.oop.model.map.elements.Genome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnimalFactory {
  private final AnimalConfiguration animalConfiguration;
  private final Random rand = new Random();

  public AnimalFactory(AnimalConfiguration animalConfiguration) {
    this.animalConfiguration = animalConfiguration;
  }

  public Animal createAnimal(Vector2d vector) {
    //TODO po dodaniu dodatkowych pól do animal

    return new Animal(createRandomGenome(), vector);
  }

  private Genome createRandomGenome() {
    List<Gen> gens = new ArrayList<>();
    for (int i = 0; i < animalConfiguration.getGenomeLength(); i++) {
      gens.add(new Gen(rand.nextInt(0, 8)));
    }

    return new Genome(gens);
  }
}
