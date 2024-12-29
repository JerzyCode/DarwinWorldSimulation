package agh.ics.oop.factory;

import agh.ics.oop.model.configuration.WorldMapConfiguration;
import agh.ics.oop.model.map.AbstractWorldMap;
import agh.ics.oop.model.map.Earth;
import agh.ics.oop.model.map.FireEarth;
import agh.ics.oop.model.map.GrassField;

public class WorldMapFactory {
  private final WorldMapConfiguration worldMapConfiguration;

  public WorldMapFactory(WorldMapConfiguration worldMapConfiguration) {
    this.worldMapConfiguration = worldMapConfiguration;
  }

  public AbstractWorldMap createWorldMap() {
    return switch (worldMapConfiguration.getMapVariant()) {
      case EARTH -> createEarth();
      case FIRE -> createFireEarth();
      case GRASS_FIELD -> createGrassField();
    };
  }

  private GrassField createGrassField() {
    return new GrassField(worldMapConfiguration.getStartPlantCount());
  }

  private Earth createEarth() {
    var width = worldMapConfiguration.getWidth();
    var height = worldMapConfiguration.getHeight();
    return new Earth(width, height);
  }

  private FireEarth createFireEarth() {
    return new FireEarth(); //TODO
  }

}
