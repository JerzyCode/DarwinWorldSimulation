package agh.ics.oop.model.map.simulation;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.map.WorldMap;

import java.util.Set;

public interface SimulationWorldMap extends WorldMap { // czemu to jest dodatkowy interfejs?
    void handleDayEnds(int currentDay);

    void clearDeadAnimals();

    Set<Animal> getAnimals();

    void sendDayHasEndedNotification(int currentDay);
}
