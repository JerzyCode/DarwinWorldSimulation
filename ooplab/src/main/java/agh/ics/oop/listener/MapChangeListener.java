package agh.ics.oop.listener;

import agh.ics.oop.model.event.MapChangedEvent;
import agh.ics.oop.model.map.WorldMap;

public interface MapChangeListener {
    void mapChanged(WorldMap worldMap, MapChangedEvent mapChangedEvent);
}
