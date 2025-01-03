package agh.ics.oop.model.elements;

import agh.ics.oop.TestAnimalBuilder;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.move.MoveDirection;
import agh.ics.oop.model.move.MoveValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

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

}