package agh.ics.oop.model.map.plant;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.PlantVariant;
import agh.ics.oop.model.elements.Plant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GardenerTest {

    private Gardener gardenerForestedEquators;
    private Gardener gardenerFullRandom;
    private Boundary boundary;

    @BeforeEach
    void setUp() {
        gardenerForestedEquators = new Gardener(PlantVariant.FORESTED_EQUATORS, 10, 5);
        gardenerFullRandom = new Gardener(PlantVariant.FULL_RANDOM, 10, 5);

        boundary = new Boundary(new Vector2d(0, 0), new Vector2d(9, 9));
    }

    @Test
    void testCreatePlantsDaily_LimitedAvailableSpace() {
        // Given
        Set<Vector2d> unavailablePositions = new HashSet<>();
        unavailablePositions.add(new Vector2d(1, 1));
        Boundary boundary2 = new Boundary(new Vector2d(0, 0), new Vector2d(0, 0));
        Set<Vector2d> unavailablePositions2 = new HashSet<>();
        unavailablePositions2.add(new Vector2d(0, 0));


        // when
        Set<Plant> plants = gardenerForestedEquators.createPlantsDaily(91, 100, boundary, unavailablePositions);
        Set<Plant> plants2 = gardenerForestedEquators.createPlantsDaily(90, 100, boundary2, unavailablePositions2);

        // then
        assertEquals(9, plants.size(), "Expected exactly 9 plants to be created.");
        assertEquals(0, plants2.size(), "Expected exactly 0 plants to be created.");
    }

    @Test
    void testCreatePlantsDaily_AllPositionsUnavailable() {
        // given
        Set<Vector2d> unavailablePositions = new HashSet<>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                unavailablePositions.add(new Vector2d(x, y));
            }
        }

        // when
        Set<Plant> plants = gardenerForestedEquators.createPlantsDaily(90, 100, boundary, unavailablePositions);

        // then
        assertTrue(plants.isEmpty(), "No plants should be created when all positions are unavailable.");
    }

    @Test
    void testCreatePlantsDaily_FullRandom() {
        // given
        Set<Vector2d> unavailablePositions = new HashSet<>();

        // when
        Set<Plant> plants = gardenerFullRandom.createPlantsDaily(90, 100, boundary, unavailablePositions);

        // then
        assertEquals(10, plants.size(), "Expected 10 plants to be created in FULL_RANDOM variant.");
        assertTrue(plants.stream().allMatch(plant -> plant.getPosition().getX() >= 0 && plant.getPosition().getY() >= 0),
                "All plant positions should be within the boundary.");
    }

    @Test
    void testCreatePlants_ExceedAvailableSpace() {
        // given
        Set<Vector2d> unavailablePositions = new HashSet<>();

        // when
        Set<Plant> plants = gardenerForestedEquators.createPlants(95, 100, boundary, 10, unavailablePositions);

        // then
        assertEquals(5, plants.size(), "Only 5 plants should be created due to space limitations.");
    }


    @Test
    void testRandomPositionGeneration_ForestedEquators() {
        // given
        Set<Vector2d> unavailablePositions = new HashSet<>();
        unavailablePositions.add(new Vector2d(0, 0));

        // when
        Set<Plant> plants = gardenerForestedEquators.createPlants(0, 100, boundary, 10, unavailablePositions);

        // then
        assertEquals(10, plants.size(), "10 plants should be created in FORESTED_EQUATORS variant.");
        assertTrue(plants.stream().allMatch(plant -> plant.getPosition().getX() >= 0 && plant.getPosition().getY() >= 0),
                "All plant positions should be within the boundary.");
    }

    @Test
    void testRandomPositionGeneration_FullRandom() {
        // given
        Set<Vector2d> unavailablePositions = new HashSet<>();

        // when
        Set<Plant> plants = gardenerFullRandom.createPlants(0, 100, boundary, 10, unavailablePositions);

        // then
        assertEquals(10, plants.size(), "10 plants should be created in FULL_RANDOM variant.");
    }
}