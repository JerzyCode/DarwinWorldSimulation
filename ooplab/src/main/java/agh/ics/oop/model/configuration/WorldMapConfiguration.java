package agh.ics.oop.model.configuration;

public class WorldMapConfiguration {
    private final int height;
    private final int width;
    private final WorldMapVariant mapVariant;

    public WorldMapConfiguration(int height, int width, WorldMapVariant mapVariant) {
        this.height = height;
        this.width = width;
        this.mapVariant = mapVariant;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
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
