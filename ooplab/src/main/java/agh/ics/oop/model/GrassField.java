package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.*;

public class GrassField extends AbstractWorldMap {
  private final RandomPositionGenerator randomizer;
  private final Map<Vector2d, Grass> grasses;
  private Vector2d displayLeftBotCorner;
  private Vector2d displayRightTopCorner;

  public GrassField(int grassCount) {
    this(new RandomPositionGenerator(grassCount, (int)Math.sqrt(grassCount * 10) + 1, (int)Math.sqrt(grassCount * 10) + 1));
  }

  GrassField(RandomPositionGenerator randomizer) { //constructor for testing purposes
    super();
    displayRightTopCorner = new Vector2d(0, 0);
    displayLeftBotCorner = new Vector2d(0, 0);
    this.grasses = new HashMap<>();
    this.randomizer = randomizer;
    placeGrass();
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

  private void placeGrass() {
    randomizer.forEach(position -> {
      displayRightTopCorner = displayRightTopCorner.upperRight(randomVector);
      displayLeftBotCorner = displayLeftBotCorner.lowerLeft(randomVector);
      var grass = new Grass(position);
      grasses.put(grass.getPosition(), grass);
    });
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
