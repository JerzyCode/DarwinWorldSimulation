package agh.ics.oop.model.exceptions;

public class InvalidCountException extends Exception {
    public InvalidCountException() {
        super("Count should be greater or equal zero.");
    }
}
