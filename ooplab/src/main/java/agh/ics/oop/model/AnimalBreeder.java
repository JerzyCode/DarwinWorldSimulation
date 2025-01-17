package agh.ics.oop.model;

import agh.ics.oop.model.elements.Animal;

@FunctionalInterface
public interface AnimalBreeder {
    Animal breed(Animal parent1, Animal parent2);
}
