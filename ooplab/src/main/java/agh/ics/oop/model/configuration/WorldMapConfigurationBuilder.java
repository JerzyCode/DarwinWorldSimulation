package agh.ics.oop.model.configuration;

public class WorldMapConfigurationBuilder {
    private int height;
    private int width;
    private int energyGain;
    private int plantGrowth;
    private int startPlantCount;
    private int fireFrequency;
    private int fireDuration;
    private WorldMapVariant mapVariant;
    private PlantVariant plantVariant;

    private WorldMapConfigurationBuilder() {

    }

    public static WorldMapConfigurationBuilder create() {
        return new WorldMapConfigurationBuilder();
    }

    public WorldMapConfigurationBuilder height(int height) {
        this.height = height;
        return this;
    }

    public WorldMapConfigurationBuilder width(int width) {
        this.width = width;
        return this;
    }

    public WorldMapConfigurationBuilder energyGain(int energyGain) {
        this.energyGain = energyGain;
        return this;
    }

    public WorldMapConfigurationBuilder plantGrowth(int plantGrowth) {
        this.plantGrowth = plantGrowth;
        return this;
    }

    public WorldMapConfigurationBuilder startPlantCount(int startPlantCount) {
        this.startPlantCount = startPlantCount;
        return this;
    }

    public WorldMapConfigurationBuilder mapVariant(WorldMapVariant mapVariant) {
        this.mapVariant = mapVariant;
        return this;
    }

    public WorldMapConfigurationBuilder fireFrequency(int fireFrequency) {
        this.fireFrequency = fireFrequency;
        return this;
    }

    public WorldMapConfigurationBuilder fireDuration(int fireDuration) {
        this.fireDuration = fireDuration;
        return this;
    }

    public WorldMapConfigurationBuilder plantVariant(PlantVariant plantVariant) {
        this.plantVariant = plantVariant;
        return this;
    }

    public WorldMapConfiguration build() {
        return new WorldMapConfiguration(height, width, energyGain, plantGrowth, startPlantCount, fireFrequency, fireDuration, mapVariant, plantVariant);
    }
}
