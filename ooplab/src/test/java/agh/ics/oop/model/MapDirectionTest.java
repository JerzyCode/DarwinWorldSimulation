package agh.ics.oop.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class MapDirectionTest {

  @ParameterizedTest
  @EnumSource(MapDirection.class)
  void shouldReturnNextMapDirection(MapDirection direction) {
    //given
    //when
    var result = direction.next();

    //then
    var expected = calculateExpectedNext(direction);
    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @EnumSource(MapDirection.class)
  void shouldReturnPreviousMapDirection(MapDirection direction) {
    //given
    //when
    var result = direction.previous();

    //then
    var expected = calculateExpectedPrevious(direction);
    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @EnumSource(MapDirection.class)
  void shouldReturnUnitVector(MapDirection direction) {
    //given
    //when
    var result = direction.toUnitVector();

    //then
    var expected = calculateUnitVector(direction);
    Assertions.assertEquals(expected, result);
  }

  private static MapDirection calculateExpectedNext(MapDirection direction) {
    return switch (direction) {
      case MapDirection.NORTH -> MapDirection.EAST;
      case MapDirection.SOUTH -> MapDirection.WEST;
      case MapDirection.WEST -> MapDirection.NORTH;
      case MapDirection.EAST -> MapDirection.SOUTH;
    };
  }

  private static MapDirection calculateExpectedPrevious(MapDirection direction) {
    return switch (direction) {
      case MapDirection.NORTH -> MapDirection.WEST;
      case MapDirection.SOUTH -> MapDirection.EAST;
      case MapDirection.WEST -> MapDirection.SOUTH;
      case MapDirection.EAST -> MapDirection.NORTH;
    };
  }

  private static Vector2d calculateUnitVector(MapDirection direction) {
    return switch (direction) {
      case MapDirection.NORTH -> new Vector2d(0, 1);
      case MapDirection.SOUTH -> new Vector2d(0, -1);
      case MapDirection.WEST -> new Vector2d(-1, 0);
      case MapDirection.EAST -> new Vector2d(1, 0);
    };
  }
}