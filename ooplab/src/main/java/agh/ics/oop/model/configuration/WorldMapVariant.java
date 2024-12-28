package agh.ics.oop.model.configuration;

public enum WorldMapVariant {
  EARTH("Earth"),
  FIRE("Fire Earth"),
  GRASS_FIELD("Grass Field");

  private final String displayText;

  WorldMapVariant(String displayText) {
    this.displayText = displayText;
  }

  public String getDisplayText() {
    return displayText;
  }
}
