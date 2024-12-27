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

  public WorldMapConfiguration() {

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
}
