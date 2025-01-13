package agh.ics.oop.model.configuration;

public class WorldMapConfiguration {
    private final int height;
    private final int width;
    private final int fireFrequency;
    private final int fireDuration;
    private final WorldMapVariant mapVariant;

    public WorldMapConfiguration(int height, int width, int fireFrequency, int fireDuration, WorldMapVariant mapVariant) {
        this.height = height;
        this.width = width;
        this.fireFrequency = fireFrequency;
        this.fireDuration = fireDuration;
        this.mapVariant = mapVariant;
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


    @Override
    public String toString() {
        return "WorldMapConfiguration{" +
                "height=" + height +
                ", width=" + width +
                ", mapVariant=" + mapVariant +
                '}';
    }
}
