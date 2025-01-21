package agh.ics.oop.model.exceptions;

public class InvalidAngleException extends RuntimeException {
    private static final String MESSAGE = "Angle should be multiplicity of 45";

    public InvalidAngleException() {
        super(MESSAGE);
    }
}
