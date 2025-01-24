package agh.ics.oop.model.statistics;

import agh.ics.oop.model.elements.Animal;

import java.util.Set;

public interface StatisticsDataProvider {

    Set<Animal> getDeadAnimals();

    Set<Animal> getAliveAnimals();

    int getCurrentDay();
}
