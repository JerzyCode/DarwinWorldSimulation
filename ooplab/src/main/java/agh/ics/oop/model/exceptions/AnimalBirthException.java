package agh.ics.oop.model.exceptions;

public class AnimalBirthException extends Exception {
    private static final String DEFAULT_MESSAGE = "Animal Could Not Be Born";
    public AnimalBirthException(String message) {
        super(message);
    }
}
