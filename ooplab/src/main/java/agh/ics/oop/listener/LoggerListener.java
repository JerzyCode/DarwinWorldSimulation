package agh.ics.oop.listener;

import agh.ics.oop.model.event.MapChangedEvent;
import agh.ics.oop.model.map.WorldMap;

public class LoggerListener implements MapChangeListener {
    @Override
    public void mapChanged(WorldMap worldMap, MapChangedEvent mapChangedEvent) {
        System.out.println(mapChangedEvent.getMessage());
    }
}
