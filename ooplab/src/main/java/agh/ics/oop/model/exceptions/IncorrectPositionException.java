package agh.ics.oop.model.exceptions;

import agh.ics.oop.model.Vector2d;

public class IncorrectPositionException extends Exception {
  private static final String MESSAGE = "%s is not correct";

  public IncorrectPositionException(Vector2d position) {
    super(String.format(MESSAGE, position));
  }
}
