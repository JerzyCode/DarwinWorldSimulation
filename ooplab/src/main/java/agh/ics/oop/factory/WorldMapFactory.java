package agh.ics.oop.factory;

import agh.ics.oop.model.configuration.WorldMapConfiguration;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.Earth;
import agh.ics.oop.model.map.FireEarth;
import agh.ics.oop.model.map.GrassField;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.map.elements.Animal;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.ArrayList;
import java.util.List;

public class WorldMapFactory {
  private final WorldMapConfiguration worldMapConfiguration;

  public WorldMapFactory(WorldMapConfiguration worldMapConfiguration) {
    this.worldMapConfiguration = worldMapConfiguration;
  }

  public WorldMap createWorldMap() {
    return switch (worldMapConfiguration.mapVariant()) {
      case EARTH -> createEarth();
      case FIRE -> createFireEarth();
      case GRASS_FIELD -> createGrassField();
    };
  }

  private GrassField createGrassField() {
    var grassField = new GrassField(worldMapConfiguration.startPlantCount());
    var animals = createAnimals();
    try {
      for (Animal animal : animals) {
        grassField.place(animal);
      }
    }
    catch (IncorrectPositionException e) {
      System.out.println(e.getMessage());
      throw new RuntimeException("Placing animal at occupied position!");
    }

    return grassField;
  }

  private Earth createEarth() {
    return new Earth(); //TODO
  }

  private FireEarth createFireEarth() {
    return new FireEarth(); //TODO
  }

  private List<Animal> createAnimals() {
    List<Animal> animals = new ArrayList<>();
    var animalCount = worldMapConfiguration.startAnimalCount();
    var randomizer = new RandomPositionGenerator(animalCount, worldMapConfiguration.width(), worldMapConfiguration.height());
    randomizer.forEach(position -> animals.add(new Animal(position)));

    return animals;
  }
}
