package agh.ics.oop.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class Vector2dTest {

  @ParameterizedTest
  @MethodSource("provideAddVectorsTestCases")
  void shouldAddVectors(Vector2d vector1, Vector2d vector2, Vector2d expected) {
    //given
    //when
    var result = vector1.add(vector2);

    //then
    Assertions.assertEquals(expected.getX(), result.getX());
    Assertions.assertEquals(expected.getY(), result.getY());
  }

  @ParameterizedTest
  @MethodSource("provideSubtractVectorsTestCases")
  void shouldSubtractVectors(Vector2d vector1, Vector2d vector2, Vector2d expected) {
    //given
    //when
    var result = vector1.subtract(vector2);

    //then
    Assertions.assertEquals(expected.getX(), result.getX());
    Assertions.assertEquals(expected.getY(), result.getY());
  }

  @ParameterizedTest
  @MethodSource("provideOppositeTestCases")
  void shouldReturnVectorWithOppositeCoordinates(Vector2d vector, Vector2d expected) {
    //given
    //when
    var result = vector.opposite();

    //then
    Assertions.assertEquals(expected.getX(), result.getX());
    Assertions.assertEquals(expected.getY(), result.getY());
  }

  @ParameterizedTest
  @MethodSource("provideToStringTestCases")
  void shouldReturnToString(Vector2d vector, String expected) {
    //given
    //when
    var result = vector.toString();

    //then
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

  private static Stream<Arguments> provideAddVectorsTestCases() {
    return Stream.of(
        Arguments.of(new Vector2d(0, 0), new Vector2d(0, 0), new Vector2d(0, 0)),
        Arguments.of(new Vector2d(1, 1), new Vector2d(2, 2), new Vector2d(3, 3)),
        Arguments.of(new Vector2d(1, 10), new Vector2d(23, 2), new Vector2d(24, 12)),
        Arguments.of(new Vector2d(-2, 13), new Vector2d(-4, -10), new Vector2d(-6, 3)),
        Arguments.of(new Vector2d(2, -13), new Vector2d(-4, 10), new Vector2d(-2, -3)));
  }

  private static Stream<Arguments> provideSubtractVectorsTestCases() {
    return Stream.of(
        Arguments.of(new Vector2d(0, 0), new Vector2d(0, 0), new Vector2d(0, 0)),
        Arguments.of(new Vector2d(1, 1), new Vector2d(2, 2), new Vector2d(-1, -1)),
        Arguments.of(new Vector2d(1, 10), new Vector2d(23, 2), new Vector2d(-22, 8)),
        Arguments.of(new Vector2d(-2, 13), new Vector2d(-4, -10), new Vector2d(2, 23)),
        Arguments.of(new Vector2d(2, -13), new Vector2d(-4, 10), new Vector2d(6, -23)));
  }

  private static Stream<Arguments> provideOppositeTestCases() {
    return Stream.of(
        Arguments.of(new Vector2d(0, 0), new Vector2d(0, 0)),
        Arguments.of(new Vector2d(1, 2), new Vector2d(-1, -2)),
        Arguments.of(new Vector2d(-1, 2), new Vector2d(1, -2)),
        Arguments.of(new Vector2d(-1, -2), new Vector2d(1, 2)),
        Arguments.of(new Vector2d(1, -2), new Vector2d(-1, 2)));
  }

  private static Stream<Arguments> provideToStringTestCases() {
    return Stream.of(
        Arguments.of(new Vector2d(0, 0), "(0, 0)"),
        Arguments.of(new Vector2d(1, 2), "(1, 2)"),
        Arguments.of(new Vector2d(-1, -2), "(-1, -2)"));
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