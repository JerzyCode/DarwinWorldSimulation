package agh.ics.oop.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AnimalTest {

  @Test
  void animalShouldHaveNorthDirectionAfterTurnLeft() {
    //given
    var animal = new Animal(new Vector2d(2, 2), MapDirection.EAST);

    //when
    animal.move(MoveDirection.LEFT);

    //then
    Assertions.assertEquals(MapDirection.NORTH, animal.getOrientation());
  }

  @Test
  void animalShouldHaveNorthDirectionAfterTurnRight() {
    //given
    var animal = new Animal(new Vector2d(2, 2), MapDirection.WEST);

    //when
    animal.move(MoveDirection.RIGHT);

    //then
    Assertions.assertEquals(MapDirection.NORTH, animal.getOrientation());
  }

  @Test
  void animalShouldHaveSouthDirectionAfterTurnLeft() {
    //given
    var animal = new Animal(new Vector2d(2, 2), MapDirection.WEST);

    //when
    animal.move(MoveDirection.LEFT);

    //then
    Assertions.assertEquals(MapDirection.SOUTH, animal.getOrientation());
  }

  @Test
  void animalShouldHaveSouthDirectionAfterTurnRight() {
    //given
    var animal = new Animal(new Vector2d(2, 2), MapDirection.EAST);

    //when
    animal.move(MoveDirection.RIGHT);

    //then
    Assertions.assertEquals(MapDirection.SOUTH, animal.getOrientation());
  }

  @Test
  void animalShouldHaveEastDirectionAfterTurnLeft() {
    //given
    var animal = new Animal(new Vector2d(2, 2), MapDirection.SOUTH);

    //when
    animal.move(MoveDirection.LEFT);

    //then
    Assertions.assertEquals(MapDirection.EAST, animal.getOrientation());
  }

  @Test
  void animalShouldHaveEastDirectionAfterTurnRight() {
    //given
    var animal = new Animal(new Vector2d(2, 2), MapDirection.NORTH);

    //when
    animal.move(MoveDirection.RIGHT);

    //then
    Assertions.assertEquals(MapDirection.EAST, animal.getOrientation());
  }

  @Test
  void animalShouldHaveWestDirectionAfterTurnLeft() {
    //given
    var animal = new Animal(new Vector2d(2, 2), MapDirection.NORTH);

    //when
    animal.move(MoveDirection.LEFT);

    //then
    Assertions.assertEquals(MapDirection.WEST, animal.getOrientation());
  }

  @Test
  void animalShouldHaveWestDirectionAfterTurnRight() {
    //given
    var animal = new Animal(new Vector2d(2, 2), MapDirection.SOUTH);

    //when
    animal.move(MoveDirection.RIGHT);

    //then
    Assertions.assertEquals(MapDirection.WEST, animal.getOrientation());
  }

  @Test
  void animalsShouldIncreaseX() {
    //given
    var animal1 = new Animal(new Vector2d(2, 2), MapDirection.EAST);
    var animal2 = new Animal(new Vector2d(2, 2), MapDirection.WEST);

    //when
    animal1.move(MoveDirection.FORWARD);
    animal2.move(MoveDirection.BACKWARD);

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

    //when
    animal1.move(MoveDirection.BACKWARD);
    animal2.move(MoveDirection.FORWARD);

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

    //when
    animal1.move(MoveDirection.FORWARD);
    animal2.move(MoveDirection.BACKWARD);

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

    //when
    animal1.move(MoveDirection.BACKWARD);
    animal2.move(MoveDirection.FORWARD);

    //then
    Assertions.assertEquals(MapDirection.NORTH, animal1.getOrientation());
    Assertions.assertEquals(new Vector2d(2, 1), animal1.getPosition());

    Assertions.assertEquals(MapDirection.SOUTH, animal2.getOrientation());
    Assertions.assertEquals(new Vector2d(2, 1), animal2.getPosition());
  }

}