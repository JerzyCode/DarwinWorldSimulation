package agh.ics.oop.factory;

2import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.AnimalConfiguration;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Gen;
import agh.ics.oop.model.elements.Genome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnimalFactory {
  private final AnimalConfiguration animalConfiguration;
  private final Random random = new Random();

  public AnimalFactory(AnimalConfiguration animalConfiguration) {
    this.animalConfiguration = animalConfiguration;
  }

  public Animal createAnimal(Vector2d vector) {
    var genome = createGenome();
    return new Animal(animalConfiguration.getStartEnergy(), vector, genome);
  }

  private Genome createGenome() {
    List<Gen> gens = new ArrayList<>();
    for (int i = 0; i < animalConfiguration.getGenomeLength(); i++) {
      gens.add(new Gen(random.nextInt(0, MapDirection.values().length)));
    }

    return new Genome(gens);
  }
}
