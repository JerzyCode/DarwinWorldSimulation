package agh.ics.oop.model.map;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.move.Move;
import agh.ics.oop.model.move.MoveDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class EarthUnitTest {

  private Earth map;

  @BeforeEach
  void setUp() {
    map = new Earth(5, 5);
  }

  @Test
  void shouldCreateBoundary() {
    assertEquals(new Vector2d(0, 0), map.getCurrentBounds().leftBottomCorner());
    assertEquals(new Vector2d(4, 4), map.getCurrentBounds().rightTopCorner());
  }

  @Test
  void canMoveToShouldReturnTrue() {
    assertTrue(map.canMoveTo(new Vector2d(0, 0)));
    assertTrue(map.canMoveTo(new Vector2d(1, 2)));
    assertTrue(map.canMoveTo(new Vector2d(2, 1)));
    assertTrue(map.canMoveTo(new Vector2d(3, 3)));
  }

  @Test
  void canMoveToShouldReturnFalse() {
    assertFalse(map.canMoveTo(new Vector2d(-1, -2)));
    assertFalse(map.canMoveTo(new Vector2d(-10000, -1)));
    assertFalse(map.canMoveTo(new Vector2d(0, 11110)));
    assertFalse(map.canMoveTo(new Vector2d(11110, 0)));
  }

  @ParameterizedTest
  @MethodSource("provideAdjustMoveArguments")
  void adjustMoveWhenTryGoOutsideMapWest(Move givenMove, Move expected) {
    //when
    var result = map.adjustMove(givenMove);

    //then
    assertEquals(expected, result);
  }

  @ParameterizedTest
  @MethodSource("providePlacePlantArgumentsSuccess")
  void placePlantSuccess(Plant plant) {
    //when && then
    try {
      map.placePlant(plant);
      assertEquals(1, map.plants.size());
      assertEquals(map.plants.get(plant.getPosition()), plant);
    }
    catch (IncorrectPositionException e) {
      fail(String.format("Place plant on position=%s expected to not throw exception.", plant.getPosition()));
    }
  }

  @ParameterizedTest
  @MethodSource("providePlacePlantArgumentsFail")
  void placePlantsOutsideMapShouldThrowException(Plant plant) {
    //then && then
    assertThrows(IncorrectPositionException.class, () -> map.placePlant(plant));
    assertEquals(0, map.plants.size());
  }

  @Test
  void placePlantOnOccupiedByPlantPositionShouldThrowException() {
    //given
    var plantPlaced = new Plant(new Vector2d(2, 3));
    map.plants.put(plantPlaced.getPosition(), plantPlaced);
    var plantToPlace = new Plant(plantPlaced.getPosition());

    //when && then
    assertThrows(IncorrectPositionException.class, () -> map.placePlant(plantToPlace));
    assertEquals(1, map.plants.size());
    assertEquals(plantPlaced, map.getPlantAtPosition(plantPlaced.getPosition()));
  }

  @Test
  void removePlantShouldSuccess() {
    //given
    var plantPlaced1 = new Plant(new Vector2d(2, 3));
    var plantPlaced2 = new Plant(new Vector2d(1, 4));
    var plantPlaced3 = new Plant(new Vector2d(3, 2));
    var wrongPosition = new Vector2d(0, 0);

    try {
      map.placePlant(plantPlaced1);
      map.placePlant(plantPlaced2);
      map.placePlant(plantPlaced3);
    }
    catch (IncorrectPositionException e) {
      fail("Should not fail remove plant test, e=" + e.getMessage());
    }

    //when
    map.removePlant(plantPlaced1.getPosition());
    map.removePlant(plantPlaced2.getPosition());
    map.removePlant(wrongPosition);

    //then
    assertEquals(1, map.plants.size());
    assertFalse(map.isPlantAtPosition(plantPlaced1.getPosition()));
    assertFalse(map.isPlantAtPosition(plantPlaced2.getPosition()));
    assertTrue(map.isPlantAtPosition(plantPlaced3.getPosition()));
  }

  @Test
  void moveAnimalAdjusterShouldNoTrigger() {
    //given
    var animalN = new Animal(new Vector2d(2, 2), MapDirection.NORTH);
    var animalS = new Animal(new Vector2d(1, 3), MapDirection.SOUTH);
    try {
      map.place(animalN);
      map.place(animalS);
    }
    catch (IncorrectPositionException e) {
      fail("Should not fail placing animal, e=" + e.getMessage());
    }

    //when
    map.move(animalN, MoveDirection.FORWARD);
    map.move(animalS, MoveDirection.FORWARD);

    //then
    assertEquals(2, map.animals.size());
    assertEquals(new Vector2d(2, 3), animalN.getPosition());
    assertEquals(new Vector2d(1, 2), animalS.getPosition());
  }

  @Test
  void moveAnimalsOnTheSamePosition() {
    //given
    var animalN = new Animal(new Vector2d(2, 2), MapDirection.NORTH);
    var animalE = new Animal(new Vector2d(1, 2), MapDirection.EAST);
    try {
      map.place(animalN);
      map.place(animalE);
    }
    catch (IncorrectPositionException e) {
      fail("Should not fail placing animal, e=" + e.getMessage());
    }

    //when
    map.move(animalE, MoveDirection.FORWARD);

    //then
    //    assertEquals(2, map.animals.size()); TODO po zmianie ze na polu moga byc 2 zwierzaki
    assertEquals(new Vector2d(2, 2), animalE.getPosition());
    assertEquals(new Vector2d(2, 2), animalN.getPosition());
  }

  @Test
  void moveAnimalsWithAdjusterHorizontal() {
    // given
    var animalW = new Animal(new Vector2d(0, 3), MapDirection.WEST);
    var animalE = new Animal(new Vector2d(4, 2), MapDirection.EAST);
    try {
      map.place(animalW);
      map.place(animalE);
    }
    catch (IncorrectPositionException e) {
      fail("Should not fail placing animal, e=" + e.getMessage());
    }

    //when
    map.move(animalW, MoveDirection.FORWARD);
    map.move(animalE, MoveDirection.FORWARD);

    //then
    assertEquals(2, map.animals.size());
    assertEquals(new Vector2d(4, 3), animalW.getPosition());
    assertEquals(new Vector2d(0, 2), animalE.getPosition());
    assertEquals(animalE, map.animals.get(animalE.getPosition()));
    assertEquals(animalW, map.animals.get(animalW.getPosition()));
  }

  @Test
  void moveAnimalsWithAdjusterVertical() {
    // given
    var animalN = new Animal(new Vector2d(2, 4), MapDirection.NORTH);
    var animalS = new Animal(new Vector2d(1, 0), MapDirection.SOUTH);
    try {
      map.place(animalN);
      map.place(animalS);
    }
    catch (IncorrectPositionException e) {
      fail("Should not fail placing animal, e=" + e.getMessage());
    }

    //when
    map.move(animalN, MoveDirection.FORWARD);
    map.move(animalS, MoveDirection.FORWARD);

    //then
    assertEquals(2, map.animals.size());
    assertEquals(new Vector2d(2, 4), animalN.getPosition());
    assertEquals(new Vector2d(1, 0), animalS.getPosition());
    assertEquals(MapDirection.SOUTH, animalN.getOrientation());
    assertEquals(MapDirection.NORTH, animalS.getOrientation());
    assertEquals(animalN, map.animals.get(animalN.getPosition()));
    assertEquals(animalS, map.animals.get(animalS.getPosition()));
  }

  @Test
  void getSizeShouldReturn() {
    var map1 = new Earth(4, 10);
    var map2 = new Earth(14, 5);
    assertEquals(4 * 10, map1.getSize());
    assertEquals(14 * 5, map2.getSize());
  }

  private static Stream<Arguments> provideAdjustMoveArguments() {
    return Stream.of(
        Arguments.of(new Move(new Vector2d(-1, 0), MapDirection.WEST), new Move(new Vector2d(4, 0), MapDirection.WEST)),
        Arguments.of(new Move(new Vector2d(-1, 2), MapDirection.WEST), new Move(new Vector2d(4, 2), MapDirection.WEST)),
        Arguments.of(new Move(new Vector2d(-1, 4), MapDirection.WEST), new Move(new Vector2d(4, 4), MapDirection.WEST)),
        Arguments.of(new Move(new Vector2d(5, 0), MapDirection.EAST), new Move(new Vector2d(0, 0), MapDirection.EAST)),
        Arguments.of(new Move(new Vector2d(5, 2), MapDirection.EAST), new Move(new Vector2d(0, 2), MapDirection.EAST)),
        Arguments.of(new Move(new Vector2d(5, 4), MapDirection.EAST), new Move(new Vector2d(0, 4), MapDirection.EAST)),
        Arguments.of(new Move(new Vector2d(0, 5), MapDirection.NORTH), new Move(new Vector2d(0, 4), MapDirection.SOUTH)),
        Arguments.of(new Move(new Vector2d(2, 5), MapDirection.NORTH), new Move(new Vector2d(2, 4), MapDirection.SOUTH)),
        Arguments.of(new Move(new Vector2d(4, 5), MapDirection.NORTH), new Move(new Vector2d(4, 4), MapDirection.SOUTH)),
        Arguments.of(new Move(new Vector2d(0, -1), MapDirection.SOUTH), new Move(new Vector2d(0, 0), MapDirection.NORTH)),
        Arguments.of(new Move(new Vector2d(2, -1), MapDirection.SOUTH), new Move(new Vector2d(2, 0), MapDirection.NORTH)),
        Arguments.of(new Move(new Vector2d(4, -1), MapDirection.SOUTH), new Move(new Vector2d(4, 0), MapDirection.NORTH))
    );
  }

  private static Stream<Arguments> providePlacePlantArgumentsSuccess() {
    return Stream.of(
        Arguments.of(new Plant(new Vector2d(0, 0))),
        Arguments.of(new Plant(new Vector2d(0, 1))),
        Arguments.of(new Plant(new Vector2d(1, 0))),
        Arguments.of(new Plant(new Vector2d(2, 3))),
        Arguments.of(new Plant(new Vector2d(4, 4)))
    );
  }

  private static Stream<Arguments> providePlacePlantArgumentsFail() {
    return Stream.of(
        Arguments.of(new Plant(new Vector2d(-1, 0))),
        Arguments.of(new Plant(new Vector2d(0, -1))),
        Arguments.of(new Plant(new Vector2d(5, 0))),
        Arguments.of(new Plant(new Vector2d(0, 5))),
        Arguments.of(new Plant(new Vector2d(5, 5)))
    );
  }
}