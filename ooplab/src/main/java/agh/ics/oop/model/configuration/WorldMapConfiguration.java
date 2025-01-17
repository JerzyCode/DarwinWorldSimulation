package agh.ics.oop.model.configuration;

public class WorldMapConfiguration {
    private final int height;
    private final int width;
    private final int fireFrequency;
    private final int fireDuration;
    private final int energyGain;
    private final int plantGrowth;
    private final int startPlantCount;
    private final WorldMapVariant mapVariant;
    private final PlantVariant plantVariant;

    public WorldMapConfiguration(int height, int width, int energyGain, int plantGrowth, int startPlantCount, int fireFrequency, int fireDuration, WorldMapVariant mapVariant, PlantVariant plantVariant) {
        this.height = height;
        this.width = width;
        this.energyGain = energyGain;
        this.plantGrowth = plantGrowth;
        this.startPlantCount = startPlantCount;
        this.fireFrequency = fireFrequency;
        this.fireDuration = fireDuration;
        this.mapVariant = mapVariant;
        this.plantVariant = plantVariant;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getFireDuration() {
        return fireDuration;
    }

    public int getFireFrequency() {
        return fireFrequency;
    }

    public WorldMapVariant getMapVariant() {
        return mapVariant;
    }

    public PlantVariant getPlantVariant() {
        return plantVariant;
    }

    public int getEnergyGain() {
        return energyGain;
    }

    public int getPlantGrowth() {
        return plantGrowth;
    }

    public int getStartPlantCount() {
        return startPlantCount;
    }

    @Override
    public String toString() {
        return "WorldMapConfiguration{" +
                "height=" + height +
                ", width=" + width +
                ", mapVariant=" + mapVariant +
                '}';
    }
}
