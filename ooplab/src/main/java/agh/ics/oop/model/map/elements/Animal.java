package agh.ics.oop.model.map.elements;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.MoveValidator;
import agh.ics.oop.model.Vector2d;

public class Animal implements WorldElement {

  private Vector2d position;

  private MapDirection orientation;

  private Genome genome;

  public void setOrientation(MapDirection orientation) {
    this.orientation = orientation;
  }

  public void setPosition(Vector2d position) {
    this.position = position;
  }

  public Animal(Genome genome, Vector2d position) {
    this.genome = genome;
    this.position = position;
    this.orientation = MapDirection.NORTH;
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

  public void move(MoveValidator moveValidator) {
    var moveGen = genome.nextGen();
    setOrientation(moveGen.rotate(orientation));
    updatePosition(position.add(orientation.toUnitVector()), moveValidator);
    moveValidator.adjustAnimalAfterMove(this);
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
