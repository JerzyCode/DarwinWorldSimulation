package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomVectorGenerator;

import java.util.*;

public class GrassField extends AbstractWorldMap {
  private final RandomVectorGenerator randomizer;
  private final Map<Vector2d, Grass> grasses;
  private Vector2d displayLeftBotCorner;
  private Vector2d displayRightTopCorner;

  public GrassField(int grassCount) {
    this(grassCount, new RandomVectorGenerator());
  }

  GrassField(int grassCount, RandomVectorGenerator randomizer) { //constructor for testing purposes
    super();
    displayRightTopCorner = new Vector2d(0, 0);
    displayLeftBotCorner = new Vector2d(0, 0);
    this.grasses = new HashMap<>();
    this.randomizer = randomizer;
    placeGrass(grassCount);
  }

  @Override
  public void move(Animal animal, MoveDirection direction) {
    super.move(animal, direction);
    adjustDisplayCorners();
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
    return !(objectAt instanceof Animal);
  }

  private void placeGrass(int grassCount) {
    int count = 0;
    while (count < grassCount) {
      var randomVector = randomizer.generateRandomVector(displayLeftBotCorner.getY(), displayLeftBotCorner.getY(),
          (int)Math.sqrt(grassCount * 10) + 1,
          (int)Math.sqrt(grassCount * 10) + 1);

      displayRightTopCorner = displayRightTopCorner.upperRight(randomVector);
      displayLeftBotCorner = displayLeftBotCorner.lowerLeft(randomVector);

      var grass = new Grass(randomVector);
      if (!isOccupied(grass.getPosition())) {
        grasses.put(grass.getPosition(), grass);
        count += 1;
      }
    }
  }

  Vector2d getDisplayRightTopCorner() { //testing purposes
    return displayRightTopCorner;
  }

  void adjustDisplayCorners() {
    var botLeft = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
    var rightTop = new Vector2d(-Integer.MAX_VALUE, -Integer.MAX_VALUE);

    for (var el : getElements()) {
      botLeft = botLeft.lowerLeft(el.getPosition());
      rightTop = rightTop.upperRight(el.getPosition());
    }
    displayLeftBotCorner = botLeft;
    displayRightTopCorner = rightTop;

  }

  @Override
  public String toString() {
    return mapVisualizer.draw(displayLeftBotCorner, displayRightTopCorner);
  }
}
