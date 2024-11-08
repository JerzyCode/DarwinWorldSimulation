package agh.ics.oop.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class MapDirectionTest {

  @ParameterizedTest
  @MethodSource("provideNextMapDirectionTestCases")
  void shouldReturnNextMapDirection(MapDirection direction, MapDirection expected) {
    //given
    //when
    var result = direction.next();

    //then
    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @MethodSource("providePreviousMapDirectionTestCases")
  void shouldReturnPreviousMapDirection(MapDirection direction, MapDirection expected) {
    //given
    //when
    var result = direction.previous();

    //then
    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @MethodSource("provideToUnitVectorTestCases")
  void shouldReturnUnitVector(MapDirection direction, Vector2d expected) {
    //given
    //when
    var result = direction.toUnitVector();

    //then
    Assertions.assertEquals(expected, result);
  }

  private static Stream<Arguments> provideNextMapDirectionTestCases() {
    return Stream.of(
        Arguments.of(MapDirection.NORTH, MapDirection.EAST),
        Arguments.of(MapDirection.SOUTH, MapDirection.WEST),
        Arguments.of(MapDirection.WEST, MapDirection.NORTH),
        Arguments.of(MapDirection.EAST, MapDirection.SOUTH)
    );
  }

  private static Stream<Arguments> providePreviousMapDirectionTestCases() {
    return Stream.of(
        Arguments.of(MapDirection.NORTH, MapDirection.WEST),
        Arguments.of(MapDirection.SOUTH, MapDirection.EAST),
        Arguments.of(MapDirection.WEST, MapDirection.SOUTH),
        Arguments.of(MapDirection.EAST, MapDirection.NORTH)
    );
  }

  private static Stream<Arguments> provideToUnitVectorTestCases() {
    return Stream.of(
        Arguments.of(MapDirection.NORTH, new Vector2d(0, 1)),
        Arguments.of(MapDirection.SOUTH, new Vector2d(0, -1)),
        Arguments.of(MapDirection.WEST, new Vector2d(-1, 0)),
        Arguments.of(MapDirection.EAST, new Vector2d(1, 0))
    );
  }

}