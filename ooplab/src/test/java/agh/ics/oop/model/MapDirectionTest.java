package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.InvalidAngleException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class MapDirectionTest {

    @Test
    void rotateShouldThrowWrongAngleException() {
        Assertions.assertThrows(InvalidAngleException.class, () -> MapDirection.WEST.rotate(10));
        Assertions.assertThrows(InvalidAngleException.class, () -> MapDirection.EAST.rotate(4));
        Assertions.assertThrows(InvalidAngleException.class, () -> MapDirection.NORTH.rotate(34));
        Assertions.assertThrows(InvalidAngleException.class, () -> MapDirection.NORTH_WEST.rotate(531));
        Assertions.assertThrows(InvalidAngleException.class, () -> MapDirection.SOUTH_WEST.rotate(10001));
    }

    @Test
    void shouldRotateMapDirectionNorth() {
        var start = MapDirection.NORTH;
        Assertions.assertEquals(MapDirection.NORTH, start.rotate(0));
        Assertions.assertEquals(MapDirection.NORTH_EAST, start.rotate(45));
        Assertions.assertEquals(MapDirection.EAST, start.rotate(90));
        Assertions.assertEquals(MapDirection.SOUTH_EAST, start.rotate(135));
        Assertions.assertEquals(MapDirection.SOUTH, start.rotate(180));
        Assertions.assertEquals(MapDirection.SOUTH_WEST, start.rotate(225));
        Assertions.assertEquals(MapDirection.WEST, start.rotate(270));
        Assertions.assertEquals(MapDirection.NORTH_WEST, start.rotate(315));
        Assertions.assertEquals(MapDirection.NORTH, start.rotate(360));
    }

    @Test
    void shouldRotateMapDirectionNorthEast() {
        var start = MapDirection.NORTH_EAST;
        Assertions.assertEquals(MapDirection.NORTH_EAST, start.rotate(0));
        Assertions.assertEquals(MapDirection.EAST, start.rotate(45));
        Assertions.assertEquals(MapDirection.SOUTH_EAST, start.rotate(90));
        Assertions.assertEquals(MapDirection.SOUTH, start.rotate(135));
        Assertions.assertEquals(MapDirection.SOUTH_WEST, start.rotate(180));
        Assertions.assertEquals(MapDirection.WEST, start.rotate(225));
        Assertions.assertEquals(MapDirection.NORTH_WEST, start.rotate(270));
        Assertions.assertEquals(MapDirection.NORTH, start.rotate(315));
        Assertions.assertEquals(MapDirection.NORTH_EAST, start.rotate(360));
    }

    @Test
    void shouldRotateMapDirectionEast() {
        var start = MapDirection.EAST;
        Assertions.assertEquals(MapDirection.EAST, start.rotate(0));
        Assertions.assertEquals(MapDirection.SOUTH_EAST, start.rotate(45));
        Assertions.assertEquals(MapDirection.SOUTH, start.rotate(90));
        Assertions.assertEquals(MapDirection.SOUTH_WEST, start.rotate(135));
        Assertions.assertEquals(MapDirection.WEST, start.rotate(180));
        Assertions.assertEquals(MapDirection.NORTH_WEST, start.rotate(225));
        Assertions.assertEquals(MapDirection.NORTH, start.rotate(270));
        Assertions.assertEquals(MapDirection.NORTH_EAST, start.rotate(315));
        Assertions.assertEquals(MapDirection.EAST, start.rotate(360));
    }

    @Test
    void shouldRotateMapDirectionSouthEast() {
        var start = MapDirection.SOUTH_EAST;
        Assertions.assertEquals(MapDirection.SOUTH_EAST, start.rotate(0));
        Assertions.assertEquals(MapDirection.SOUTH, start.rotate(45));
        Assertions.assertEquals(MapDirection.SOUTH_WEST, start.rotate(90));
        Assertions.assertEquals(MapDirection.WEST, start.rotate(135));
        Assertions.assertEquals(MapDirection.NORTH_WEST, start.rotate(180));
        Assertions.assertEquals(MapDirection.NORTH, start.rotate(225));
        Assertions.assertEquals(MapDirection.NORTH_EAST, start.rotate(270));
        Assertions.assertEquals(MapDirection.EAST, start.rotate(315));
        Assertions.assertEquals(MapDirection.SOUTH_EAST, start.rotate(360));
    }

    @Test
    void shouldRotateMapDirectionSouth() {
        var start = MapDirection.SOUTH;
        Assertions.assertEquals(MapDirection.SOUTH, start.rotate(0));
        Assertions.assertEquals(MapDirection.SOUTH_WEST, start.rotate(45));
        Assertions.assertEquals(MapDirection.WEST, start.rotate(90));
        Assertions.assertEquals(MapDirection.NORTH_WEST, start.rotate(135));
        Assertions.assertEquals(MapDirection.NORTH, start.rotate(180));
        Assertions.assertEquals(MapDirection.NORTH_EAST, start.rotate(225));
        Assertions.assertEquals(MapDirection.EAST, start.rotate(270));
        Assertions.assertEquals(MapDirection.SOUTH_EAST, start.rotate(315));
        Assertions.assertEquals(MapDirection.SOUTH, start.rotate(360));
    }

    @Test
    void shouldRotateMapDirectionSouthWest() {
        var start = MapDirection.SOUTH_WEST;
        Assertions.assertEquals(MapDirection.SOUTH_WEST, start.rotate(0));
        Assertions.assertEquals(MapDirection.WEST, start.rotate(45));
        Assertions.assertEquals(MapDirection.NORTH_WEST, start.rotate(90));
        Assertions.assertEquals(MapDirection.NORTH, start.rotate(135));
        Assertions.assertEquals(MapDirection.NORTH_EAST, start.rotate(180));
        Assertions.assertEquals(MapDirection.EAST, start.rotate(225));
        Assertions.assertEquals(MapDirection.SOUTH_EAST, start.rotate(270));
        Assertions.assertEquals(MapDirection.SOUTH, start.rotate(315));
        Assertions.assertEquals(MapDirection.SOUTH_WEST, start.rotate(360));
    }

    @Test
    void shouldRotateMapDirectionWest() {
        var start = MapDirection.WEST;
        Assertions.assertEquals(MapDirection.WEST, start.rotate(0));
        Assertions.assertEquals(MapDirection.NORTH_WEST, start.rotate(45));
        Assertions.assertEquals(MapDirection.NORTH, start.rotate(90));
        Assertions.assertEquals(MapDirection.NORTH_EAST, start.rotate(135));
        Assertions.assertEquals(MapDirection.EAST, start.rotate(180));
        Assertions.assertEquals(MapDirection.SOUTH_EAST, start.rotate(225));
        Assertions.assertEquals(MapDirection.SOUTH, start.rotate(270));
        Assertions.assertEquals(MapDirection.SOUTH_WEST, start.rotate(315));
        Assertions.assertEquals(MapDirection.WEST, start.rotate(360));
    }

    @Test
    void shouldRotateMapDirectionNorthWest() {
        var start = MapDirection.NORTH_WEST;
        Assertions.assertEquals(MapDirection.NORTH_WEST, start.rotate(0));
        Assertions.assertEquals(MapDirection.NORTH, start.rotate(45));
        Assertions.assertEquals(MapDirection.NORTH_EAST, start.rotate(90));
        Assertions.assertEquals(MapDirection.EAST, start.rotate(135));
        Assertions.assertEquals(MapDirection.SOUTH_EAST, start.rotate(180));
        Assertions.assertEquals(MapDirection.SOUTH, start.rotate(225));
        Assertions.assertEquals(MapDirection.SOUTH_WEST, start.rotate(270));
        Assertions.assertEquals(MapDirection.WEST, start.rotate(315));
        Assertions.assertEquals(MapDirection.NORTH_WEST, start.rotate(360));
    }

    @ParameterizedTest
    @MethodSource("provideNextRightAngleMapDirectionTestCases")
    void shouldReturnNextRightAngleMapDirectionParametrized(MapDirection direction, MapDirection expected) {
        //given
        //when
        var result = direction.rotateRightAngleClockwise();

        //then
        Assertions.assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("providePreviousRightAngleMapDirectionTestCases")
    void shouldReturnPreviousRightAngleMapDirection(MapDirection direction, MapDirection expected) {
        //given
        //when
        var result = direction.rotateRightAngleCounterClockwise();

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

    private static Stream<Arguments> provideNextRightAngleMapDirectionTestCases() {
        return Stream.of(
                Arguments.of(MapDirection.NORTH, MapDirection.EAST),
                Arguments.of(MapDirection.SOUTH, MapDirection.WEST),
                Arguments.of(MapDirection.WEST, MapDirection.NORTH),
                Arguments.of(MapDirection.EAST, MapDirection.SOUTH)
        );
    }

    private static Stream<Arguments> providePreviousRightAngleMapDirectionTestCases() {
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
                Arguments.of(MapDirection.NORTH_EAST, new Vector2d(1, 1)),
                Arguments.of(MapDirection.EAST, new Vector2d(1, 0)),
                Arguments.of(MapDirection.SOUTH_EAST, new Vector2d(1, -1)),
                Arguments.of(MapDirection.SOUTH, new Vector2d(0, -1)),
                Arguments.of(MapDirection.SOUTH_WEST, new Vector2d(-1, -1)),
                Arguments.of(MapDirection.WEST, new Vector2d(-1, 0)),
                Arguments.of(MapDirection.NORTH_WEST, new Vector2d(-1, 1))
        );
    }

}