package agh.ics.oop.factory;

import agh.ics.oop.model.configuration.WorldMapConfiguration;
import agh.ics.oop.model.map.Earth;
import agh.ics.oop.model.map.fire.FireEarth;
import agh.ics.oop.model.map.WorldMap;

public class WorldMapFactory {
    private final WorldMapConfiguration worldMapConfiguration;

    public WorldMapFactory(WorldMapConfiguration worldMapConfiguration) {
        this.worldMapConfiguration = worldMapConfiguration;
    }

    public WorldMap createWorldMap() {
        return switch (worldMapConfiguration.getMapVariant()) {
            case EARTH -> createEarth();
            case FIRE -> createFireEarth();
        };
    }


    private Earth createEarth() {
        var width = worldMapConfiguration.getWidth();
        var height = worldMapConfiguration.getHeight();
        return new Earth(width, height);
    }

    private FireEarth createFireEarth() {
        var width = worldMapConfiguration.getWidth();
        var height = worldMapConfiguration.getHeight();
        var fireDuration = worldMapConfiguration.getFireDuration();
        var fireFrequency = worldMapConfiguration.getFireFrequency();
        return new FireEarth(width, height, fireFrequency, fireDuration);
    }

}
