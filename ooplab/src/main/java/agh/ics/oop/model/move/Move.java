package agh.ics.oop.model.move;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;

import java.util.Objects;

public class Move {
  private final Vector2d toPosition;
  private final MapDirection orientation;

  public Move(Vector2d position, MapDirection direction) {
    if (position == null) {
      throw new IllegalArgumentException("Position cannot be null");
    }
    if (direction == null) {
      throw new IllegalArgumentException("Direction cannot be null");
    }

    this.toPosition = position;
    this.orientation = direction;
  }

  public MapDirection getOrientation() {
    return orientation;
  }

  public Vector2d getToPosition() {
    return toPosition;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object)
      return true;
    if (object == null || getClass() != object.getClass())
      return false;
    Move move = (Move)object;
    return Objects.equals(toPosition, move.toPosition) && orientation == move.orientation;
  }

  @Override
  public int hashCode() {
    return Objects.hash(toPosition, orientation);
  }
}
