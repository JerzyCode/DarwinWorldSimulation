package agh.ics.oop.model.exceptions;

public class WrongAngleException extends RuntimeException {
  private static final String MESSAGE = "Angle should be multiplicity of 45";

  public WrongAngleException() {
    super(MESSAGE);
  }
}
