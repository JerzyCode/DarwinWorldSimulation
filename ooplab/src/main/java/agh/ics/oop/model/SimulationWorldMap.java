package agh.ics.oop.model;

import agh.ics.oop.model.map.WorldMap;

public interface SimulationWorldMap extends WorldMap {
    void handleDayEnds(int currentDay);
}
