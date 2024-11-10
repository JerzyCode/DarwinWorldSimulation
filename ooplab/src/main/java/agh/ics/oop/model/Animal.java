package agh.ics.oop.model;

public class Animal {

  private Vector2d position;
  private MapDirection orientation;

  public Animal() {
    this(new Vector2d(2, 2));
  }

  public Animal(Vector2d position) {
    this(position, MapDirection.NORTH);
  }

  Animal(Vector2d position, MapDirection orientation) {
    this.orientation = orientation;
    this.position = position;
  }

  public boolean isAt(Vector2d position) {
    return this.position.equals(position);
  }

  public void move(MoveDirection moveDirection) {
    switch (moveDirection) {
      case FORWARD -> position = position.add(orientation.toUnitVector());
      case BACKWARD -> position = position.subtract(orientation.toUnitVector());
      case LEFT -> orientation = orientation.previous();
      case RIGHT -> orientation = orientation.next();
    }
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
