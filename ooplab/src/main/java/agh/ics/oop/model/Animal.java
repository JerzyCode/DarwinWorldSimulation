package agh.ics.oop.model;

public class Animal {
  private static final Vector2d RIGHT_CORNER = new Vector2d(4, 4);
  private static final Vector2d LEFT_CORNER = new Vector2d(0, 0);

  private Vector2d position;
  private MapDirection orientation;

  public Animal() {
    this(new Vector2d(2, 2));
  }

  public Animal(Vector2d position) {
    this.orientation = MapDirection.NORTH;
    this.position = position;
  }

  public boolean isAt(Vector2d position) {
    return this.position.equals(position);
  }

  public void move(MoveDirection moveDirection) {
    switch (moveDirection) { //TODO move ma sie ruszać, validator waliduje
      case FORWARD -> updatePosition(position.add(orientation.toUnitVector()));
      case BACKWARD -> updatePosition(position.subtract(orientation.toUnitVector()));
      case LEFT -> orientation = orientation.previous();
      case RIGHT -> orientation = orientation.next();
    }
  }

  private void updatePosition(Vector2d newPosition) {
    if (isNewPositionAllowed(newPosition)) {
      position = newPosition;
    }
  }

  private boolean isNewPositionAllowed(Vector2d newPosition) {
    return newPosition.precedes(RIGHT_CORNER) && newPosition.follows(LEFT_CORNER);
  }

  @Override
  public String toString() {
    return "Animal(" +
        "position=" + position +
        ", direction=" + orientation +
        ')';
  }

  public MapDirection getOrientation() {
    return orientation;
  }

  public Vector2d getPosition() {
    return position;
  }
}
