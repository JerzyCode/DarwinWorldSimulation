package agh.ics.oop.model.configuration;

public class WorldMapConfiguration {
  private int height;
  private int width;
  private WorldMapVariant mapVariant;

  public WorldMapConfiguration(int height, int width, WorldMapVariant mapVariant) {
    this.height = height;
    this.width = width;
    this.mapVariant = mapVariant;
  }

  private WorldMapConfiguration() {

  }

  static WorldMapConfiguration getDefault() {
    var defaultConfig = new WorldMapConfiguration();
    defaultConfig.height = 5;
    defaultConfig.width = 5;
    defaultConfig.mapVariant = WorldMapVariant.GRASS_FIELD;
    return defaultConfig;
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
        ", mapVariant=" + mapVariant +
        '}';
  }
}
