package agh.ics.oop.model;

import agh.ics.oop.listener.MapChangeListener;
import agh.ics.oop.model.event.MapChangedEvent;
import agh.ics.oop.model.map.WorldMap;

public class ConsoleMapDisplay implements MapChangeListener {
    private int eventsCount;

    public ConsoleMapDisplay() {
        this.eventsCount = 0;
    }

    @Override
    public void mapChanged(WorldMap worldMap, MapChangedEvent event) {
        synchronized (System.out) {
            eventsCount += 1;
            System.out.println(event.getMessage());
            System.out.println("Map ID: " + worldMap.getId());
            System.out.print(worldMap);
            System.out.println("Events count: " + eventsCount);
        }
    }
}
