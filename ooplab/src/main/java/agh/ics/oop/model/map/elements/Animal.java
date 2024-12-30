package agh.ics.oop.model.map.elements;

import agh.ics.oop.model.*;

public class Animal implements WorldElement {

  private Vector2d position;

  private MapDirection orientation;

  public void setOrientation(MapDirection orientation) {
    this.orientation = orientation;
  }

  public void setPosition(Vector2d position) {
    this.position = position;
  }

  public Animal() {
    this(new Vector2d(2, 2));
  }

  public Animal(Vector2d position) {
    this(position, MapDirection.NORTH);
  }

  public Animal(Vector2d position, MapDirection orientation) {
    this.orientation = orientation;
    this.position = position;
  }

  public boolean isAt(Vector2d position) {
    return this.position.equals(position);
  }

  public void changeDirection(int angle) {

  }

  public void move(MoveDirection moveDirection, MoveValidator validator) {
    switch (moveDirection) {
      case FORWARD -> updatePosition(position.add(orientation.toUnitVector()), validator);
      case BACKWARD -> updatePosition(position.subtract(orientation.toUnitVector()), validator);
      case LEFT -> orientation = orientation.previous_right_angle();
      case RIGHT -> orientation = orientation.next_right_angle();
    }

    validator.adjustAnimalAfterMove(this);
  }

  private void updatePosition(Vector2d newPosition, MoveValidator validator) {
    if (validator.canMoveTo(newPosition)) {
      position = newPosition;
    }
  }

  @Override
  public String toString() {
    return orientation.getSymbol();
  }

  @Override
  public Vector2d getPosition() {
    return position;
  }

  public MapDirection getOrientation() {
    return orientation;
  }
}
