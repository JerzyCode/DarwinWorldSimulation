package agh.ics.oop.model.exceptions;

import agh.ics.oop.model.Vector2d;

public class PlantIsAlreadyGrownException extends Exception {
  private static final String MESSAGE = "Plant is already grown at position %s";

  public PlantIsAlreadyGrownException(Vector2d vector2d) {
    super(String.format(MESSAGE, vector2d));
  }
}
