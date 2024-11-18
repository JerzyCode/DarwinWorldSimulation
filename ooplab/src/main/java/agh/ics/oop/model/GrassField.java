package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.RandomVectorGenerator;

import java.util.*;

public class GrassField implements WorldMap {
  private final RandomVectorGenerator randomizer;
  private final Map<Vector2d, Grass> grasses;
  private final Map<Vector2d, Animal> animals;
  private final MapVisualizer mapVisualizer;
  private final Vector2d leftBotCorner;
  private Vector2d rightTopCorner;

  public GrassField(int grassCount) {
    this(grassCount, new RandomVectorGenerator());
  }

  GrassField(int grassCount, RandomVectorGenerator randomizer) { //constructor for testing purposes
    this(grassCount, randomizer, new HashMap<>(), new HashMap<>());
  }

  GrassField(int grassCount, RandomVectorGenerator randomizer, Map<Vector2d, Grass> grasses, Map<Vector2d, Animal> animals) { //constructor for testing purposes
    this.mapVisualizer = new MapVisualizer(this);
    this.grasses = grasses;
    this.animals = animals;
    this.leftBotCorner = new Vector2d(0, 0);
    this.rightTopCorner = new Vector2d(0, 0);
    this.randomizer = randomizer;

    placeGrass(grassCount);
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
  public Collection<WorldElement> getElements() {
    var elements = new ArrayList<WorldElement>();
    elements.addAll(animals.values());
    elements.addAll(grasses.values());

    return Collections.unmodifiableCollection(elements);
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
    while (count < grassCount) {
      var randomVector = randomizer.generateRandomVector(leftBotCorner.getY(), leftBotCorner.getY(),
          (int)Math.sqrt(grassCount * 10) + 1,
          (int)Math.sqrt(grassCount * 10) + 1);

      rightTopCorner = rightTopCorner.upperRight(randomVector);

      var grass = new Grass(randomVector);
      if (!isOccupied(grass.getPosition())) {
        grasses.put(grass.getPosition(), grass);
        count += 1;
      }
    }
  }

  Vector2d getRightTopCorner() { //testing purposes
    return rightTopCorner;
  }
}
