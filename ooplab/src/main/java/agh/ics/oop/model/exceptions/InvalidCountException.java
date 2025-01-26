package agh.ics.oop.model.exceptions;

public class InvalidCountException extends Exception {
    public InvalidCountException() {
        super("Gens count should be greater or equal zero.");
    }
}
