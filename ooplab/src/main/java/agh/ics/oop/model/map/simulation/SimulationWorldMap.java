package agh.ics.oop.model.map.simulation;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.map.WorldMap;

import java.util.Set;

public interface SimulationWorldMap extends WorldMap {
    void handleDayEnds(int currentDay);

    Set<Animal> getAnimals();

    Set<Animal> getDeadAnimals();
}
