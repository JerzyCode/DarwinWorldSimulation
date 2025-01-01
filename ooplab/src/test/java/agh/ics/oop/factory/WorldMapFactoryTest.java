package agh.ics.oop.factory;

import agh.ics.oop.model.configuration.WorldMapConfiguration;
import agh.ics.oop.model.configuration.WorldMapVariant;
import agh.ics.oop.model.map.Earth;
import agh.ics.oop.model.map.FireEarth;
import agh.ics.oop.model.map.GrassField;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class WorldMapFactoryTest {

  @Test
  void should_create_grass_field() {
    //given
    var configuration = new WorldMapConfiguration(
        100,
        100,
        WorldMapVariant.GRASS_FIELD);
    var factory = new WorldMapFactory(configuration);

    //when
    var map = factory.createWorldMap();

    //then

    assertInstanceOf(GrassField.class, map);
  }

  @Test
  void should_create_earth() {
    //given
    var configuration = new WorldMapConfiguration(
        100,
        100,
        WorldMapVariant.EARTH);
    var factory = new WorldMapFactory(configuration);

    //when
    var map = factory.createWorldMap();

    //then

    assertInstanceOf(Earth.class, map);
    //TODO more assertions after Earth implementation
  }

  @Test
  void should_create_fire_earth() {
    //given
    var configuration = new WorldMapConfiguration(
        100,
        100,
        WorldMapVariant.FIRE);
    var factory = new WorldMapFactory(configuration);

    //when
    var map = factory.createWorldMap();

    //then

    assertInstanceOf(FireEarth.class, map);
    //TODO more assertions after FireEarth implementation
  }

}