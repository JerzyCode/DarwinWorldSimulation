package agh.ics.oop.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class AnimalTest {

  @ParameterizedTest
  @MethodSource("provideIsAtPositionTestCases")
  void shouldReturnIfIsAtPosition(Vector2d animalPosition, Vector2d position, boolean expected) {
    //given
    var animal = new Animal(animalPosition);

    //when
    var result = animal.isAt(position);

    //then
    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @MethodSource("provideMoveAnimalTestCases")
  void shouldMoveAnimal(MoveDirection moveDirection, Vector2d expectedPosition, MapDirection expectedOrientation) {
    //given
    var animal = new Animal();

    //when
    animal.move(moveDirection);

    //then
    Assertions.assertEquals(animal.getPosition(), expectedPosition);
    Assertions.assertEquals(animal.getOrientation(), expectedOrientation);
  }

  @ParameterizedTest
  @MethodSource("provideOnlyChangeAnimalTestCases")
  void shouldOnlyChangeDirectionToSouth(MoveDirection direction, Vector2d position, MapDirection expectedOrientation, Vector2d expectedPosition) {
    //given
    var animal = new Animal(position);

    //when
    animal.move(direction);

    //then
    Assertions.assertEquals(animal.getOrientation(), expectedOrientation);
    Assertions.assertEquals(animal.getPosition(), expectedPosition);
  }

  private static Stream<Arguments> provideIsAtPositionTestCases() {
    return Stream.of(
        Arguments.of(new Vector2d(2, 4), new Vector2d(2, 4), true),
        Arguments.of(new Vector2d(0, 0), new Vector2d(0, 0), true),
        Arguments.of(new Vector2d(2, 3), new Vector2d(2, 2), false),
        Arguments.of(new Vector2d(4, 3), new Vector2d(2, 2), false),
        Arguments.of(new Vector2d(1, 2), new Vector2d(2, 2), false)
    );
  }

  private static Stream<Arguments> provideMoveAnimalTestCases() {
    return Stream.of(
        Arguments.of(MoveDirection.FORWARD, new Vector2d(2, 3), MapDirection.NORTH),
        Arguments.of(MoveDirection.BACKWARD, new Vector2d(2, 1), MapDirection.SOUTH),
        Arguments.of(MoveDirection.LEFT, new Vector2d(1, 2), MapDirection.WEST),
        Arguments.of(MoveDirection.RIGHT, new Vector2d(3, 2), MapDirection.EAST)
    );
  }

  private static Stream<Arguments> provideOnlyChangeAnimalTestCases() {
    return Stream.of(
        Arguments.of(MoveDirection.BACKWARD, new Vector2d(0, 0), MapDirection.SOUTH, new Vector2d(0, 0)),
        Arguments.of(MoveDirection.LEFT, new Vector2d(0, 0), MapDirection.WEST, new Vector2d(0, 0)),
        Arguments.of(MoveDirection.FORWARD, new Vector2d(4, 4), MapDirection.NORTH, new Vector2d(4, 4)),
        Arguments.of(MoveDirection.RIGHT, new Vector2d(4, 4), MapDirection.EAST, new Vector2d(4, 4)));
  }

}