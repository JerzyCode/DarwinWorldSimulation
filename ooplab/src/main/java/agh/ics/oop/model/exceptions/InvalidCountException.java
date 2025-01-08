package agh.ics.oop.model.exceptions;

public class InvalidCountException extends Exception { //TODO dopytać czy  strumieniach można mieć runtime exception
    public InvalidCountException() {
        super("Gens count should be greater or equal zero.");
    }
}
