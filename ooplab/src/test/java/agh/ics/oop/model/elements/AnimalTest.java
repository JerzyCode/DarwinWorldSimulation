package agh.ics.oop.model.elements;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.exceptions.InvalidCountException;
import agh.ics.oop.model.move.MoveDirection;
import agh.ics.oop.model.move.MoveValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AnimalTest {
    private final MoveValidator validator = Mockito.mock(MoveValidator.class);

    @Test
    void animalShouldHaveNorthDirectionAfterTurnLeft() {
        //given
        var animal = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.EAST)
                .build();
        when(validator.canMoveTo(any())).thenReturn(true);

        //when
        animal.move(MoveDirection.LEFT, validator);

        //then
        assertEquals(MapDirection.NORTH, animal.getOrientation());
    }

    @Test
    void animalShouldHaveNorthDirectionAfterTurnRight() {
        //given
        var animal = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.WEST)
                .build();
        when(validator.canMoveTo(any())).thenReturn(true);

        //when
        animal.move(MoveDirection.RIGHT, validator);

        //then
        assertEquals(MapDirection.NORTH, animal.getOrientation());
    }

    @Test
    void animalShouldHaveSouthDirectionAfterTurnLeft() {
        //given
        var animal = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.WEST)
                .build();
        when(validator.canMoveTo(any())).thenReturn(true);

        //when
        animal.move(MoveDirection.LEFT, validator);

        //then
        assertEquals(MapDirection.SOUTH, animal.getOrientation());
    }

    @Test
    void animalShouldHaveSouthDirectionAfterTurnRight() {
        //given
        var animal = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.EAST)
                .build();
        when(validator.canMoveTo(any())).thenReturn(true);

        //when
        animal.move(MoveDirection.RIGHT, validator);

        //then
        assertEquals(MapDirection.SOUTH, animal.getOrientation());
    }

    @Test
    void animalShouldHaveEastDirectionAfterTurnLeft() {
        //given
        var animal = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.SOUTH)
                .build();
        when(validator.canMoveTo(any())).thenReturn(true);

        //when
        animal.move(MoveDirection.LEFT, validator);

        //then
        assertEquals(MapDirection.EAST, animal.getOrientation());
    }

    @Test
    void animalShouldHaveEastDirectionAfterTurnRight() {
        //given
        var animal = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.NORTH)
                .build();
        when(validator.canMoveTo(any())).thenReturn(true);

        //when
        animal.move(MoveDirection.RIGHT, validator);

        //then
        assertEquals(MapDirection.EAST, animal.getOrientation());
    }

    @Test
    void animalShouldHaveWestDirectionAfterTurnLeft() {
        //given
        var animal = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.NORTH)
                .build();
        when(validator.canMoveTo(any())).thenReturn(true);

        //when
        animal.move(MoveDirection.LEFT, validator);

        //then
        assertEquals(MapDirection.WEST, animal.getOrientation());
    }

    @Test
    void animalShouldHaveWestDirectionAfterTurnRight() {
        //given
        var animal = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.SOUTH)
                .build();
        when(validator.canMoveTo(any())).thenReturn(true);

        //when
        animal.move(MoveDirection.RIGHT, validator);

        //then
        assertEquals(MapDirection.WEST, animal.getOrientation());
    }

    @Test
    void animalsShouldIncreaseX() {
        //given
        var animal1 = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.EAST)
                .build();
        var animal2 = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.WEST)
                .build();
        when(validator.canMoveTo(any())).thenReturn(true);

        //when
        animal1.move(MoveDirection.FORWARD, validator);
        animal2.move(MoveDirection.BACKWARD, validator);

        //then
        assertEquals(MapDirection.EAST, animal1.getOrientation());
        assertEquals(new Vector2d(3, 2), animal1.getPosition());

        assertEquals(MapDirection.WEST, animal2.getOrientation());
        assertEquals(new Vector2d(3, 2), animal2.getPosition());
    }

    @Test
    void animalsShouldDecreaseX() {
        //given
        var animal1 = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.EAST)
                .build();
        var animal2 = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.WEST)
                .build();
        when(validator.canMoveTo(any())).thenReturn(true);

        //when
        animal1.move(MoveDirection.BACKWARD, validator);
        animal2.move(MoveDirection.FORWARD, validator);

        //then
        assertEquals(MapDirection.EAST, animal1.getOrientation());
        assertEquals(new Vector2d(1, 2), animal1.getPosition());

        assertEquals(MapDirection.WEST, animal2.getOrientation());
        assertEquals(new Vector2d(1, 2), animal2.getPosition());
    }

    @Test
    void animalsShouldIncreaseY() {
        //given
        var animal1 = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.NORTH)
                .build();
        var animal2 = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.SOUTH)
                .build();
        when(validator.canMoveTo(any())).thenReturn(true);

        //when
        animal1.move(MoveDirection.FORWARD, validator);
        animal2.move(MoveDirection.BACKWARD, validator);

        //then
        assertEquals(MapDirection.NORTH, animal1.getOrientation());
        assertEquals(new Vector2d(2, 3), animal1.getPosition());

        assertEquals(MapDirection.SOUTH, animal2.getOrientation());
        assertEquals(new Vector2d(2, 3), animal2.getPosition());
    }

    @Test
    void animalsShouldDecreaseY() {
        //given
        var animal1 = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.NORTH)
                .build();
        var animal2 = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.SOUTH)
                .build();
        when(validator.canMoveTo(any())).thenReturn(true);

        //when
        animal1.move(MoveDirection.BACKWARD, validator);
        animal2.move(MoveDirection.FORWARD, validator);

        //then
        assertEquals(MapDirection.NORTH, animal1.getOrientation());
        assertEquals(new Vector2d(2, 1), animal1.getPosition());

        assertEquals(MapDirection.SOUTH, animal2.getOrientation());
        assertEquals(new Vector2d(2, 1), animal2.getPosition());
    }

    @Test
    void animalShouldNotMoveForwardWhenValidatorReturnFalse() {
        //given
        var animal = Animal.builder()
                .orientation(MapDirection.NORTH)
                .position(new Vector2d(2, 2))
                .build();
        when(validator.canMoveTo(any())).thenReturn(false);

        //when
        animal.move(MoveDirection.FORWARD, validator);

        //then
        assertEquals(MapDirection.NORTH, animal.getOrientation());
        assertEquals(new Vector2d(2, 2), animal.getPosition());
    }

    @Test
    void animalShouldNotMoveBackwardWhenValidatorReturnFalse() {
        //given
        var animal = Animal.builder()
                .orientation(MapDirection.NORTH)
                .position(new Vector2d(2, 2))
                .build();
        when(validator.canMoveTo(any())).thenReturn(false);

        //when
        animal.move(MoveDirection.BACKWARD, validator);

        //then
        assertEquals(MapDirection.NORTH, animal.getOrientation());
        assertEquals(new Vector2d(2, 2), animal.getPosition());
    }

    @Test
    void animalShouldNotMoveAccordingToGenome() {
        //given
        var genome = new Genome(List.of(new Gen(0)));
        var animal = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.NORTH)
                .genome(genome)
                .build();
        when(validator.canMoveTo(any())).thenReturn(true);

        //when
        animal.move(validator);

        //then
        assertEquals(new Vector2d(2, 3), animal.getPosition());
        assertEquals(MapDirection.NORTH, animal.getOrientation());
    }

    @Test
    void animalShouldMakeMovesAccordingToGenome() {
        //given
        var genome = new Genome(List.of(
                new Gen(0), new Gen(1), new Gen(2), new Gen(3),
                new Gen(4), new Gen(5), new Gen(6), new Gen(7)));

        var animal = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.NORTH)
                .genome(genome)
                .build();

        when(validator.canMoveTo(any())).thenReturn(true);

        //when & then
        animal.move(validator); // 0 degrees
        assertEquals(new Vector2d(2, 3), animal.getPosition());
        assertEquals(MapDirection.NORTH, animal.getOrientation());

        animal.move(validator); // 45 degrees
        assertEquals(new Vector2d(3, 4), animal.getPosition());
        assertEquals(MapDirection.NORTH_EAST, animal.getOrientation());

        animal.move(validator); // 90 degrees
        assertEquals(new Vector2d(4, 3), animal.getPosition());
        assertEquals(MapDirection.SOUTH_EAST, animal.getOrientation());

        animal.move(validator); // 135 degrees
        assertEquals(new Vector2d(3, 3), animal.getPosition());
        assertEquals(MapDirection.WEST, animal.getOrientation());

        animal.move(validator); // 180 degrees
        assertEquals(new Vector2d(4, 3), animal.getPosition());
        assertEquals(MapDirection.EAST, animal.getOrientation());

        animal.move(validator); // 225 degrees
        assertEquals(new Vector2d(3, 4), animal.getPosition());
        assertEquals(MapDirection.NORTH_WEST, animal.getOrientation());

        animal.move(validator); // 270 degrees
        assertEquals(new Vector2d(2, 3), animal.getPosition());
        assertEquals(MapDirection.SOUTH_WEST, animal.getOrientation());

        animal.move(validator); // 315 degrees
        assertEquals(new Vector2d(2, 2), animal.getPosition());
        assertEquals(MapDirection.SOUTH, animal.getOrientation());

        animal.move(validator); // 360 degrees
        assertEquals(new Vector2d(2, 1), animal.getPosition());
        assertEquals(MapDirection.SOUTH, animal.getOrientation());
    }

    @Test
    void animalIsDeadTest() {
        //given
        var animal = Animal.builder()
                .energy(20)
                .position(new Vector2d(2, 2))
                .build();

        //when & then
        assertFalse(animal.isDead());

        animal.decreaseEnergy(20);
        assertTrue(animal.isDead());
    }

    @Test
    void animalIsWellFeedTest() {
        //given
        var animal = Animal.builder()
                .energy(10)
                .position(new Vector2d(1, 2))
                .wellFedEnergy(5)
                .build();

        //when & then
        assertTrue(animal.canMakeChild());

        animal.decreaseEnergy(5);
        assertTrue(animal.canMakeChild());

        animal.decreaseEnergy(3);
        assertFalse(animal.canMakeChild());
    }


    @Test
    void getPartOfGensShouldReturnLeftPart() {
        //given
        var genome = new Genome(List.of(
                new Gen(0), new Gen(1), new Gen(2), new Gen(3),
                new Gen(4), new Gen(5), new Gen(6), new Gen(7)));

        var animal = Animal.builder()
                .genome(genome)
                .build();
        var count = 5;

        //when
        var expectedGens = List.of(new Gen(0), new Gen(1), new Gen(2), new Gen(3), new Gen(4));
        List<Gen> gensForChild = null;
        try {
            gensForChild = animal.getPartOfGens(count, true);
        } catch (InvalidCountException e) {
            fail("Should not throw exception in test, e=" + e.getMessage());
        }

        //then
        assertEquals(count, gensForChild.size());
        for (int i = 0; i < count; i++) {
            assertEquals(expectedGens.get(i).getIndex(), gensForChild.get(i).getIndex());
        }
    }

    @Test
    void getPartOfGensShouldReturnRightPart() {
        //given
        var genome = new Genome(List.of(
                new Gen(0), new Gen(1), new Gen(2), new Gen(3),
                new Gen(4), new Gen(5), new Gen(6), new Gen(7)));

        var animal = Animal.builder()
                .genome(genome)
                .build();
        var count = 5;

        //when
        List<Gen> gensForChild = null;
        try {
            gensForChild = animal.getPartOfGens(count, false);
        } catch (InvalidCountException e) {
            fail("Should not throw exception in test, e=" + e.getMessage());
        }

        //then
        var expectedGens = List.of(new Gen(3), new Gen(4), new Gen(5), new Gen(6), new Gen(7));
        assertEquals(count, gensForChild.size());

        for (int i = 0; i < count; i++) {
            assertEquals(expectedGens.get(i).getIndex(), gensForChild.get(i).getIndex());
        }
    }

    @Test
    void getPartOfGensShouldReturnEmptyList() {
        //given
        var genome1 = new Genome(List.of(new Gen(0)));
        var animal1 = Animal.builder()
                .genome(genome1)
                .build();

        var genome2 = new Genome(new ArrayList<>());
        var animal2 = Animal.builder()
                .genome(genome2)
                .build();

        //when & then
        try {
            assertEquals(0, animal1.getPartOfGens(0, true).size());
            assertEquals(0, animal1.getPartOfGens(0, false).size());
            assertEquals(0, animal2.getPartOfGens(0, true).size());
            assertEquals(0, animal2.getPartOfGens(0, false).size());
        } catch (InvalidCountException e) {
            fail("Should not throw exception in test, e=" + e.getMessage());
        }
    }

    @Test
    void getGensForChildShouldReturnAllPartOfGens() {
        //given
        var genome = new Genome(List.of(new Gen(0), new Gen(1), new Gen(2), new Gen(3)));
        var animal = Animal.builder()
                .genome(genome)
                .build();

        //when
        List<Gen> gensLeft;
        List<Gen> gensRight;
        try {
            gensLeft = animal.getPartOfGens(10, true);
            gensRight = animal.getPartOfGens(10, false);
        } catch (InvalidCountException e) {
            throw new RuntimeException(e);
        }

        //then
        assertEquals(4, gensLeft.size());
        assertEquals(4, gensRight.size());
        for (int i = 0; i < 4; i++) {
            assertEquals(i, gensLeft.get(i).getIndex());
        }

        for (int i = 0; i < 4; i++) {
            assertEquals(i, gensRight.get(i).getIndex());
        }
    }


    @Test
    void getPartOfGensShouldThrowException() {
        //given
        var genome = new Genome(List.of(new Gen(0), new Gen(1), new Gen(2), new Gen(3)));
        var animal = Animal.builder()
                .genome(genome)
                .build();

        //when & then
        assertThrows(InvalidCountException.class, () -> animal.getPartOfGens(-1, true));
        assertThrows(InvalidCountException.class, () -> animal.getPartOfGens(-5, true));
        assertThrows(InvalidCountException.class, () -> animal.getPartOfGens(-10, false));
        assertThrows(InvalidCountException.class, () -> animal.getPartOfGens(-1, false));
    }

    @Test
    void getDescendants() {
        // given

        var genome = new Genome(List.of(new Gen(0), new Gen(1), new Gen(2), new Gen(3)));
        var position = new Vector2d(2, 2);
        var animal1 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.NORTH)
                .genome(genome)
                .build();
        var animal2 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.NORTH_EAST)
                .genome(genome)
                .build();

        var animal3 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.EAST)
                .genome(genome)
                .build();

        var animal4 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.SOUTH_EAST)
                .genome(genome)
                .parents(Set.of(animal1, animal2))
                .startDay(1)
                .build();
        animal1.addChild(animal4);
        animal2.addChild(animal4);

        var animal5 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.SOUTH)
                .genome(genome)
                .parents(Set.of(animal2, animal3))
                .startDay(1)
                .build();

        animal2.addChild(animal5);
        animal3.addChild(animal5);


        var animal6 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.SOUTH_WEST)
                .genome(genome)
                .parents(Set.of(animal4, animal5))
                .startDay(1)
                .build();

        animal4.addChild(animal6);
        animal5.addChild(animal6);

        var expectedDescendantsOfAnimal5 = Set.of(animal6);
        var expectedDescendantsOfAnimal4 = Set.of(animal6);
        var expectedDescendantsOfAnimal3 = Set.of(animal6, animal5);
        var expectedDescendantsOfAnimal2 = Set.of(animal6, animal5, animal4);
        var expectedDescendantsOfAnimal1 = Set.of(animal6, animal4);

        // when

        var descendantsOfAnimal6 = animal6.getDescendants();
        var descendantsOfAnimal5 = animal5.getDescendants();
        var descendantsOfAnimal4 = animal4.getDescendants();
        var descendantsOfAnimal3 = animal3.getDescendants();
        var descendantsOfAnimal2 = animal2.getDescendants();
        var descendantsOfAnimal1 = animal1.getDescendants();

        // then

        assertTrue(descendantsOfAnimal6.isEmpty());
        assertEquals(expectedDescendantsOfAnimal5, descendantsOfAnimal5);
        assertEquals(expectedDescendantsOfAnimal4, descendantsOfAnimal4);
        assertEquals(expectedDescendantsOfAnimal3, descendantsOfAnimal3);
        assertEquals(expectedDescendantsOfAnimal2, descendantsOfAnimal2);
        assertEquals(expectedDescendantsOfAnimal1, descendantsOfAnimal1);

    }

    @Test
    void getParents() {
        // given
        var genome = new Genome(List.of(new Gen(0), new Gen(1), new Gen(2)));
        var position = new Vector2d(2, 2);
        var animal1 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.NORTH)
                .genome(genome)
                .build();
        var animal2 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.NORTH_EAST)
                .genome(genome)
                .build();

        // when
        var animal3 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.EAST)
                .genome(genome)
                .parents(Set.of(animal1, animal2))
                .build();

        // then
        assertEquals(Set.of(animal1, animal2), animal3.getParents());
    }

    @Test
    void getChildren() {
        var genome = new Genome(List.of(new Gen(0), new Gen(1), new Gen(2)));
        var position = new Vector2d(2, 2);
        var animal1 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.NORTH)
                .genome(genome)
                .build();
        var animal2 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.NORTH_EAST)
                .genome(genome)
                .build();

        var animal3 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.EAST)
                .genome(genome)
                .parents(Set.of(animal1, animal2))
                .build();

        var animal4 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.WEST)
                .genome(genome)
                .parents(Set.of(animal1, animal2))
                .build();

        // when
        animal1.addChild(animal3);
        animal1.addChild(animal4);
        animal2.addChild(animal3);
        animal2.addChild(animal4);

        // then
        assertEquals(Set.of(animal3, animal4), animal1.getChildren());
        assertEquals(Set.of(animal3, animal4), animal2.getChildren());
        assertTrue(animal3.getChildren().isEmpty());
        assertTrue(animal4.getChildren().isEmpty());
    }

    @Test
    void getCountOfChildren() {
        var genome = new Genome(List.of(new Gen(0), new Gen(1), new Gen(2)));
        var position = new Vector2d(2, 2);
        var animal1 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.NORTH)
                .genome(genome)
                .build();
        var animal2 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.NORTH_EAST)
                .genome(genome)
                .build();

        var animal3 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.EAST)
                .genome(genome)
                .parents(Set.of(animal1, animal2))
                .build();

        var animal4 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.WEST)
                .genome(genome)
                .parents(Set.of(animal1, animal2))
                .build();
        animal1.addChild(animal3);
        animal1.addChild(animal4);
        animal2.addChild(animal3);
        animal2.addChild(animal4);

        // when & then
        assertEquals(2, animal1.getCountOfChildren());
        assertEquals(2, animal2.getCountOfChildren());
        assertEquals(0, animal3.getCountOfChildren());
        assertEquals(0, animal4.getCountOfChildren());
    }

    @Test
    void getCountOfDescendants(){
        // given

        var genome = new Genome(List.of(new Gen(0), new Gen(1), new Gen(2), new Gen(3)));
        var position = new Vector2d(2, 2);
        var animal1 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.NORTH)
                .genome(genome)
                .build();
        var animal2 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.NORTH_EAST)
                .genome(genome)
                .build();

        var animal3 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.EAST)
                .genome(genome)
                .build();

        var animal4 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.SOUTH_EAST)
                .genome(genome)
                .parents(Set.of(animal1, animal2))
                .startDay(1)
                .build();
        animal1.addChild(animal4);
        animal2.addChild(animal4);

        var animal5 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.SOUTH)
                .genome(genome)
                .parents(Set.of(animal2, animal3))
                .startDay(1)
                .build();

        animal2.addChild(animal5);
        animal3.addChild(animal5);


        var animal6 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.SOUTH_WEST)
                .genome(genome)
                .parents(Set.of(animal4, animal5))
                .startDay(1)
                .build();

        animal4.addChild(animal6);
        animal5.addChild(animal6);

        // when & then

        assertEquals(0, animal6.getCountOfDescendants());
        assertEquals(1, animal5.getCountOfDescendants());
        assertEquals(1, animal4.getCountOfDescendants());
        assertEquals(2, animal3.getCountOfDescendants());
        assertEquals(3, animal2.getCountOfDescendants());
        assertEquals(2, animal1.getCountOfDescendants());
    }

}