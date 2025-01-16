package agh.ics.oop.model.exceptions;

import agh.ics.oop.model.elements.Animal;

public class AnimalStillAliveException extends RuntimeException {
    public AnimalStillAliveException(Animal animal) {
        super("Animal " + animal.hashCode() + " is still alive!");
    }
}
