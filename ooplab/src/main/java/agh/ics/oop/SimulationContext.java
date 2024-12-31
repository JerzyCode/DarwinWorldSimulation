package agh.ics.oop;

import agh.ics.oop.factory.AnimalFactory;
import agh.ics.oop.factory.PlantFactory;
import agh.ics.oop.factory.WorldMapFactory;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.exceptions.PlantIsAlreadyGrownException;
import agh.ics.oop.model.map.AbstractWorldMap;
import agh.ics.oop.model.map.PlantMap;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.map.elements.Animal;
import agh.ics.oop.model.map.elements.Plant;
import agh.ics.oop.model.move.MoveDirection;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationContext {
  private final Random random = new Random();
  private final Configuration configuration;
  private final AnimalFactory animalFactory;
  private final PlantFactory plantFactory;
  private final WorldMap worldMap;
  private final List<Animal> animals;
  private final List<Plant> plants;

  public SimulationContext(Configuration configuration) {
    this.configuration = configuration;
    this.animalFactory = new AnimalFactory(configuration.getAnimalConfiguration());
    WorldMapFactory worldMapFactory = new WorldMapFactory(configuration.getWorldMapConfiguration());
    this.plantFactory = new PlantFactory(configuration.getSimulationConfiguration().getPlantVariant());
    this.worldMap = worldMapFactory.createWorldMap();
    this.plants = createPlants(configuration.getWorldMapConfiguration().getStartPlantCount());
    this.animals = createAnimals();
  }

  public void handleDayEnds() {
    handleAnimalsMove();
    handleGrassEating();
    handleCopulate();
    handleAnimalLossEnergy();
    handlePlantGrowth();
  }

  private void handleAnimalsMove() {
    animals.forEach(animal -> worldMap.move(animal, getRandomMoveDirection()));
  }

  private void handleGrassEating() {
    if (worldMap instanceof PlantMap plantMap)
      animals.forEach(animal -> {
        if (plantMap.isPlantAtPosition(animal.getPosition())) {
          plantMap.removePlant(animal.getPosition());
          plants.removeIf(grass -> grass.getPosition() == animal.getPosition());
          //TODO increase animal energy
        }
      });

  }

  private void handlePlantGrowth() {
    createPlants(configuration.getSimulationConfiguration().getPlantGrowth());
  }

  private void handleCopulate() {

  }

  private void handleAnimalLossEnergy() {

  }

  private List<Plant> createPlants(int count) {
    //TODO walidacja ze nie moze byc startPlantCount > width * height
    if (!(worldMap instanceof PlantMap)) {
      return new ArrayList<>();
    }
    var boundary = worldMap.getCurrentBounds();
    List<Plant> plants = new ArrayList<>();
    for (int i = 0; i < count; i++) {

      var plant = plantFactory.createPlant(boundary);
      try {
        ((PlantMap)worldMap).placePlant(plant);
        plants.add(plant);
      }
      catch (PlantIsAlreadyGrownException e) {
        System.out.println(e.getMessage());
      }
    }
    return plants;
  }

  private List<Animal> createAnimals() {
    var boundary = worldMap.getCurrentBounds();
    List<Animal> animals = new ArrayList<>();
    var randomizer = new RandomPositionGenerator(
        configuration.getSimulationConfiguration().getStartAnimalCount(),
        boundary.rightTopCorner().getX(),
        boundary.rightTopCorner().getY());

    for (Vector2d position : randomizer) {
      var animal = animalFactory.createAnimal(position);
      try {
        worldMap.place(animal);
        animals.add(animal);
      }
      catch (IncorrectPositionException e) {
        System.out.println("createAnimals(), animal not placed: message=" + e.getMessage());
      }
    }
    return animals;
  }

  private MoveDirection getRandomMoveDirection() { //TODO do wydzielenia do genów
    return MoveDirection.values()[random.nextInt(4)];
  }

  public void setMapChangeListener(MapChangeListener listener) {
    ((AbstractWorldMap)worldMap).addListener(listener);
  }

  public WorldMap getWorldMap() {
    return worldMap;
  }
}
