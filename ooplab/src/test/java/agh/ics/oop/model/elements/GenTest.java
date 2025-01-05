package agh.ics.oop.model.elements;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.configuration.MutationVariant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenTest {

    @Test
    void createIndexShouldThrowException() {
        //when && then
        assertThrows(IllegalArgumentException.class, () -> new Gen(-1));
        assertThrows(IllegalArgumentException.class, () -> new Gen(-3));
        assertThrows(IllegalArgumentException.class, () -> new Gen(-4));
        assertThrows(IllegalArgumentException.class, () -> new Gen(MapDirection.values().length));
        assertThrows(IllegalArgumentException.class, () -> new Gen(MapDirection.values().length + 1));
    }

    @Test
    void rotateGenZeroIndexShouldNotRotate() {
        //given
        var gen = new Gen(0);

        //when && then
        assertEquals(MapDirection.NORTH, gen.rotate(MapDirection.NORTH));
        assertEquals(MapDirection.NORTH_EAST, gen.rotate(MapDirection.NORTH_EAST));
        assertEquals(MapDirection.EAST, gen.rotate(MapDirection.EAST));
        assertEquals(MapDirection.SOUTH_EAST, gen.rotate(MapDirection.SOUTH_EAST));
        assertEquals(MapDirection.SOUTH, gen.rotate(MapDirection.SOUTH));
        assertEquals(MapDirection.SOUTH_WEST, gen.rotate(MapDirection.SOUTH_WEST));
        assertEquals(MapDirection.WEST, gen.rotate(MapDirection.WEST));
        assertEquals(MapDirection.NORTH_WEST, gen.rotate(MapDirection.NORTH_WEST));
    }

    @Test
    void rotateGenOneIndexShouldRotate45Degrees() {
        //given
        var gen = new Gen(1);

        //when && then
        assertEquals(MapDirection.NORTH_EAST, gen.rotate(MapDirection.NORTH));
        assertEquals(MapDirection.EAST, gen.rotate(MapDirection.NORTH_EAST));
        assertEquals(MapDirection.SOUTH_EAST, gen.rotate(MapDirection.EAST));
        assertEquals(MapDirection.SOUTH, gen.rotate(MapDirection.SOUTH_EAST));
        assertEquals(MapDirection.SOUTH_WEST, gen.rotate(MapDirection.SOUTH));
        assertEquals(MapDirection.WEST, gen.rotate(MapDirection.SOUTH_WEST));
        assertEquals(MapDirection.NORTH_WEST, gen.rotate(MapDirection.WEST));
        assertEquals(MapDirection.NORTH, gen.rotate(MapDirection.NORTH_WEST));
    }

    @Test
    void rotateGenTwoIndexShouldRotate90Degrees() {
        //given
        var gen = new Gen(2);

        //when && then
        assertEquals(MapDirection.EAST, gen.rotate(MapDirection.NORTH));
        assertEquals(MapDirection.SOUTH_EAST, gen.rotate(MapDirection.NORTH_EAST));
        assertEquals(MapDirection.SOUTH, gen.rotate(MapDirection.EAST));
        assertEquals(MapDirection.SOUTH_WEST, gen.rotate(MapDirection.SOUTH_EAST));
        assertEquals(MapDirection.WEST, gen.rotate(MapDirection.SOUTH));
        assertEquals(MapDirection.NORTH_WEST, gen.rotate(MapDirection.SOUTH_WEST));
        assertEquals(MapDirection.NORTH, gen.rotate(MapDirection.WEST));
        assertEquals(MapDirection.NORTH_EAST, gen.rotate(MapDirection.NORTH_WEST));
    }

    @Test
    void rotateGenFourIndexShouldRotate180Degrees() {
        //given
        var gen = new Gen(4);

        //when && then
        assertEquals(MapDirection.SOUTH, gen.rotate(MapDirection.NORTH));
        assertEquals(MapDirection.SOUTH_WEST, gen.rotate(MapDirection.NORTH_EAST));
        assertEquals(MapDirection.WEST, gen.rotate(MapDirection.EAST));
        assertEquals(MapDirection.NORTH_WEST, gen.rotate(MapDirection.SOUTH_EAST));
        assertEquals(MapDirection.NORTH, gen.rotate(MapDirection.SOUTH));
        assertEquals(MapDirection.NORTH_EAST, gen.rotate(MapDirection.SOUTH_WEST));
        assertEquals(MapDirection.EAST, gen.rotate(MapDirection.WEST));
        assertEquals(MapDirection.SOUTH_EAST, gen.rotate(MapDirection.NORTH_WEST));
    }

    @Test
    void rotateGenFiveIndexShouldRotate225Degrees() {
        //given
        var gen = new Gen(5);

        //when && then
        assertEquals(MapDirection.SOUTH_WEST, gen.rotate(MapDirection.NORTH));
        assertEquals(MapDirection.WEST, gen.rotate(MapDirection.NORTH_EAST));
        assertEquals(MapDirection.NORTH_WEST, gen.rotate(MapDirection.EAST));
        assertEquals(MapDirection.NORTH, gen.rotate(MapDirection.SOUTH_EAST));
        assertEquals(MapDirection.NORTH_EAST, gen.rotate(MapDirection.SOUTH));
        assertEquals(MapDirection.EAST, gen.rotate(MapDirection.SOUTH_WEST));
        assertEquals(MapDirection.SOUTH_EAST, gen.rotate(MapDirection.WEST));
        assertEquals(MapDirection.SOUTH, gen.rotate(MapDirection.NORTH_WEST));
    }

    @Test
    void rotateGenSixIndexShouldRotate280Degrees() {
        //given
        var gen = new Gen(6);

        //when && then
        assertEquals(MapDirection.WEST, gen.rotate(MapDirection.NORTH));
        assertEquals(MapDirection.NORTH_WEST, gen.rotate(MapDirection.NORTH_EAST));
        assertEquals(MapDirection.NORTH, gen.rotate(MapDirection.EAST));
        assertEquals(MapDirection.NORTH_EAST, gen.rotate(MapDirection.SOUTH_EAST));
        assertEquals(MapDirection.EAST, gen.rotate(MapDirection.SOUTH));
        assertEquals(MapDirection.SOUTH_EAST, gen.rotate(MapDirection.SOUTH_WEST));
        assertEquals(MapDirection.SOUTH, gen.rotate(MapDirection.WEST));
        assertEquals(MapDirection.SOUTH_WEST, gen.rotate(MapDirection.NORTH_WEST));
    }

    @Test
    void rotateGenSevenIndexShouldRotate315Degrees() {
        //given
        var gen = new Gen(7);

        //when && then
        assertEquals(MapDirection.NORTH_WEST, gen.rotate(MapDirection.NORTH));
        assertEquals(MapDirection.NORTH, gen.rotate(MapDirection.NORTH_EAST));
        assertEquals(MapDirection.NORTH_EAST, gen.rotate(MapDirection.EAST));
        assertEquals(MapDirection.EAST, gen.rotate(MapDirection.SOUTH_EAST));
        assertEquals(MapDirection.SOUTH_EAST, gen.rotate(MapDirection.SOUTH));
        assertEquals(MapDirection.SOUTH, gen.rotate(MapDirection.SOUTH_WEST));
        assertEquals(MapDirection.SOUTH_WEST, gen.rotate(MapDirection.WEST));
        assertEquals(MapDirection.WEST, gen.rotate(MapDirection.NORTH_WEST));
    }

    @Test
    void mutateGenFullRandom() {
        for (int i = 0; i < 8; i++) {
            var gen = new Gen(i);
            gen.mutate(MutationVariant.FULL_RANDOM);
            assertNotEquals(i, gen.getIndex());
        }
    }

    @Test
    void mutateGenLittleCorrection() {
        for (int i = 0; i < 8; i++) {
            var gen = new Gen(i);
            gen.mutate(MutationVariant.LITTLE_CORRECTION);
            int mutatedIndex = gen.getIndex();

            assertNotEquals(i, mutatedIndex);
            if (i == 0) {
                assertTrue(mutatedIndex == 1 || mutatedIndex == 7);
            } else if (i == 7) {
                assertTrue(mutatedIndex == 6 || mutatedIndex == 0);
            } else {
                assertTrue(mutatedIndex == i + 1 || mutatedIndex == i - 1);
            }
        }

    }

}