package agh.ics.oop.model.statistics;

import agh.ics.oop.model.elements.Animal;

import java.util.Set;

public interface AnimalDataProvider {

    Set<Animal> getDeadAnimals();
    Set<Animal> getAliveAnimals();
}
