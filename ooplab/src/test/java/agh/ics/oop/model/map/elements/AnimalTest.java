package agh.ics.oop.model.map.elements;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.move.MoveDirection;
import agh.ics.oop.model.move.MoveValidator;
import agh.ics.oop.model.Vector2d;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

class AnimalTest {
  private final MoveValidator validator = Mockito.mock(MoveValidator.class);

  @Test
  void animalShouldHaveNorthDirectionAfterTurnLeft() {
    //given
    var animal = new Animal(new Vector2d(2, 2), MapDirection.EAST);
    Mockito.when(validator.canMoveTo(any())).thenReturn(true);

    //when
    animal.move(MoveDirection.LEFT, validator);

    //then
    Assertions.assertEquals(MapDirection.NORTH, animal.getOrientation());
  }

  @Test
  void animalShouldHaveNorthDirectionAfterTurnRight() {
    //given
    var animal = new Animal(new Vector2d(2, 2), MapDirection.WEST);
    Mockito.when(validator.canMoveTo(any())).thenReturn(true);

    //when
    animal.move(MoveDirection.RIGHT, validator);

    //then
    Assertions.assertEquals(MapDirection.NORTH, animal.getOrientation());
  }

  @Test
  void animalShouldHaveSouthDirectionAfterTurnLeft() {
    //given
    var animal = new Animal(new Vector2d(2, 2), MapDirection.WEST);
    Mockito.when(validator.canMoveTo(any())).thenReturn(true);

    //when
    animal.move(MoveDirection.LEFT, validator);

    //then
    Assertions.assertEquals(MapDirection.SOUTH, animal.getOrientation());
  }

  @Test
  void animalShouldHaveSouthDirectionAfterTurnRight() {
    //given
    var animal = new Animal(new Vector2d(2, 2), MapDirection.EAST);
    Mockito.when(validator.canMoveTo(any())).thenReturn(true);

    //when
    animal.move(MoveDirection.RIGHT, validator);

    //then
    Assertions.assertEquals(MapDirection.SOUTH, animal.getOrientation());
  }

  @Test
  void animalShouldHaveEastDirectionAfterTurnLeft() {
    //given
    var animal = new Animal(new Vector2d(2, 2), MapDirection.SOUTH);
    Mockito.when(validator.canMoveTo(any())).thenReturn(true);

    //when
    animal.move(MoveDirection.LEFT, validator);

    //then
    Assertions.assertEquals(MapDirection.EAST, animal.getOrientation());
  }

  @Test
  void animalShouldHaveEastDirectionAfterTurnRight() {
    //given
    var animal = new Animal(new Vector2d(2, 2), MapDirection.NORTH);
    Mockito.when(validator.canMoveTo(any())).thenReturn(true);

    //when
    animal.move(MoveDirection.RIGHT, validator);

    //then
    Assertions.assertEquals(MapDirection.EAST, animal.getOrientation());
  }

  @Test
  void animalShouldHaveWestDirectionAfterTurnLeft() {
    //given
    var animal = new Animal(new Vector2d(2, 2), MapDirection.NORTH);
    Mockito.when(validator.canMoveTo(any())).thenReturn(true);

    //when
    animal.move(MoveDirection.LEFT, validator);

    //then
    Assertions.assertEquals(MapDirection.WEST, animal.getOrientation());
  }

  @Test
  void animalShouldHaveWestDirectionAfterTurnRight() {
    //given
    var animal = new Animal(new Vector2d(2, 2), MapDirection.SOUTH);
    Mockito.when(validator.canMoveTo(any())).thenReturn(true);

    //when
    animal.move(MoveDirection.RIGHT, validator);

    //then
    Assertions.assertEquals(MapDirection.WEST, animal.getOrientation());
  }

  @Test
  void animalsShouldIncreaseX() {
    //given
    var animal1 = new Animal(new Vector2d(2, 2), MapDirection.EAST);
    var animal2 = new Animal(new Vector2d(2, 2), MapDirection.WEST);
    Mockito.when(validator.canMoveTo(any())).thenReturn(true);

    //when
    animal1.move(MoveDirection.FORWARD, validator);
    animal2.move(MoveDirection.BACKWARD, validator);

    //then
    Assertions.assertEquals(MapDirection.EAST, animal1.getOrientation());
    Assertions.assertEquals(new Vector2d(3, 2), animal1.getPosition());

    Assertions.assertEquals(MapDirection.WEST, animal2.getOrientation());
    Assertions.assertEquals(new Vector2d(3, 2), animal2.getPosition());
  }

  @Test
  void animalsShouldDecreaseX() {
    //given
    var animal1 = new Animal(new Vector2d(2, 2), MapDirection.EAST);
    var animal2 = new Animal(new Vector2d(2, 2), MapDirection.WEST);
    Mockito.when(validator.canMoveTo(any())).thenReturn(true);

    //when
    animal1.move(MoveDirection.BACKWARD, validator);
    animal2.move(MoveDirection.FORWARD, validator);

    //then
    Assertions.assertEquals(MapDirection.EAST, animal1.getOrientation());
    Assertions.assertEquals(new Vector2d(1, 2), animal1.getPosition());

    Assertions.assertEquals(MapDirection.WEST, animal2.getOrientation());
    Assertions.assertEquals(new Vector2d(1, 2), animal2.getPosition());
  }

  @Test
  void animalsShouldIncreaseY() {
    //given
    var animal1 = new Animal(new Vector2d(2, 2), MapDirection.NORTH);
    var animal2 = new Animal(new Vector2d(2, 2), MapDirection.SOUTH);
    Mockito.when(validator.canMoveTo(any())).thenReturn(true);

    //when
    animal1.move(MoveDirection.FORWARD, validator);
    animal2.move(MoveDirection.BACKWARD, validator);

    //then
    Assertions.assertEquals(MapDirection.NORTH, animal1.getOrientation());
    Assertions.assertEquals(new Vector2d(2, 3), animal1.getPosition());

    Assertions.assertEquals(MapDirection.SOUTH, animal2.getOrientation());
    Assertions.assertEquals(new Vector2d(2, 3), animal2.getPosition());
  }

  @Test
  void animalsShouldDecreaseY() {
    //given
    var animal1 = new Animal(new Vector2d(2, 2), MapDirection.NORTH);
    var animal2 = new Animal(new Vector2d(2, 2), MapDirection.SOUTH);
    Mockito.when(validator.canMoveTo(any())).thenReturn(true);

    //when
    animal1.move(MoveDirection.BACKWARD, validator);
    animal2.move(MoveDirection.FORWARD, validator);

    //then
    Assertions.assertEquals(MapDirection.NORTH, animal1.getOrientation());
    Assertions.assertEquals(new Vector2d(2, 1), animal1.getPosition());

    Assertions.assertEquals(MapDirection.SOUTH, animal2.getOrientation());
    Assertions.assertEquals(new Vector2d(2, 1), animal2.getPosition());
  }

  @Test
  void animalShouldNotMoveForwardWhenValidatorReturnFalse() {
    //given
    var animal = new Animal(new Vector2d(2, 2));
    Mockito.when(validator.canMoveTo(any())).thenReturn(false);

    //when
    animal.move(MoveDirection.FORWARD, validator);

    //then
    Assertions.assertEquals(MapDirection.NORTH, animal.getOrientation());
    Assertions.assertEquals(new Vector2d(2, 2), animal.getPosition());
  }

  @Test
  void animalShouldNotMoveBackwardWhenValidatorReturnFalse() {
    //given
    var animal = new Animal(new Vector2d(2, 2));
    Mockito.when(validator.canMoveTo(any())).thenReturn(false);

    //when
    animal.move(MoveDirection.BACKWARD, validator);

    //then
    Assertions.assertEquals(MapDirection.NORTH, animal.getOrientation());
    Assertions.assertEquals(new Vector2d(2, 2), animal.getPosition());
  }

}