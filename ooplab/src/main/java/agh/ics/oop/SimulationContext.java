package agh.ics.oop;

import agh.ics.oop.factory.AnimalFactory;
import agh.ics.oop.factory.PlantFactory;
import agh.ics.oop.factory.WorldMapFactory;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
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
    this.plants =  new ArrayList<>();
    this.animals = new ArrayList<>();

    createPlants(configuration.getWorldMapConfiguration().getStartPlantCount());
    createAnimals();
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

  private void createPlants(int plantCount) {
    int countOfPlantsBeforeCreating = plants.size();
    var countOfAvailablePlacesForPlants = worldMap.getSize() - countOfPlantsBeforeCreating;
    plantCount = Math.min(plantCount, countOfAvailablePlacesForPlants);

    int placedPlantsCount = 0;

    while (placedPlantsCount < plantCount) {
      try {
        var plant = plantFactory.createPlant(worldMap.getCurrentBounds());
        ((PlantMap)worldMap).placePlant(plant);
        plants.add(plant);
        placedPlantsCount++;
      }
      catch (IncorrectPositionException e) {
        System.out.println("Couldn't create plant: " + e.getMessage());
      }
    }
  }

  private void createAnimals() {
    var boundary = worldMap.getCurrentBounds();
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
