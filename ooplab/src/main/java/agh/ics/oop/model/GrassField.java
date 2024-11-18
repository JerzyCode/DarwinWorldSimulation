package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GrassField implements WorldMap {
  private Random randomizer;
  private final Map<Vector2d, Grass> grasses;
  private final Map<Vector2d, Animal> animals;
  private final MapVisualizer mapVisualizer;
  private final Vector2d leftBotCorner;
  private Vector2d rightTopCorner;

  GrassField(int grassCount, Random randomizer) { //constructor for testing purposes
    this(grassCount);
    this.randomizer = randomizer;
  }

  public GrassField(int grassCount) {
    this.mapVisualizer = new MapVisualizer(this);
    this.grasses = new HashMap<>();
    this.animals = new HashMap<>();
    this.leftBotCorner = new Vector2d(0, 0);
    this.randomizer = new Random();

    placeGrass(grassCount);
    this.rightTopCorner = initializeRightTopCorner();
  }

  @Override
  public boolean place(Animal animal) {
    var position = animal.getPosition();
    if (!canMoveTo(position)) {
      return false;
    }

    animals.put(position, animal);
    return true;
  }

  @Override
  public void move(Animal animal, MoveDirection direction) {
    if (animals.containsValue(animal)) {
      animals.remove(animal.getPosition());
      animal.move(direction, this);
      animals.put(animal.getPosition(), animal);
      rightTopCorner = rightTopCorner.upperRight(animal.getPosition());
    }
  }

  @Override
  public boolean isOccupied(Vector2d position) {
    return animals.containsKey(position) || grasses.containsKey(position);
  }

  @Override
  public WorldElement objectAt(Vector2d position) {
    var animal = animals.get(position);
    return animal != null ? animal : grasses.get(position);
  }

  @Override
  public boolean canMoveTo(Vector2d position) {
    var objectAt = objectAt(position);
    return position.follows(leftBotCorner) && !(objectAt instanceof Animal);
  }

  @Override
  public String toString() {
    return mapVisualizer.draw(leftBotCorner, rightTopCorner);
  }

  private void placeGrass(int grassCount) {
    int count = 0;
    while (count <= grassCount) {
      var grass = new Grass(generateRandomVector(grassCount));
      if (!isOccupied(grass.getPosition())) {
        grasses.put(grass.getPosition(), grass);
        count += 1;
      }
    }
  }

  private Vector2d initializeRightTopCorner() {
    return grasses.keySet().stream().max((v1, v2) -> {
      if (v2.precedes(v1)) {
        return 1;
      }
      else if (v1.equals(v2)) {
        return 0;
      }
      else {
        return -1;
      }
    }).orElse(new Vector2d(4, 4)); // map size if grass count is 0
  }

  private Vector2d generateRandomVector(int grassCount) {
    var randomX = randomizer.nextInt(0, (int)Math.sqrt(grassCount * 10) + 1);
    var randomY = randomizer.nextInt(leftBotCorner.getY(), (int)Math.sqrt(grassCount * 10) + 1);
    return new Vector2d(randomX, randomY);
  }

}
