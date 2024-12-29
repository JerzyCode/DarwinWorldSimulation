package agh.ics.oop;

import agh.ics.oop.factory.AnimalFactory;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.configuration.SimulationConfiguration;
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
public class Simulation implements Runnable {
  private final SimulationConfiguration simulationConfiguration;
  private final Random random = new Random();
  private final AnimalFactory animalFactory;
  private final List<Animal> animals;
  private final List<Grass> plants;
  private final WorldMap worldMap;

  public Simulation(Configuration configuration, WorldMap worldMap) {
    this.simulationConfiguration = configuration.getSimulationConfiguration();
    this.animalFactory = new AnimalFactory(configuration.getAnimalConfiguration());
    this.worldMap = worldMap;
    animals = createAnimals();
    plants = new ArrayList<>();
  }

  @Override
  public void run() {
    for (int i = 0; i < simulationConfiguration.getDaysCount(); i++) {
      try {
        animals.forEach(animal -> worldMap.move(animal, getRandomMoveDirection()));
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
    return MoveDirection.values()[random.nextInt(4)];
  }

  private void createRandomPlants() { //TODO refactor
    var boundary = worldMap.getCurrentBounds();

    var randomizer = new RandomPositionGenerator(
        simulationConfiguration.getPlantGrowth(),
        boundary.rightTopCorner().getX(),
        boundary.rightTopCorner().getY());

    for (Vector2d pos : randomizer) {
      var grass = new Grass(pos);
      plants.add(grass);
      addPlantOnMap(grass);
    }

  }

  private List<Animal> createAnimals() {
    var boundary = worldMap.getCurrentBounds();
    List<Animal> animals = new ArrayList<>();
    var randomizer = new RandomPositionGenerator(
        simulationConfiguration.getStartAnimalCount(),
        boundary.rightTopCorner().getX(),
        boundary.rightTopCorner().getY());

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
