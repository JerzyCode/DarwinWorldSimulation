package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomVectorGenerator;

import java.util.*;

public class GrassField extends AbstractWorldMap {
  private final RandomVectorGenerator randomizer;
  private final Map<Vector2d, Grass> grasses;

  public GrassField(int grassCount) {
    this(grassCount, new RandomVectorGenerator());
  }

  GrassField(int grassCount, RandomVectorGenerator randomizer) { //constructor for testing purposes
    super();
    super.rightTopCorner = new Vector2d(0, 0);
    this.grasses = new HashMap<>();
    this.randomizer = randomizer;
    placeGrass(grassCount);
  }

  @Override
  public void move(Animal animal, MoveDirection direction) {
    super.move(animal, direction);
    rightTopCorner = rightTopCorner.upperRight(animal.getPosition());
  }

  @Override
  public boolean isOccupied(Vector2d position) {
    return super.isOccupied(position) || grasses.containsKey(position);
  }

  @Override
  public WorldElement objectAt(Vector2d position) {
    var animal = super.objectAt(position);
    return animal != null ? animal : grasses.get(position);
  }

  @Override
  public Collection<WorldElement> getElements() {
    var elements = new ArrayList<WorldElement>();
    elements.addAll(super.getElements());
    elements.addAll(grasses.values());

    return Collections.unmodifiableCollection(elements);
  }

  @Override
  public boolean canMoveTo(Vector2d position) {
    var objectAt = objectAt(position);
    return position.follows(leftBotCorner) && !(objectAt instanceof Animal);
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
