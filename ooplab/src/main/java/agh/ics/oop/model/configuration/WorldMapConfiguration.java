package agh.ics.oop.model.configuration;

public class WorldMapConfiguration {
  private int height;
  private int width;
  private int startPlantCount;
  private WorldMapVariant mapVariant;

  WorldMapConfiguration() {
    this(5, 5, 3, WorldMapVariant.GRASS_FIELD);
  }

  public WorldMapConfiguration(int height, int width, int startPlantCount, WorldMapVariant mapVariant) {
    this.height = height;
    this.width = width;
    this.startPlantCount = startPlantCount;
    this.mapVariant = mapVariant;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getStartPlantCount() {
    return startPlantCount;
  }

  public void setStartPlantCount(int startPlantCount) {
    this.startPlantCount = startPlantCount;
  }

  public WorldMapVariant getMapVariant() {
    return mapVariant;
  }

  public void setMapVariant(WorldMapVariant mapVariant) {
    this.mapVariant = mapVariant;
  }

  @Override
  public String toString() {
    return "WorldMapConfiguration{" +
        "height=" + height +
        ", width=" + width +
        ", startPlantCount=" + startPlantCount +
        ", mapVariant=" + mapVariant +
        '}';
  }
}
