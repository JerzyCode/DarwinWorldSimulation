package agh.ics.oop;

import agh.ics.oop.factory.AnimalFactory;
import agh.ics.oop.factory.PlantFactory;
import agh.ics.oop.factory.WorldMapFactory;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.AbstractWorldMap;
import agh.ics.oop.model.map.PlantMap;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.move.MoveDirection;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SimulationContext {
  private final Random random = new Random();
  private final Configuration configuration;
  private final AnimalFactory animalFactory;
  private final PlantFactory plantFactory;
  private final WorldMap worldMap;
  private final Set<Animal> animals;
  private final Set<Plant> plants;

  public SimulationContext(Configuration configuration) {
    this.configuration = configuration;
    this.animalFactory = new AnimalFactory(configuration.getAnimalConfiguration());
    WorldMapFactory worldMapFactory = new WorldMapFactory(configuration.getWorldMapConfiguration());
    this.plantFactory = new PlantFactory(configuration.getSimulationConfiguration().getPlantVariant());
    this.worldMap = worldMapFactory.createWorldMap();
    this.plants = new HashSet<>();
    this.animals = new HashSet<>();

    createPlants(configuration.getSimulationConfiguration().getStartPlantCount());
    createAnimals();
  }

  public void handleDayEnds() {
    clearDeadAnimals();
    handleAnimalsMove();
    handleGrassEating();
    handleCopulate();
    handleAnimalLossEnergy();
    handlePlantGrowth();
  }

  private void handleAnimalsMove() {
    animals.forEach(animal -> worldMap.move(animal, MoveDirection.FORWARD));
  }

  private void handleGrassEating() {
    if (worldMap instanceof PlantMap plantMap)
      animals.forEach(animal -> {
        var position = animal.getPosition();
        if (plantMap.isPlantAtPosition(position)) {
          plantMap.removePlant(position);
          plants.remove(plantMap.getPlantAtPosition(position));
          animal.increaseEnergy(configuration.getSimulationConfiguration().getEnergyGain());
        }
      });
  }

  private void handlePlantGrowth() {
    createPlants(configuration.getSimulationConfiguration().getPlantGrowth());
  }

  private void handleCopulate() {

  }

  private void clearDeadAnimals() {
    animals.removeIf(animal -> {
      if (animal.isDead()) {
        worldMap.removeAnimal(animal);
        return true;
      }
      return false;
    });
  }

  private void handleAnimalLossEnergy() {
    animals.forEach(animal -> animal.decreaseEnergy(1));
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

  public void setMapChangeListener(MapChangeListener listener) {
    ((AbstractWorldMap)worldMap).addListener(listener);
  }

  public WorldMap getWorldMap() {
    return worldMap;
  }
}
