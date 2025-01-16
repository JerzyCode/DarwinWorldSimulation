package agh.ics.oop.factory;

import agh.ics.oop.model.SimulationWorldMap;
import agh.ics.oop.model.configuration.SimulationConfiguration;
import agh.ics.oop.model.configuration.WorldMapConfiguration;
import agh.ics.oop.model.map.fire.FireEarth;
import agh.ics.oop.model.map.plant.Earth;

public class WorldMapFactory {
    private final WorldMapConfiguration worldMapConfiguration;
    private final SimulationConfiguration simulationConfiguration;

    public WorldMapFactory(WorldMapConfiguration worldMapConfiguration, SimulationConfiguration simulationConfiguration) {
        this.worldMapConfiguration = worldMapConfiguration;
        this.simulationConfiguration = simulationConfiguration;
    }

    public SimulationWorldMap createWorldMap() {
        return switch (worldMapConfiguration.getMapVariant()) {
            case EARTH -> createEarth();
            case FIRE -> createFireEarth();
        };
    }


    private Earth createEarth() {
        var width = worldMapConfiguration.getWidth();
        var height = worldMapConfiguration.getHeight();
        var plantVariant = worldMapConfiguration.getPlantVariant();
        var plantGrowth = simulationConfiguration.getPlantGrowth();
        var startPlantCount = simulationConfiguration.getStartPlantCount();

        return new Earth(width, height, plantGrowth, startPlantCount, plantVariant);
    }

    private FireEarth createFireEarth() {
        var width = worldMapConfiguration.getWidth();
        var height = worldMapConfiguration.getHeight();
        var fireDuration = worldMapConfiguration.getFireDuration();
        var fireFrequency = worldMapConfiguration.getFireFrequency();
        var plantVariant = worldMapConfiguration.getPlantVariant();
        var plantGrowth = simulationConfiguration.getPlantGrowth();
        var startPlantCount = simulationConfiguration.getStartPlantCount();

        return new FireEarth(width, height, fireFrequency, fireDuration, plantGrowth, startPlantCount, plantVariant);
    }

}
