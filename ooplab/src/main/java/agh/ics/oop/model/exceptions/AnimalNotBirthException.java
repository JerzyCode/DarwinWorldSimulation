package agh.ics.oop.model.exceptions;

public class AnimalNotBirthException extends RuntimeException { //NotBorn
    public AnimalNotBirthException(String message) {
        super(message);
    }
}
