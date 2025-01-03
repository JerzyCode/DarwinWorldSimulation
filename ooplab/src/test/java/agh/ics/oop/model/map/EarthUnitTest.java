package agh.ics.oop.model.map;

import agh.ics.oop.TestAnimalBuilder;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Gen;
import agh.ics.oop.model.elements.Genome;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.exceptions.PositionOccupiedByWorldElementException;
import agh.ics.oop.model.exceptions.PositionOutOfMapBoundaryException;
import agh.ics.oop.model.move.Move;
import agh.ics.oop.model.move.MoveDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
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
    void placeAnimalShouldCreateNewListAtPosition() {
        //given
        var animal = TestAnimalBuilder.create().position(new Vector2d(2, 2)).build();

        //when && then
        try {
            map.place(animal);
            var animalsAtPosition = map.getAnimalsAtPosition(animal.getPosition());
            assertEquals(1, animalsAtPosition.size());
            assertTrue(animalsAtPosition.contains(animal));
        } catch (IncorrectPositionException e) {
            fail("Should not fail place animal test, e=" + e.getMessage());
        }
    }

    @Test
    void placeAnimalOutsideMapShouldThrowException() {
        //given
        var animal1 = TestAnimalBuilder.create().position(new Vector2d(-1, 2)).build();
        var animal2 = TestAnimalBuilder.create().position(new Vector2d(0, 10)).build();
        var animal3 = TestAnimalBuilder.create().position(new Vector2d(15, 2)).build();
        var animal4 = TestAnimalBuilder.create().position(new Vector2d(3, -2)).build();

        //when && then
        assertThrows(PositionOutOfMapBoundaryException.class, () -> map.place(animal1));
        assertThrows(PositionOutOfMapBoundaryException.class, () -> map.place(animal2));
        assertThrows(PositionOutOfMapBoundaryException.class, () -> map.place(animal3));
        assertThrows(PositionOutOfMapBoundaryException.class, () -> map.place(animal4));
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
        } catch (IncorrectPositionException e) {
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
        assertThrows(PositionOccupiedByWorldElementException.class, () -> map.placePlant(plantToPlace));
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
        } catch (IncorrectPositionException e) {
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
        var animalN = TestAnimalBuilder.create()
                .position(new Vector2d(2, 2))
                .genome(new Genome(List.of(new Gen(0))))
                .build();

        var animalS = TestAnimalBuilder.create()
                .position(new Vector2d(1, 3))
                .genome(new Genome(List.of(new Gen(4))))
                .build();
        try {
            map.place(animalN);
            map.place(animalS);
        } catch (IncorrectPositionException e) {
            fail("Should not fail placing animal, e=" + e.getMessage());
        }

        //when
        map.move(animalN, MoveDirection.FORWARD);
        map.move(animalS, MoveDirection.FORWARD);

        //then
        assertEquals(2, map.getElements().size());
        assertEquals(new Vector2d(2, 3), animalN.getPosition());
        assertEquals(new Vector2d(1, 2), animalS.getPosition());
    }

    @Test
    void moveAnimalsOnTheSamePosition() {
        //given
        var animalN = TestAnimalBuilder.create()
                .position(new Vector2d(2, 2))
                .genome(new Genome(List.of(new Gen(0))))
                .build();

        var animalE = TestAnimalBuilder.create()
                .position(new Vector2d(1, 2))
                .genome(new Genome(List.of(new Gen(2))))
                .build();
        try {
            map.place(animalN);
            map.place(animalE);
        } catch (IncorrectPositionException e) {
            fail("Should not fail placing animal, e=" + e.getMessage());
        }

        //when
        map.move(animalE, MoveDirection.FORWARD);

        //then
        var animalsAtPosition = map.getAnimalsAtPosition(animalN.getPosition());
        assertTrue(animalsAtPosition.contains(animalE));
        assertTrue(animalsAtPosition.contains(animalN));
        assertEquals(new Vector2d(2, 2), animalE.getPosition());
        assertEquals(new Vector2d(2, 2), animalN.getPosition());
    }

    @Test
    void moveAnimalsWithAdjusterHorizontal() {
        // given
        var animalW = TestAnimalBuilder.create()
                .position(new Vector2d(0, 3))
                .genome(new Genome(List.of(new Gen(6))))
                .build();

        var animalE = TestAnimalBuilder.create()
                .position(new Vector2d(4, 2))
                .genome(new Genome(List.of(new Gen(2))))
                .build();
        try {
            map.place(animalW);
            map.place(animalE);
        } catch (IncorrectPositionException e) {
            fail("Should not fail placing animal, e=" + e.getMessage());
        }

        //when
        map.move(animalW, MoveDirection.FORWARD);
        map.move(animalE, MoveDirection.FORWARD);

        //then
        assertEquals(2, map.getElements().size());
        assertEquals(new Vector2d(4, 3), animalW.getPosition());
        assertEquals(new Vector2d(0, 2), animalE.getPosition());
        assertEquals(animalE, map.objectAt(animalE.getPosition()));
        assertEquals(animalW, map.objectAt(animalW.getPosition()));
    }

    @Test
    void moveAnimalsWithAdjusterVertical() {
        // given
        var animalN = TestAnimalBuilder.create()
                .position(new Vector2d(2, 4))
                .genome(new Genome(List.of(new Gen(0))))
                .build();

        var animalS = TestAnimalBuilder.create()
                .position(new Vector2d(1, 0))
                .genome(new Genome(List.of(new Gen(4))))
                .build();
        try {
            map.place(animalN);
            map.place(animalS);
        } catch (IncorrectPositionException e) {
            fail("Should not fail placing animal, e=" + e.getMessage());
        }

        //when
        map.move(animalN, MoveDirection.FORWARD);
        map.move(animalS, MoveDirection.FORWARD);

        //then
        assertEquals(2, map.getElements().size());
        assertEquals(new Vector2d(2, 4), animalN.getPosition());
        assertEquals(new Vector2d(1, 0), animalS.getPosition());
        assertEquals(MapDirection.SOUTH, animalN.getOrientation());
        assertEquals(MapDirection.NORTH, animalS.getOrientation());
        assertEquals(animalN, map.objectAt(animalN.getPosition()));
        assertEquals(animalS, map.objectAt(animalS.getPosition()));
    }

    @Test
    void moveAnimalsWithAdjusterCorners() {
        // given
        var animalNE = TestAnimalBuilder.create()
                .position(new Vector2d(4, 4))
                .genome(new Genome(List.of(new Gen(1))))
                .build();

        var animalSE = TestAnimalBuilder.create()
                .position(new Vector2d(4, 0))
                .genome(new Genome(List.of(new Gen(3))))
                .build();

        var animalSW = TestAnimalBuilder.create()
                .position(new Vector2d(0, 0))
                .genome(new Genome(List.of(new Gen(5))))
                .build();

        var animalNW = TestAnimalBuilder.create()
                .position(new Vector2d(0, 4))
                .genome(new Genome(List.of(new Gen(7))))
                .build();
        try {
            map.place(animalNE);
            map.place(animalSE);
            map.place(animalSW);
            map.place(animalNW);
        } catch (IncorrectPositionException e) {
            fail("Should not fail placing animal, e=" + e.getMessage());
        }

        //when & then
        assertEquals(4, map.getElements().size());

        map.move(animalNE, MoveDirection.FORWARD);
        assertEquals(new Vector2d(0, 4), animalNE.getPosition());

        map.move(animalSE, MoveDirection.FORWARD);
        assertEquals(new Vector2d(0, 0), animalSE.getPosition());

        map.move(animalSW, MoveDirection.FORWARD);
        assertEquals(new Vector2d(4, 0), animalSW.getPosition());

        map.move(animalNW, MoveDirection.FORWARD);
        assertEquals(new Vector2d(4, 4), animalNW.getPosition());

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

    @Test
    void shouldRemoveAnimal() {
        //given
        var animal1 = TestAnimalBuilder.create()
                .position(new Vector2d(2, 2))
                .build();

        var animal2 = TestAnimalBuilder.create()
                .position(new Vector2d(2, 2))
                .build();

        try {
            map.place(animal1);
            map.place(animal2);
        } catch (IncorrectPositionException e) {
            fail("Should not fail placing animal, e=" + e.getMessage());
        }

        //when
        map.removeAnimal(animal2);

        //then
        var animalsAtPosition = map.getAnimalsAtPosition(new Vector2d(2, 2));
        assertEquals(1, animalsAtPosition.size());
        assertTrue(animalsAtPosition.contains(animal1));
        assertFalse(animalsAtPosition.contains(animal2));
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