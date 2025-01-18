package agh.ics.oop.factory;

import agh.ics.oop.model.AnimalBreeder;
import agh.ics.oop.model.configuration.WorldMapConfiguration;
import agh.ics.oop.model.map.fire.FireEarth;
import agh.ics.oop.model.map.plant.Earth;
import agh.ics.oop.model.map.simulation.SimulationWorldMap;

public class WorldMapFactory {
    private final WorldMapConfiguration worldMapConfiguration;
    private final AnimalBreeder breeder;

    public WorldMapFactory(WorldMapConfiguration worldMapConfiguration, AnimalBreeder breeder) {
        this.worldMapConfiguration = worldMapConfiguration;
        this.breeder = breeder;
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
        var plantGrowth = worldMapConfiguration.getPlantGrowth();
        var startPlantCount = worldMapConfiguration.getStartPlantCount();
        var energyGain = worldMapConfiguration.getEnergyGain();

        return new Earth(width, height, plantGrowth, startPlantCount, energyGain, plantVariant, breeder);
    }

    private FireEarth createFireEarth() {
        var width = worldMapConfiguration.getWidth();
        var height = worldMapConfiguration.getHeight();
        var fireDuration = worldMapConfiguration.getFireDuration();
        var fireFrequency = worldMapConfiguration.getFireFrequency();
        var plantVariant = worldMapConfiguration.getPlantVariant();
        var plantGrowth = worldMapConfiguration.getPlantGrowth();
        var startPlantCount = worldMapConfiguration.getStartPlantCount();
        var energyGain = worldMapConfiguration.getEnergyGain();


        return new FireEarth(width, height, fireFrequency, fireDuration, plantGrowth, startPlantCount, energyGain, plantVariant, breeder);
    }

}
