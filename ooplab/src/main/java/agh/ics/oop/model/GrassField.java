package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.*;

public class GrassField extends AbstractWorldMap {
  private final RandomPositionGenerator randomizer;
  private final Map<Vector2d, Grass> grasses;

  public GrassField(int grassCount) {
    this(new RandomPositionGenerator(grassCount, (int)Math.sqrt(grassCount * 10) + 1, (int)Math.sqrt(grassCount * 10) + 1));
  }

  GrassField(RandomPositionGenerator randomizer) { //constructor for testing purposes
    super();
    super.rightTopCorner = new Vector2d(0, 0);
    this.grasses = new HashMap<>();
    this.randomizer = randomizer;
    placeGrass();
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

  private void placeGrass() {
    randomizer.forEach(position -> {
      rightTopCorner = rightTopCorner.upperRight(position);
      var grass = new Grass(position);
      grasses.put(grass.getPosition(), grass);
    });
  }

  Vector2d getRightTopCorner() { //testing purposes
    return rightTopCorner;
  }
}
