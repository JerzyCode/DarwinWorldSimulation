package agh.ics.oop.model.elements;

import agh.ics.oop.TestAnimalBuilder;
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
        var animal = TestAnimalBuilder.create()
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
        var animal = TestAnimalBuilder.create()
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
        var animal = TestAnimalBuilder.create()
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
        var animal = TestAnimalBuilder.create()
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
        var animal = TestAnimalBuilder.create()
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
        var animal = TestAnimalBuilder.create()
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
        var animal = TestAnimalBuilder.create()
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
        var animal = TestAnimalBuilder.create()
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
        var animal1 = TestAnimalBuilder.create()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.EAST)
                .build();
        var animal2 = TestAnimalBuilder.create()
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
        var animal1 = TestAnimalBuilder.create()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.EAST)
                .build();
        var animal2 = TestAnimalBuilder.create()
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
        var animal1 = TestAnimalBuilder.create()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.NORTH)
                .build();
        var animal2 = TestAnimalBuilder.create()
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
        var animal1 = TestAnimalBuilder.create()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.NORTH)
                .build();
        var animal2 = TestAnimalBuilder.create()
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
        var animal = TestAnimalBuilder.create()
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
        var animal = TestAnimalBuilder.create()
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
        var animal = TestAnimalBuilder.create()
                .position(new Vector2d(2, 2))
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

        var animal = TestAnimalBuilder.create()
                .position(new Vector2d(2, 2))
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
        var animal = TestAnimalBuilder.create()
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
        var animal = TestAnimalBuilder.create()
                .energy(10)
                .position(new Vector2d(1, 2))
                .build();
        var wellFedEnergy = 5;

        //when & then
        assertTrue(animal.canMakeChild(wellFedEnergy));

        animal.decreaseEnergy(5);
        assertTrue(animal.canMakeChild(wellFedEnergy));

        animal.decreaseEnergy(3);
        assertFalse(animal.canMakeChild(wellFedEnergy));
    }


    @Test
    void getPartOfGensShouldReturnLeftPart() {
        //given
        var genome = new Genome(List.of(
                new Gen(0), new Gen(1), new Gen(2), new Gen(3),
                new Gen(4), new Gen(5), new Gen(6), new Gen(7)));

        var animal = TestAnimalBuilder.create()
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

        var animal = TestAnimalBuilder.create()
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
        var animal1 = TestAnimalBuilder.create()
                .genome(genome1)
                .build();

        var genome2 = new Genome(new ArrayList<>());
        var animal2 = TestAnimalBuilder.create()
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
        var animal = TestAnimalBuilder.create()
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
        var animal = TestAnimalBuilder.create()
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
        var animal1 = new Animal(10, position, MapDirection.NORTH, genome);
        var animal2 = new Animal(10, position, MapDirection.NORTH_EAST, genome);
        var animal3 = new Animal(10, position, MapDirection.EAST, genome);

        var animal4 = new Animal(10, position, MapDirection.SOUTH_EAST, genome, animal1, animal2, 1);
        animal1.addChild(animal4);
        animal2.addChild(animal4);

        var animal5 = new Animal(10, position, MapDirection.SOUTH, genome, animal2, animal3, 1);
        animal2.addChild(animal5);
        animal3.addChild(animal5);

        var animal6 = new Animal(10, position, MapDirection.SOUTH_WEST, genome, animal4, animal5, 1);
        animal4.addChild(animal6);
        animal5.addChild(animal6);

        var expectedAncestorsOfAnimal5 = Set.of(animal6);
        var expectedAncestorsOfAnimal4 = Set.of(animal6);
        var expectedAncestorsOfAnimal3 = Set.of(animal6, animal5);
        var expectedAncestorsOfAnimal2 = Set.of(animal6, animal5, animal4);
        var expectedAncestorsOfAnimal1 = Set.of(animal6, animal4);

        // when

        var ancestorsOfAnimal6 = animal6.getDescendants();
        var ancestorsOfAnimal5 = animal5.getDescendants();
        var ancestorsOfAnimal4 = animal4.getDescendants();
        var ancestorsOfAnimal3 = animal3.getDescendants();
        var ancestorsOfAnimal2 = animal2.getDescendants();
        var ancestorsOfAnimal1 = animal1.getDescendants();

        // then

        assertTrue(ancestorsOfAnimal6.isEmpty());
        assertEquals(expectedAncestorsOfAnimal5, ancestorsOfAnimal5);
        assertEquals(expectedAncestorsOfAnimal4, ancestorsOfAnimal4);
        assertEquals(expectedAncestorsOfAnimal3, ancestorsOfAnimal3);
        assertEquals(expectedAncestorsOfAnimal2, ancestorsOfAnimal2);
        assertEquals(expectedAncestorsOfAnimal1, ancestorsOfAnimal1);

    }

}