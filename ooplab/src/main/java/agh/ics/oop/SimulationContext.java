package agh.ics.oop;

import agh.ics.oop.factory.AnimalFactory;
import agh.ics.oop.factory.PlantFactory;
import agh.ics.oop.factory.WorldMapFactory;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Fire;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.exceptions.PositionOccupiedByFireException;
import agh.ics.oop.model.map.AbstractWorldMap;
import agh.ics.oop.model.map.FireEarth;
import agh.ics.oop.model.map.PlantMap;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.move.MoveDirection;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.*;
import java.util.stream.Collectors;

public class SimulationContext {
  private final Random random = new Random();
  private final Configuration configuration;
  private final AnimalFactory animalFactory;
  private final PlantFactory plantFactory;
  private final WorldMap worldMap;
  private final Set<Animal> animals;
  private final Set<Plant> plants;
  private final Set<Fire> fires;
  private int currentDay;

  public SimulationContext(Configuration configuration) {
    this.configuration = configuration;
    this.animalFactory = new AnimalFactory(configuration.getAnimalConfiguration());
    WorldMapFactory worldMapFactory = new WorldMapFactory(configuration.getWorldMapConfiguration());
    this.plantFactory = new PlantFactory(configuration.getSimulationConfiguration().getPlantVariant());
    this.worldMap = worldMapFactory.createWorldMap();
    this.plants = new HashSet<>();
    this.animals = new HashSet<>();
    this.fires = new HashSet<>();
    currentDay = 1;

    createPlants(configuration.getSimulationConfiguration().getStartPlantCount());
    createAnimals();
  }

  public void handleDayEnds() {
    System.out.println("Current day: " + currentDay);
    clearDeadAnimals();
    handleAnimalsMove();
    handlePlantEating();
    handleCopulate();
    handleAnimalLossEnergy();
    handlePlantGrowth();
    handleFirefightings();
    currentDay++;
  }

  private void handleAnimalsMove() {
    animals.forEach(animal -> worldMap.move(animal, MoveDirection.FORWARD));
  }

  private void handlePlantEating() {
    if (worldMap instanceof PlantMap plantMap)
      animals.forEach(animal -> {
        var position = animal.getPosition();
        if (plantMap.isPlantAtPosition(position)) {
          plants.remove(plantMap.getPlantAtPosition(position));
          plantMap.removePlant(position);
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

  private void handleFirefightings() {
    if (worldMap instanceof FireEarth fireEarth) {
      fireEarth.decreaseFireRemainingLifetime();
      removeBurnedFires();
      spreadFire();
      var fireFrequency = configuration.getSimulationConfiguration().getFireFrequency();
      if (fireFrequency > 0 && currentDay % fireFrequency == 0) {
        createFire();
      }
    }
  }

  private void removeBurnedFires() {
    if (worldMap instanceof FireEarth fireEarth) {
      fireEarth.getBurnedFires()
          .forEach(fire -> {
            fires.remove(fire);
            fireEarth.removeFire(fire);
          });
    }
  }

  private void spreadFire() {
    if (worldMap instanceof FireEarth fireEarth) {
      Set<Fire> newFires = new HashSet<>();
      for (Fire fire : fires) {
        var newFirePositions = fireEarth.getNewFirePositions(fire);

        newFirePositions.forEach(firePosition -> {
          try {
            var newFire = new Fire(firePosition, configuration.getSimulationConfiguration().getFireDuration());

            var plant = fireEarth.getPlantAtPosition(firePosition);
            plants.remove(plant);
            fireEarth.removePlant(firePosition);

            fireEarth.placeFire(newFire);
            newFires.add(newFire);

          }
          catch (IncorrectPositionException | PositionOccupiedByFireException e) {
            System.out.println("Couldn't spread fire to new position: " + e.getMessage());
          }
        });
      }
      fires.addAll(newFires);
    }
  }

  //TODO: tak sobie myślę, że skoro na polu może być maksymalnie jedna trawa, tak samo jak maksymalnie jeden ogień to może lepiej to trzymać w HashMapie.
  private void createFire() {
    if (worldMap instanceof FireEarth fireEarth) {

      List<Vector2d> plantsPositions
          = plants.stream()
          .map(Plant::getPosition)
          .collect(Collectors.toCollection(ArrayList::new));

      Collections.shuffle(plantsPositions);

      boolean isFirePlaced = false;
      for (Vector2d position : plantsPositions) {
        try {
          var plant = fireEarth.getPlantAtPosition(position);
          plants.remove(plant);
          fireEarth.removePlant(position);

          var newFire = new Fire(position, configuration.getSimulationConfiguration().getFireDuration());
          fireEarth.placeFire(newFire);
          fires.add(newFire);
          isFirePlaced = true;

        }
        catch (IncorrectPositionException | PositionOccupiedByFireException e) {
          System.out.println("Couldn't create fire: " + e.getMessage());
        }
        if (isFirePlaced) {
          return;
        }
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
