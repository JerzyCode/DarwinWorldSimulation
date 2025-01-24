package agh.ics.oop.model.statistics;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.WorldElement;

import java.util.Set;

public interface StatisticsDataProvider {

    Set<Animal> getDeadAnimals();

    Set<Animal> getAliveAnimals();

    Set<WorldElement> getMapElements();

    Boundary getCurrentBoundary();
}
