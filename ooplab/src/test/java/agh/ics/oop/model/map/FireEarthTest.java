package agh.ics.oop.model.map;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Fire;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.exceptions.PositionOccupiedByFireException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FireEarthTest {
    FireEarth fireEarth;

    @BeforeEach
    void setUp() {
        fireEarth = new FireEarth(5, 5);
        try{
            fireEarth.placePlant(new Plant(new Vector2d(0, 0)));
            fireEarth.placeFire(new Fire(new Vector2d(0, 1), 3));
        } catch (PositionOccupiedByFireException | IncorrectPositionException e){
            fail(e.getMessage());
        }
    }

    @Test
    void placePlantShouldThrowsExceptionWhenGivenFieldIsBurning() {
        // given
        Plant plant = new Plant(new Vector2d(0, 1));

        // when & then
        assertThrows(IncorrectPositionException.class, () -> fireEarth.placePlant(plant));
    }

    @Test
    void placePlantShouldThrowsExceptionWhenGivenFieldIsOccupiedByOtherPlant() {
        // given
        Plant plant = new Plant(new Vector2d(0, 0));

        // when & then
        assertThrows(IncorrectPositionException.class, () -> fireEarth.placePlant(plant));
    }

    @Test
    void placePlantShouldThrowsExceptionWhenGivenFieldIsOutsideMap() {
        // given
        Plant plant = new Plant(new Vector2d(5, 4));

        // when & then
        assertThrows(IncorrectPositionException.class, () -> fireEarth.placePlant(plant));
    }



    @Test
    void placePlantWorksProperlyWhenPlantPositionIsAvailable() {
        // given
        Plant plant1 = new Plant(new Vector2d(1, 1));
        Plant plant2 = new Plant(new Vector2d(4, 4));
        Plant plant3 = new Plant(new Vector2d(3, 2));

        // when
        try {
            fireEarth.placePlant(plant1);
            fireEarth.placePlant(plant2);
            fireEarth.placePlant(plant3);
        } catch (IncorrectPositionException e){
            fail(e.getMessage());
        }

        // then
        assertEquals(plant1, fireEarth.getPlantAtPosition(plant1.getPosition()));
        assertEquals(plant2, fireEarth.getPlantAtPosition(plant2.getPosition()));
        assertEquals(plant3, fireEarth.getPlantAtPosition(plant3.getPosition()));
    }

    @Test
    void getElements() {
        // given
        Plant plant1 = new Plant(new Vector2d(1, 1));
        Plant plant2 = new Plant(new Vector2d(4, 4));
        Plant plant3 = new Plant(new Vector2d(3, 2));
        Fire fire1 = new Fire(new Vector2d(2, 2), 3);
        Fire fire2 = new Fire(new Vector2d(3, 3), 3);

        try {
            fireEarth.placePlant(plant1);
            fireEarth.placePlant(plant2);
            fireEarth.placePlant(plant3);
            fireEarth.placeFire(fire1);
            fireEarth.placeFire(fire2);
        } catch (IncorrectPositionException | PositionOccupiedByFireException e){
            fail(e.getMessage());
        }

        // when
        var elements = fireEarth.getElements();
        var fires = elements.stream()
                .filter(fire -> fire instanceof Fire)
                .toList();
        var plants = elements.stream()
                .filter(plant -> plant instanceof Plant)
                .toList();

        // then
        assertEquals(3, fires.size());
        assertEquals(4, plants.size());
        assertTrue(fires.contains(fire1));
        assertTrue(fires.contains(fire2));
        assertTrue(plants.contains(plant1));
        assertTrue(plants.contains(plant2));
        assertTrue(plants.contains(plant3));
    }

    @Test
    void isFireAtPosition() {
        // given
        var positionWithFire = new Vector2d(0, 1);
        var positionWithoutFire = new Vector2d(0, 0);
        var positionWithoutFire2 = new Vector2d(1, 0);

        // when
        // then
        assertTrue(fireEarth.isFireAtPosition(positionWithFire));
        assertFalse(fireEarth.isFireAtPosition(positionWithoutFire));
        assertFalse(fireEarth.isFireAtPosition(positionWithoutFire2));

    }

    @Test
    void placeFireShouldThrowsExceptionWhenPositionIsOccupiedByPlant() {
        // given
        var fire = new Fire(new Vector2d(0, 0), 3);
        // when & then
        assertThrows(IncorrectPositionException.class, () -> fireEarth.placeFire(fire));

    }

    @Test
    void placeFireShouldThrowsExceptionWhenPositionIsOccupiedByOtherFire() {
        // given
        var fire = new Fire(new Vector2d(0, 1), 3);
        // when & then
        assertThrows(PositionOccupiedByFireException.class, () -> fireEarth.placeFire(fire));

    }

    @Test
    void placeFireShouldThrowsExceptionWhenPositionIsOutsideMap() {
        // given
        var fire = new Fire(new Vector2d(1, 5), 3);

        // when & then
        assertThrows(IncorrectPositionException.class, () -> fireEarth.placeFire(fire));
    }

    @Test
    void getFireAtPosition() {
        // given
        var firePosition = new Vector2d(3, 3);
        var fire = new Fire(firePosition, 1);
        try{
            fireEarth.placeFire(fire);
        } catch (IncorrectPositionException | PositionOccupiedByFireException e){
            fail(e.getMessage());
        }

        // when
        var fireByPosition = fireEarth.getFireAtPosition(firePosition);

        // then
        assertEquals(fire, fireByPosition);
    }

    @Test
    void decreaseFireRemainingLifetime() {
        // given
        var firePosition = new Vector2d(3, 3);
        var fire = new Fire(firePosition, 1);
        try{
            fireEarth.placeFire(fire);
        } catch (IncorrectPositionException | PositionOccupiedByFireException e){
            fail(e.getMessage());
        }

        // when
        fireEarth.decreaseFireRemainingLifetime();

        // then
        assertTrue(fire.isBurned());
        assertFalse(fireEarth.getFireAtPosition(new Vector2d(0, 1)).isBurned());

    }

    @Test
    void removeFire() {
        // given
        var fire = fireEarth.getFireAtPosition(new Vector2d(0, 1));

        // when
        fireEarth.removeFire(fire);

        // then
        assertNull(fireEarth.getFireAtPosition(new Vector2d(0, 1)));

    }

    @Test
    void getBurnedFires() {
        // given
        var fire1 = new Fire(new Vector2d(0, 2), 1);
        var burnedFire1 = new Fire(new Vector2d(0, 3), 0);
        var burnedFire2 = new Fire(new Vector2d(0, 4), 0);
        try{
            fireEarth.placeFire(fire1);
            fireEarth.placeFire(burnedFire1);
            fireEarth.placeFire(burnedFire2);
        } catch (IncorrectPositionException | PositionOccupiedByFireException e){
            fail(e.getMessage());
        }

        // when
        var burnedFires = fireEarth.getBurnedFires();

        // then
        assertEquals(2, burnedFires.size());
        assertTrue(burnedFires.contains(burnedFire1));
        assertTrue(burnedFires.contains(burnedFire2));

    }
}