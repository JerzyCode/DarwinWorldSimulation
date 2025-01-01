package agh.ics.oop.model.map;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.move.Move;
import agh.ics.oop.model.move.MoveAdjuster;
import agh.ics.oop.model.move.MoveDirection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Earth extends AbstractPlantMap implements MoveAdjuster {
  private final Boundary boundary;

  public Earth(int width, int height) {
    super();
    boundary = new Boundary(new Vector2d(0, 0), new Vector2d(width - 1, height - 1));
  }

  // TODO: remove unused constructor
  public Earth() {
    this(1, 1);
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
  public Collection<WorldElement> getElements() {
    var elements = new ArrayList<WorldElement>();
    elements.addAll(plants.values());
    elements.addAll(super.getElements());

    return Collections.unmodifiableCollection(elements);
  }

  @Override
  public Move adjustMove(Move move) {
    var position = move.getToPosition();
    var orientation = move.getOrientation();

    if (position.getY() < boundary.leftBottomCorner().getY()) {
      position = new Vector2d(position.getX(), boundary.leftBottomCorner().getY());
      orientation = orientation.next().next();
    }
    else if (position.getY() > boundary.rightTopCorner().getY()) {
      position = new Vector2d(position.getX(), boundary.rightTopCorner().getY());
      orientation = orientation.next().next();
    }
    else if (position.getX() < boundary.leftBottomCorner().getX()) {
      position = new Vector2d(boundary.rightTopCorner().getX(), position.getY());
    }
    else if (position.getX() > boundary.rightTopCorner().getX()) {
      position = new Vector2d(boundary.leftBottomCorner().getX(), position.getY());
    }
    return new Move(position, orientation);
  }

  // TODO: różni się tylko jedną linijką ale trzeba zrobić override bo trzeba jakoś przekazać 3 argument do move
  @Override
  public void move(Animal animal, MoveDirection direction) {
    if (animals.containsValue(animal)){
      animals.remove(animal.getPosition());
      animal.move(direction, this, this);
      animals.put(animal.getPosition(), animal);
      notifyListeners("Animal moved to position: " + animal.getPosition());
      System.out.println("After move:" + animals.size());

    }

  }
}

