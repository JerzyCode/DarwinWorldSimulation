package agh.ics.oop.model.map;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class GrassField extends AbstractPlantMap {
  private RandomPositionGenerator randomizer;
  private Vector2d displayLeftBotCorner;
  private Vector2d displayRightTopCorner;

  public GrassField(int startWidth, int startHeight) {
    super(); //TODO grassfield źle działa, bo tworzą mu się rośliny nie przez simulationContext
    displayLeftBotCorner = new Vector2d(0, 0);
    displayRightTopCorner = new Vector2d(startWidth - 1, startHeight - 1);
  }

  public GrassField(int grassCount) {
    this(new RandomPositionGenerator(grassCount, (int)Math.sqrt(grassCount * 10) + 1, (int)Math.sqrt(grassCount * 10) + 1));
  }

  GrassField(RandomPositionGenerator randomizer) { //constructor for testing purposes
    super();
    this.randomizer = randomizer;
    placeGrass();
  }

  @Override
  public boolean isOccupied(Vector2d position) {
    return super.isOccupied(position) || plants.containsKey(position);
  }

  @Override
  public WorldElement objectAt(Vector2d position) {
    var animal = super.objectAt(position);
    return animal != null ? animal : plants.get(position);
  }

  @Override
  public Collection<WorldElement> getElements() {
    var elements = new ArrayList<WorldElement>();
    elements.addAll(plants.values());
    elements.addAll(super.getElements());

    return Collections.unmodifiableCollection(elements);
  }

  @Override
  public boolean canMoveTo(Vector2d position) {
    var objectAt = objectAt(position);
    return !(objectAt instanceof Animal);
  }

  private void placeGrass() {
    randomizer.forEach(position -> {
      var grass = new Plant(position);
      plants.put(grass.getPosition(), grass);
    });
  }

  @Override
  public Boundary getCurrentBounds() {
    adjustDisplayCorners();
    return new Boundary(displayLeftBotCorner, displayRightTopCorner);
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

}
