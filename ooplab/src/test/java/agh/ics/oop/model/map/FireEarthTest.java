package agh.ics.oop.model.map;

import agh.ics.oop.TestAnimalBuilder;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.PlantVariant;
import agh.ics.oop.model.elements.*;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.exceptions.PositionOccupiedByWorldElementException;
import agh.ics.oop.model.map.fire.FireEarth;
import agh.ics.oop.model.move.MoveDirection;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FireEarthTest {

    @Test
    void placePlantShouldSucceed() {
        // given
        var plant = new Plant(new Vector2d(0, 0));
        var fireEarth = new FireEarth(1, 1, 1, 10, 5, 0, 5, PlantVariant.FORESTED_EQUATORS);

        //when & then
        try {
            fireEarth.placePlant(plant);
        } catch (IncorrectPositionException e) {
            fail("Place plant should not throw exception: " + e.getMessage());
        }

        assertTrue(fireEarth.isPlantAtPosition(new Vector2d(0, 0)));
    }

    @Test
    void placePlantShouldThrowExceptionWhenPlacePlantAtFire() {
        // given
        var plant = new Plant(new Vector2d(0, 0));
        var fireEarth = new FireEarth(1, 1, 1, 10, 5, 0, 5, PlantVariant.FORESTED_EQUATORS);
        try {
            fireEarth.placePlant(plant);
            assertTrue(fireEarth.isPlantAtPosition(new Vector2d(0, 0)));
        } catch (IncorrectPositionException e) {
            fail("Place plant should not throw exception: " + e.getMessage());
        }

        fireEarth.handleDayEnds(0);

        // when & then
        assertThrows(PositionOccupiedByWorldElementException.class,
                () -> fireEarth.placePlant(new Plant(new Vector2d(0, 0))));
    }


    @Test
    void placeFireShouldSucceed() {
        //given
        var fireEarth = new FireEarth(10, 10, 2, 5, 5, 0, 5, PlantVariant.FORESTED_EQUATORS);
        var position = new Vector2d(5, 5);

        try {
            fireEarth.placePlant(new Plant(position));
        } catch (IncorrectPositionException e) {
            fail("Place plant should not throw exception: " + e.getMessage());
        }

        //when
        var result = fireEarth.placeFire(new Fire(position, 5));

        assertTrue(result);
        assertFalse(fireEarth.isPlantAtPosition(position));
        assertTrue(fireEarth.isFireAtPosition(position));
    }


    @Test
    void placeFireShouldFail() {
        //given
        var fireEarth = new FireEarth(10, 10, 2, 5, 5, 0, 5, PlantVariant.FORESTED_EQUATORS);
        var fireOutsideMap = new Fire(new Vector2d(-1, 10), 2);
        var fireNotAtPlant = new Fire(new Vector2d(2, 2), 2);

        //when
        var result1 = fireEarth.placeFire(fireOutsideMap);
        var result2 = fireEarth.placeFire(fireNotAtPlant);

        assertFalse(result1);
        assertFalse(result2);
        assertFalse(fireEarth.isPlantAtPosition(new Vector2d(-1, 10)));
        assertFalse(fireEarth.isFireAtPosition(new Vector2d(2, 2)));
    }

    @Test
    void canPlaceFireShouldReturnTrue() {
        //given
        var fireEarth = new FireEarth(10, 10, 2, 5, 5, 0, 5, PlantVariant.FORESTED_EQUATORS);
        var position = new Vector2d(5, 5);

        try {
            fireEarth.placePlant(new Plant(position));
        } catch (IncorrectPositionException e) {
            fail("Place plant should not throw exception: " + e.getMessage());
        }

        //when
        var result = fireEarth.canPlaceFire(position);

        //then
        assertTrue(result);
    }

    @Test
    void canPLaceFireShouldReturnFalseWhenPlaceNotAtPlant() {
        //given
        var fireEarth = new FireEarth(10, 10, 2, 5, 5, 0, 5, PlantVariant.FORESTED_EQUATORS);
        var position = new Vector2d(5, 5);

        try {
            fireEarth.placePlant(new Plant(position));
            fireEarth.placeFire(new Fire(position, 5));
        } catch (IncorrectPositionException e) {
            fail("Place plant should not throw exception: " + e.getMessage());

            //when
            var result = fireEarth.canPlaceFire(position);

            //then
            assertFalse(result);
            assertTrue(fireEarth.isFireAtPosition(position));
        }
    }


    @Test
    void canPLaceFireShouldReturnFalseWhenPlaceAtOtherFire() {
        //given
        var fireEarth = new FireEarth(10, 10, 2, 5, 5, 0, 5, PlantVariant.FORESTED_EQUATORS);
        var position = new Vector2d(5, 5);
        //when
        var result = fireEarth.canPlaceFire(position);

        //then
        assertFalse(result);
    }


    @Test
    void canPLaceFireShouldReturnFalseWhenOutsideMap() {
        //given
        var fireEarth = new FireEarth(10, 10, 2, 5, 5, 0, 5, PlantVariant.FORESTED_EQUATORS);

        //when & then
        assertFalse(fireEarth.canPlaceFire(new Vector2d(-1, -1)));
        assertFalse(fireEarth.canPlaceFire(new Vector2d(-1, 5)));
        assertFalse(fireEarth.canPlaceFire(new Vector2d(5, -1)));
        assertFalse(fireEarth.canPlaceFire(new Vector2d(11, 11)));
        assertFalse(fireEarth.canPlaceFire(new Vector2d(11, 2)));
        assertFalse(fireEarth.canPlaceFire(new Vector2d(1, 11)));
    }


    @Test
    void handleDayEndsShouldCreateNewFire() {
        // given
        var fireEarth = new FireEarth(10, 10, 1, 10, 0, 0, 5, PlantVariant.FORESTED_EQUATORS);
        var animal = TestAnimalBuilder.create()
                .position(new Vector2d(8, 8))
                .build();

        try {
            fireEarth.place(animal);
            fireEarth.placePlant(new Plant(new Vector2d(0, 0)));
            fireEarth.placePlant(new Plant(new Vector2d(3, 3)));
        } catch (IncorrectPositionException e) {
            fail("Place element should not throw exception: " + e.getMessage());
        }


        //when & then
        fireEarth.handleDayEnds(0);
        var elementsAtMap = fireEarth.getElements();

        var animalCount = 0;
        var plantCount = 0;
        var fireCount = 0;

        for (var element : elementsAtMap) {
            if (element instanceof Plant) {
                plantCount++;
            } else if (element instanceof Fire) {
                fireCount++;
            } else if (element instanceof Animal) {
                animalCount++;
            }
        }

        assertEquals(1, animalCount);
        assertEquals(1, plantCount);
        assertEquals(1, fireCount);
        assertTrue(fireEarth.isFireAtPosition(new Vector2d(0, 0)) ||
                fireEarth.isFireAtPosition(new Vector2d(3, 3)));
    }


    @Test
    void handleDayEndsShouldSpreadFire() {
        // given
        var fireEarth = new FireEarth(10, 10, 1, 2, 0, 0, 5, PlantVariant.FORESTED_EQUATORS);
        var fireCenter = new Vector2d(3, 3);

        try {
            fireEarth.placePlant(new Plant(fireCenter));
            fireEarth.placePlant(new Plant(fireCenter.add(new Vector2d(1, 0))));
            fireEarth.placePlant(new Plant(fireCenter.add(new Vector2d(-1, 0))));
            fireEarth.placePlant(new Plant(fireCenter.add(new Vector2d(0, 1))));
            fireEarth.placePlant(new Plant(fireCenter.add(new Vector2d(0, -1))));
        } catch (IncorrectPositionException e) {
            fail("Place plant should not throw exception: " + e.getMessage());
        }

        fireEarth.placeFire(new Fire(fireCenter, 5));


        //when & then
        fireEarth.handleDayEnds(1);

        var elementsAtMap = fireEarth.getElements();
        var plantCount = 0;
        var fireCount = 0;

        for (var element : elementsAtMap) {
            if (element instanceof Plant) {
                plantCount++;
            } else if (element instanceof Fire) {
                fireCount++;
            }
        }

        assertEquals(0, plantCount);
        assertEquals(5, fireCount);
        assertTrue(fireEarth.isFireAtPosition(new Vector2d(4, 3)));
        assertTrue(fireEarth.isFireAtPosition(new Vector2d(3, 4)));
        assertTrue(fireEarth.isFireAtPosition(new Vector2d(2, 3)));
        assertTrue(fireEarth.isFireAtPosition(new Vector2d(3, 2)));
        assertTrue(fireEarth.isFireAtPosition(new Vector2d(3, 3)));
    }


    @Test
    void handleDayEndsShouldBurnRemoveFires() {
        //given
        var fireEarth = new FireEarth(10, 10, 1, 2, 0, 0, 5, PlantVariant.FORESTED_EQUATORS);

        try {
            fireEarth.placePlant(new Plant(new Vector2d(3, 3)));
            fireEarth.placeFire(new Fire(new Vector2d(3, 3), 1));
        } catch (IncorrectPositionException e) {
            fail("Place plant should not throw exception: " + e.getMessage());
        }

        //when & then
        assertTrue(fireEarth.isFireAtPosition(new Vector2d(3, 3)));
        assertFalse(fireEarth.isPlantAtPosition(new Vector2d(3, 3)));
        fireEarth.handleDayEnds(1);
        fireEarth.handleDayEnds(2);
        assertFalse(fireEarth.isFireAtPosition(new Vector2d(3, 3)));
        assertFalse(fireEarth.isPlantAtPosition(new Vector2d(3, 3)));

        var elementsAtMap = fireEarth.getElements();
        var plantCount = 0;
        var fireCount = 0;

        for (var element : elementsAtMap) {
            if (element instanceof Plant) {
                plantCount++;
            } else if (element instanceof Fire) {
                fireCount++;
            }
        }

        assertEquals(0, plantCount);
        assertEquals(0, fireCount);
    }


    @Test
    void animalStepOnFireShouldKill() {
        //given
        var fireEarth = new FireEarth(10, 10, 1, 2, 0, 0, 5, PlantVariant.FORESTED_EQUATORS);
        var animal = TestAnimalBuilder.create()
                .position(new Vector2d(2, 3))
                .genome(new Genome(List.of(new Gen(4))))
                .energy(10)
                .orientation(MapDirection.NORTH)
                .build();
        var plant = new Plant(new Vector2d(2, 2));
        var fire = new Fire(new Vector2d(2, 2), 2);

        try {
            fireEarth.place(animal);
            fireEarth.placePlant(plant);
            fireEarth.placeFire(fire);
        } catch (IncorrectPositionException e) {
            fail("Placing element not throw exception");
        }

        //when
        fireEarth.move(animal, MoveDirection.FORWARD);

        //then
        assertTrue(fireEarth.isFireAtPosition(new Vector2d(2, 2)));
        assertFalse(fireEarth.isPlantAtPosition(new Vector2d(2, 2)));
        assertTrue(animal.isDead());
    }

}