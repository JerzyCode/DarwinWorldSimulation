package agh.ics.oop.factory;

import agh.ics.oop.model.configuration.WorldMapConfiguration;
import agh.ics.oop.model.configuration.WorldMapVariant;
import agh.ics.oop.model.map.Earth;
import agh.ics.oop.model.map.FireEarth;
import agh.ics.oop.model.map.GrassField;
import agh.ics.oop.model.map.elements.Animal;
import agh.ics.oop.model.map.elements.Grass;
import agh.ics.oop.model.map.elements.WorldElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class WorldMapFactoryTest {

  @ParameterizedTest
  @MethodSource("provideConfigurationArgs")
  void should_create_grass_field(int height, int width, int startPlantCount, int startAnimalCount) {
    //given
    var configuration = new WorldMapConfiguration(
        height,
        width,
        WorldMapVariant.GRASS_FIELD,
        startPlantCount,
        startAnimalCount);
    var factory = new WorldMapFactory(configuration);

    //when
    var map = factory.createWorldMap();

    //then
    var actualAnimalsCount = countElements(map.getElements().stream().toList(), Animal.class);
    var actualPlantsCount = countElements(map.getElements().stream().toList(), Grass.class);

    assertInstanceOf(GrassField.class, map);
    assertEquals(configuration.startAnimalCount(), actualAnimalsCount);
    assertEquals(configuration.startPlantCount(), actualPlantsCount);
  }

  @Test
  void should_create_earth() {
    //given
    var configuration = new WorldMapConfiguration(
        100,
        100,
        WorldMapVariant.EARTH,
        50,
        24);
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
        WorldMapVariant.FIRE,
        50,
        21);
    var factory = new WorldMapFactory(configuration);

    //when
    var map = factory.createWorldMap();

    //then

    assertInstanceOf(FireEarth.class, map);
    //TODO more assertions after FireEarth implementation
  }

  private int countElements(List<WorldElement> elements, Class<? extends WorldElement> clazz) {
    int result = 0;
    for (var element : elements) {
      if (element.getClass() == clazz) {
        result++;
      }
    }
    return result;
  }

  private static Stream<Arguments> provideConfigurationArgs() {
    return Stream.of(
        Arguments.of(0, 0, 0, 0),
        Arguments.of(10, 10, 5, 2),
        Arguments.of(5, 10, 15, 4),
        Arguments.of(25, 25, 25, 25),
        Arguments.of(50, 2, 15, 4)
    );
  }
}