package agh.ics.oop;

import agh.ics.oop.factory.AnimalFactory;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.configuration.SimulationConfiguration;
import agh.ics.oop.model.configuration.WorldMapConfiguration;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.GrassField;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.map.elements.Animal;
import agh.ics.oop.model.map.elements.Grass;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TODO ogólny refactor tej klasy + testy integracyjne
public class SimulationWithConfig implements Runnable {
  private final SimulationConfiguration simulationConfiguration;
  private final WorldMapConfiguration worldMapConfiguration;
  private final AnimalFactory animalFactory;
  private final List<Animal> animals;
  private final List<Grass> plants = new ArrayList<>();
  private final WorldMap worldMap;

  public SimulationWithConfig(Configuration configuration, WorldMap worldMap) {
    this.simulationConfiguration = configuration.getSimulationConfiguration();
    this.worldMapConfiguration = configuration.getWorldMapConfiguration();
    this.animalFactory = new AnimalFactory(configuration.getAnimalConfiguration());
    this.worldMap = worldMap;
    animals = createAnimals();
    //    plants = createPlants(); TODO początkowe planty powinny być tworzone w mapie
  }

  @Override
  public void run() {
    int animalCount = animals.size();
    int maxDays = 50;

    for (int i = 0; i < maxDays; i++) {
      var index = i % animalCount;
      var animal = animals.get(index);
      try {
        worldMap.move(animal, getRandomMoveDirection());
        createRandomPlants();
        Thread.sleep(500);
      }
      catch (InterruptedException e) {
        System.out.println("Simulation was interrupted!!");
        return;
      }
    }
  }

  private MoveDirection getRandomMoveDirection() { //TODO do wydzielenia do genów
    var random = new Random();
    return MoveDirection.values()[random.nextInt(4)];
  }

  private void createRandomPlants() {
    var randomizer = new RandomPositionGenerator(
        simulationConfiguration.getPlantGrowth(),
        worldMapConfiguration.getWidth(),
        worldMapConfiguration.getHeight());

    for (Vector2d pos : randomizer) {
      var grass = new Grass(pos);
      plants.add(grass);
      addPlantOnMap(grass);
    }

  }

  private List<Animal> createAnimals() {
    var boundary = worldMap.getCurrentBounds();
    var maxWidth = Math.max(boundary.rightTopCorner().getX(), worldMapConfiguration.getWidth());
    var maxHeight = Math.max(boundary.rightTopCorner().getY(), worldMapConfiguration.getHeight());
    List<Animal> animals = new ArrayList<>();
    var randomizer = new RandomPositionGenerator(
        simulationConfiguration.getStartAnimalCount(),
        maxWidth,
        maxHeight);

    for (Vector2d position : randomizer) {
      System.out.println("position=" + position);
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

  private void addPlantOnMap(Grass grass) { //TODO do wywalenia
    if (worldMap instanceof GrassField) {
      ((GrassField)worldMap).placeGrass(grass);
    }
  }

}
