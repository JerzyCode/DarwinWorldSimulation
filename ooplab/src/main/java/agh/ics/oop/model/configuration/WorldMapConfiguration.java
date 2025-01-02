package agh.ics.oop.model.configuration;

public class WorldMapConfiguration {
  private int height;
  private int width;
  private WorldMapVariant mapVariant;

  WorldMapConfiguration() {
    this(5, 5, WorldMapVariant.GRASS_FIELD);
  }

  public WorldMapConfiguration(int height, int width, WorldMapVariant mapVariant) {
    this.height = height;
    this.width = width;
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

  public WorldMapVariant getMapVariant() {
    return mapVariant;
  }

  public void setMapVariant(WorldMapVariant mapVariant) {
    this.mapVariant = mapVariant;
  }

  // TODO: to chwilowo żeby działało, ale jakoś musisz grassField przerobić do przyjętej konwencji, że to symulacja zarządza
  // TODO: ALERT TRZEBA COŚ Z TYM WRESZCIE ZROBIĆ, chyba można po prostu usunąć
  public int getStartPlantCount(){
    return 5;
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
