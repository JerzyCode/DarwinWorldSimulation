package agh.ics.oop.factory;

import agh.ics.oop.model.configuration.WorldMapConfiguration;
import agh.ics.oop.model.map.Earth;
import agh.ics.oop.model.map.FireEarth;
import agh.ics.oop.model.map.GrassField;
import agh.ics.oop.model.map.WorldMap;

public class WorldMapFactory {
  private final WorldMapConfiguration worldMapConfiguration;

  public WorldMapFactory(WorldMapConfiguration worldMapConfiguration) {
    this.worldMapConfiguration = worldMapConfiguration;
  }

  public WorldMap createWorldMap() {
    return switch (worldMapConfiguration.getMapVariant()) {
      case EARTH -> createEarth();
      case FIRE -> createFireEarth();
      case GRASS_FIELD -> createGrassField();
    };
  }

  private GrassField createGrassField() {
    return new GrassField(0);
  }

  private Earth createEarth() {
    return new Earth(); //TODO
  }

  private FireEarth createFireEarth() {
    return new FireEarth(); //TODO
  }

}
