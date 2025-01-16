package agh.ics.oop.factory;

import agh.ics.oop.model.configuration.WorldMapConfiguration;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.map.fire.FireEarth;
import agh.ics.oop.model.map.plant.Earth;

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
        var plantVariant = worldMapConfiguration.getPlantVariant();

        return new Earth(width, height, plantVariant);
    }

    private FireEarth createFireEarth() {
        var width = worldMapConfiguration.getWidth();
        var height = worldMapConfiguration.getHeight();
        var fireDuration = worldMapConfiguration.getFireDuration();
        var fireFrequency = worldMapConfiguration.getFireFrequency();
        var plantVariant = worldMapConfiguration.getPlantVariant();
        return new FireEarth(width, height, fireFrequency, fireDuration, plantVariant);
    }

}
