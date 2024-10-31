package agh.ics.oop.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

class Vector2dTest {

  private static final Random RANDOM = new Random();
  private static final int MAX_RANDOM = 100000;

  @Test
  void shouldAddVectors() {
    //given
    var vector1 = new Vector2d(RANDOM.nextInt(MAX_RANDOM), RANDOM.nextInt(MAX_RANDOM));
    var vector2 = new Vector2d(RANDOM.nextInt(MAX_RANDOM), RANDOM.nextInt(MAX_RANDOM));

    //when
    var result = vector1.add(vector2);

    //then
    Assertions.assertEquals(vector1.getX() + vector2.getX(), result.getX());
    Assertions.assertEquals(vector1.getY() + vector2.getY(), result.getY());
  }

  @Test
  void shouldSubtractVectors() {
    //given
    var vector1 = new Vector2d(RANDOM.nextInt(MAX_RANDOM), RANDOM.nextInt(MAX_RANDOM));
    var vector2 = new Vector2d(RANDOM.nextInt(MAX_RANDOM), RANDOM.nextInt(MAX_RANDOM));

    //when
    var result = vector1.subtract(vector2);

    //then
    Assertions.assertEquals(vector1.getX() - vector2.getX(), result.getX());
    Assertions.assertEquals(vector1.getY() - vector2.getY(), result.getY());
  }

  @Test
  void shouldReturnVectorWithOppositeCoordinates() {
    //given
    var vector = new Vector2d(RANDOM.nextInt(MAX_RANDOM), RANDOM.nextInt(MAX_RANDOM));

    //when
    var result = vector.opposite();

    //then
    Assertions.assertEquals(-vector.getX(), result.getX());
    Assertions.assertEquals(-vector.getY(), result.getY());
  }

  @Test
  void shouldReturnToString() {
    //given
    var vector = new Vector2d(RANDOM.nextInt(MAX_RANDOM), RANDOM.nextInt(MAX_RANDOM));

    //when
    var result = vector.toString();

    //then
    var expected = String.format("(%d, %d)", vector.getX(), vector.getY());
    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @MethodSource("provideUpperRightTestCases")
  void shouldReturnUpperRightVector(Vector2d vector1, Vector2d vector2, Vector2d expected) {
    //given
    //when
    var result = vector1.upperRight(vector2);

    //then
    Assertions.assertEquals(expected.getX(), result.getX());
    Assertions.assertEquals(expected.getY(), result.getY());
  }

  @ParameterizedTest
  @MethodSource("provideLowerLeftTestCases")
  void shouldReturnLowerLeftVector(Vector2d vector1, Vector2d vector2, Vector2d expected) {
    //given
    //when
    var result = vector1.lowerLeft(vector2);

    //then
    Assertions.assertEquals(expected.getX(), result.getX());
    Assertions.assertEquals(expected.getY(), result.getY());
  }

  @ParameterizedTest
  @MethodSource("provideEqualTestCases")
  void shouldVerifyIfVectorsAreEqual(Vector2d vector1, Object object, boolean expected) {
    //given
    //when
    var result = vector1.equals(object);

    //then
    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @MethodSource("providePrecedesTestCases")
  void shouldVerifyIfVectorPrecedesOther(Vector2d vector1, Vector2d vector2, boolean expected) {
    //given
    //when
    var result = vector1.precedes(vector2);

    //then
    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @MethodSource("provideFollowsTestCases")
  void shouldVerifyIfVectorFollowsOther(Vector2d vector1, Vector2d vector2, boolean expected) {
    //given
    //when
    var result = vector1.follows(vector2);

    //then
    Assertions.assertEquals(expected, result);
  }

  private static Stream<Arguments> provideEqualTestCases() {
    Vector2d vector = new Vector2d(1, 2);
    return Stream.of(
        Arguments.of(vector, vector, true),
        Arguments.of(new Vector2d(1, 1), new Vector2d(1, 1), true),
        Arguments.of(new Vector2d(0, 0), new Vector2d(0, 0), true),
        Arguments.of(new Vector2d(40, 40), new Vector2d(40, 40), true),
        Arguments.of(new Vector2d(0, 1), new Vector2d(0, 0), false),
        Arguments.of(new Vector2d(1, 0), new Vector2d(0, 0), false),
        Arguments.of(new Vector2d(1, 0), "NO VECTOR", false),
        Arguments.of(new Vector2d(1, 0), null, false));
  }

  private static Stream<Arguments> providePrecedesTestCases() {
    return Stream.of(
        Arguments.of(new Vector2d(0, 0), new Vector2d(0, 0), true),
        Arguments.of(new Vector2d(0, 0), new Vector2d(1, 0), true),
        Arguments.of(new Vector2d(0, 0), new Vector2d(1, 1), true),
        Arguments.of(new Vector2d(0, 0), new Vector2d(0, 1), true),
        Arguments.of(new Vector2d(0, 0), new Vector2d(-1, 0), false),
        Arguments.of(new Vector2d(0, 0), new Vector2d(0, -1), false),
        Arguments.of(new Vector2d(0, 0), new Vector2d(-1, -1), false),
        Arguments.of(new Vector2d(0, 0), new Vector2d(-1, 1), false)
    );
  }

  private static Stream<Arguments> provideFollowsTestCases() {
    return Stream.of(
        Arguments.of(new Vector2d(0, 0), new Vector2d(0, 0), true),
        Arguments.of(new Vector2d(0, 0), new Vector2d(-1, 0), true),
        Arguments.of(new Vector2d(0, 0), new Vector2d(0, -1), true),
        Arguments.of(new Vector2d(0, 0), new Vector2d(-1, -1), true),
        Arguments.of(new Vector2d(0, 0), new Vector2d(-1, 1), false),
        Arguments.of(new Vector2d(0, 0), new Vector2d(1, 0), false),
        Arguments.of(new Vector2d(0, 0), new Vector2d(1, 1), false),
        Arguments.of(new Vector2d(0, 0), new Vector2d(0, 1), false)
    );
  }

  private static Stream<Arguments> provideUpperRightTestCases() {
    return Stream.of(
        Arguments.of(new Vector2d(0, 0), new Vector2d(0, 0), new Vector2d(0, 0)),
        Arguments.of(new Vector2d(2, -3), new Vector2d(1, 3), new Vector2d(2, 3)),
        Arguments.of(new Vector2d(2, 3), new Vector2d(3, 1), new Vector2d(3, 3)),
        Arguments.of(new Vector2d(1, 3), new Vector2d(4, 4), new Vector2d(4, 4)),
        Arguments.of(new Vector2d(4, 4), new Vector2d(1, 3), new Vector2d(4, 4))
    );
  }

  private static Stream<Arguments> provideLowerLeftTestCases() {
    return Stream.of(
        Arguments.of(new Vector2d(0, 0), new Vector2d(0, 0), new Vector2d(0, 0)),
        Arguments.of(new Vector2d(2, -3), new Vector2d(1, 3), new Vector2d(1, -3)),
        Arguments.of(new Vector2d(2, 3), new Vector2d(3, 1), new Vector2d(2, 1)),
        Arguments.of(new Vector2d(1, 3), new Vector2d(-1, -4), new Vector2d(-1, -4)),
        Arguments.of(new Vector2d(-4, -4), new Vector2d(1, 3), new Vector2d(-4, -4))
    );
  }
}