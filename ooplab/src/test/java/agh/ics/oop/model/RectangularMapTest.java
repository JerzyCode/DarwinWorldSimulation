package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangularMapTest {
  private RectangularMap rectangularMap;

  @BeforeEach
  void setUp() {
    rectangularMap = new RectangularMap(5, 5);
  }

  @Test
  void shouldInitializeRectangularMap() {
    //given
    int width = 5;
    int height = 5;

    //when
    var rectangularMap = new RectangularMap(width, height);

    //then
    assertEquals(new Vector2d(4, 4), rectangularMap.getRightTopCorner());
  }

  @Test
  void shouldPlaceAnimal() throws IncorrectPositionException {
    //given
    var animal = new Animal(new Vector2d(2, 2));

    //when
    rectangularMap.place(animal);

    //then
    assertEquals(animal, rectangularMap.getElements().iterator().next());
  }

  @Test
  void shouldThrowExceptionWhenAnimalPositionOccupied() throws IncorrectPositionException {
    //given
    var animal1 = new Animal(new Vector2d(2, 2));
    var animal2 = new Animal(new Vector2d(2, 2));
    rectangularMap.place(animal1);

    //when && then
    assertThrows(IncorrectPositionException.class, () -> rectangularMap.place(animal2));
    assertEquals(1, rectangularMap.getElements().size());
  }

  @Test
  void shouldThrowExceptionWhenAnimalWrongPosition() {
    //given
    var animal = new Animal(new Vector2d(-1, -1));

    //when && then
    assertThrows(IncorrectPositionException.class, () -> rectangularMap.place(animal));
    assertEquals(0, rectangularMap.getElements().size());
  }

  @Test
  void shouldMoveAnimal() throws IncorrectPositionException {
    //given
    var animal = new Animal(new Vector2d(2, 2), MapDirection.NORTH);
    rectangularMap.place(animal);

    //when
    rectangularMap.move(animal, MoveDirection.FORWARD);

    //then
    assertEquals(new Vector2d(2, 3), animal.getPosition());
    assertEquals(1, rectangularMap.getElements().size());
  }

  @Test
  void shouldNotMoveAnimalOnOccupiedPosition() throws IncorrectPositionException {
    //given
    var animal1 = new Animal(new Vector2d(2, 2), MapDirection.NORTH);
    var animal2 = new Animal(new Vector2d(2, 1), MapDirection.NORTH);
    rectangularMap.place(animal1);
    rectangularMap.place(animal2);

    //when
    rectangularMap.move(animal1, MoveDirection.FORWARD);

    //then
    assertEquals(new Vector2d(2, 1), animal2.getPosition());
    assertEquals(2, rectangularMap.getElements().size());
  }

  @Test
  void positionShouldBeOccupied() throws IncorrectPositionException {
    //given
    var animal = new Animal(new Vector2d(2, 2), MapDirection.NORTH);
    rectangularMap.place(animal);

    //when
    var result = rectangularMap.isOccupied(animal.getPosition());

    //then
    assertTrue(result);
  }

  @Test
  void positionShouldNotBeOccupied() throws IncorrectPositionException {
    //given
    var animal = new Animal(new Vector2d(4, 4), MapDirection.NORTH);
    rectangularMap.place(animal);

    //when
    var result = rectangularMap.isOccupied(new Vector2d(0, 0));

    //then
    assertFalse(result);
  }

  @Test
  void shouldReturnAnimalObjectAt() throws IncorrectPositionException {
    //given
    var animal = new Animal(new Vector2d(2, 2));
    rectangularMap.place(animal);

    //when
    var resultOptional = rectangularMap.objectAt(animal.getPosition());

    //then
    assertTrue(resultOptional.isPresent());
    assertEquals(animal, resultOptional.get());
  }

  @Test
  void cantMoveToPositionIsOccupied() throws IncorrectPositionException {
    //given
    var animal = new Animal(new Vector2d(2, 2), MapDirection.NORTH);
    rectangularMap.place(animal);

    //when
    var result = rectangularMap.canMoveTo(animal.getPosition());

    //then
    assertFalse(result);
  }

  @Test
  void cantMovePositionNotFollowsLeftBotCorner() {
    //when
    var result = rectangularMap.canMoveTo(new Vector2d(-1, -1));

    //then
    assertFalse(result);
  }

  @Test
  void cantMovePositionNotPrecedesRightTopCorner() {
    //when
    var result = rectangularMap.canMoveTo(new Vector2d(100, 100));

    //then
    assertFalse(result);
  }

  @Test
  void canMoveToPosition() {
    //when
    var result = rectangularMap.canMoveTo(new Vector2d(2, 2));

    //then
    assertTrue(result);
  }

  @Test
  void shouldReturnEmptyElements() {
    //when
    var result = rectangularMap.getElements();

    //then
    assertEquals(0, result.size());
  }

}