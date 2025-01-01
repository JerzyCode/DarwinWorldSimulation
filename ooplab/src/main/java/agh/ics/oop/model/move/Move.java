package agh.ics.oop.model.move;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;

public class Move {
  private final Vector2d toPosition;
  private final MapDirection orientation;

  public Move(Vector2d position, MapDirection direction) {
    this.toPosition = position;
    this.orientation = direction;
  }

  public MapDirection getOrientation() {
    return orientation;
  }

  public Vector2d getToPosition() {
    return toPosition;
  }
}
