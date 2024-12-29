package agh.ics.oop.model.map;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.elements.Animal;
import agh.ics.oop.model.map.elements.WorldElement;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class Earth extends AbstractWorldMap {
  private final Boundary boundary;

  public Earth(int width, int height) {
    super();
    boundary = new Boundary(new Vector2d(0, 0), new Vector2d(width - 1, height - 1));
  }

  // TODO: remove unused constructor
  public Earth() {
    this(5, 5);
  }

  @Override
  public Boundary getCurrentBounds() {
    return boundary;
  }

  @Override
  public boolean canMoveTo(Vector2d position) {
    return true;
  }

  @Override
  public void move(Animal animal, MoveDirection direction) {
    if (animals.containsValue(animal)) {
      animals.remove(animal.getPosition());
      animal.move(direction, this);
      fixAnimalPositionAfterMove(animal);
      animals.put(animal.getPosition(), animal);
      super.notifyListeners("Animal moved to position: " + animal.getPosition());
    }
  }

  private void fixAnimalPositionAfterMove(Animal animal){
    var position = animal.getPosition();

    Vector2d newPosition = animal.getPosition();
    MapDirection orientation = animal.getOrientation();
    if (position.getX() > boundary.rightTopCorner().getX()) {
      newPosition = new Vector2d(boundary.leftBottomCorner().getX(), position.getY());
    }
    else if (position.getX() < boundary.leftBottomCorner().getX()) {
      newPosition = new Vector2d(boundary.rightTopCorner().getX(), position.getY());
    }
    else if (position.getY() > boundary.rightTopCorner().getY()) {
      newPosition = new Vector2d(position.getX(), boundary.rightTopCorner().getY());
      orientation = MapDirection.SOUTH;
    }
    else if (position.getY() < boundary.leftBottomCorner().getY()) {
      newPosition = new Vector2d(position.getX(), boundary.leftBottomCorner().getY());
      orientation = MapDirection.NORTH;
    }

    animal.setOrientation(orientation);
    animal.setPosition(newPosition);

  }
}
