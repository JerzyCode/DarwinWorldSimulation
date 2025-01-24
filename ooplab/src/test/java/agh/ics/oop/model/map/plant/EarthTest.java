package agh.ics.oop.model.map.plant;

import agh.ics.oop.factory.AnimalFactory;
import agh.ics.oop.model.AnimalBreeder;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.AnimalConfiguration;
import agh.ics.oop.model.configuration.PlantVariant;
import agh.ics.oop.model.elements.Animal;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EarthTest {
    private Earth map;
    private final AnimalBreeder breeder = mock(AnimalBreeder.class);


    @BeforeEach
    void setUp() {
        when(breeder.breed(any(), any())).thenReturn(Animal.builder()
                .position(new Vector2d(0, 0))
                .orientation(MapDirection.NORTH)
                .build());

        map = new Earth(5, 5, 5, 0, 5, PlantVariant.FORESTED_EQUATORS, breeder);
    }

    @Test
    void shouldCreateBoundary() {
        assertEquals(new Vector2d(0, 0), map.getCurrentBounds().leftBottomCorner());
        assertEquals(new Vector2d(4, 4), map.getCurrentBounds().rightTopCorner());
    }

    @Test
    void placeAnimalShouldCreateNewListAtPosition() {
        //given
        var animal = Animal.builder().position(new Vector2d(2, 2)).build();

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
        var animal1 = Animal.builder().position(new Vector2d(-1, 2)).build();
        var animal2 = Animal.builder().position(new Vector2d(0, 10)).build();
        var animal3 = Animal.builder().position(new Vector2d(15, 2)).build();
        var animal4 = Animal.builder().position(new Vector2d(3, -2)).build();

        //when && then
        assertThrows(PositionOutOfMapBoundaryException.class, () -> map.place(animal1));
        assertThrows(PositionOutOfMapBoundaryException.class, () -> map.place(animal2));
        assertThrows(PositionOutOfMapBoundaryException.class, () -> map.place(animal3));
        assertThrows(PositionOutOfMapBoundaryException.class, () -> map.place(animal4));
    }

    @Test
    void getAnimalsAtPositionShouldReturnEmptySet() {
        //given
        //when && then
        assertTrue(map.getAnimalsAtPosition(new Vector2d(0, 0)).isEmpty());
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
        assertEquals(1, map.getPlantCount());

        var elementAtPosition = map.objectAt(plantPlaced.getPosition());
        assertTrue(elementAtPosition.isPresent());
        assertInstanceOf(Plant.class, elementAtPosition.get());
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
        assertEquals(1, map.getPlantCount());
        assertFalse(map.isPlantAtPosition(plantPlaced1.getPosition()));
        assertFalse(map.isPlantAtPosition(plantPlaced2.getPosition()));
        assertTrue(map.isPlantAtPosition(plantPlaced3.getPosition()));
    }

    @Test
    void moveAnimalAdjusterShouldNoTrigger() {
        //given
        var animalN = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.NORTH)
                .genome(new Genome(List.of(new Gen(0))))
                .build();

        var animalS = Animal.builder()
                .position(new Vector2d(1, 3))
                .orientation(MapDirection.NORTH)
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
        var animalN = Animal.builder()
                .position(new Vector2d(2, 2))
                .orientation(MapDirection.NORTH)
                .genome(new Genome(List.of(new Gen(0))))
                .build();

        var animalE = Animal.builder()
                .position(new Vector2d(1, 2))
                .orientation(MapDirection.NORTH)
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
        var animalW = Animal.builder()
                .position(new Vector2d(0, 3))
                .orientation(MapDirection.NORTH)
                .genome(new Genome(List.of(new Gen(6))))
                .build();

        var animalE = Animal.builder()
                .position(new Vector2d(4, 2))
                .orientation(MapDirection.NORTH)
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

        var animalAtMap1Opt = map.objectAt(animalE.getPosition());
        assertTrue(animalAtMap1Opt.isPresent());
        assertEquals(animalE, animalAtMap1Opt.get());

        var animalAtMap2Opt = map.objectAt(animalW.getPosition());
        assertTrue(animalAtMap2Opt.isPresent());
        assertEquals(animalW, animalAtMap2Opt.get());
    }

    @Test
    void moveAnimalsWithAdjusterVertical() {
        // given
        var animalN = Animal.builder()
                .position(new Vector2d(2, 4))
                .orientation(MapDirection.NORTH)
                .genome(new Genome(List.of(new Gen(0))))
                .build();

        var animalS = Animal.builder()
                .position(new Vector2d(1, 0))
                .orientation(MapDirection.NORTH)
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

        var animalOnMapOpt1 = map.objectAt(animalN.getPosition());
        assertTrue(animalOnMapOpt1.isPresent());
        assertEquals(animalN, animalOnMapOpt1.get());


        var animalOnMapOpt2 = map.objectAt(animalS.getPosition());
        assertTrue(animalOnMapOpt2.isPresent());
        assertEquals(animalS, animalOnMapOpt2.get());
    }

    @Test
    void moveAnimalsWithAdjusterCorners() {
        // given
        var animalNE = Animal.builder()
                .position(new Vector2d(4, 4))
                .orientation(MapDirection.NORTH)
                .genome(new Genome(List.of(new Gen(1))))
                .build();

        var animalSE = Animal.builder()
                .position(new Vector2d(4, 0))
                .orientation(MapDirection.NORTH)
                .genome(new Genome(List.of(new Gen(3))))
                .build();

        var animalSW = Animal.builder()
                .position(new Vector2d(0, 0))
                .orientation(MapDirection.NORTH)
                .genome(new Genome(List.of(new Gen(5))))
                .build();

        var animalNW = Animal.builder()
                .position(new Vector2d(0, 4))
                .orientation(MapDirection.NORTH)
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
        var map1 = new Earth(4, 10, 5, 0, 5, PlantVariant.FORESTED_EQUATORS, breeder);
        var map2 = new Earth(14, 5, 5, 0, 5, PlantVariant.FORESTED_EQUATORS, breeder);


        var mapBoundary1 = map1.getCurrentBounds();
        assertEquals(3, mapBoundary1.rightTopCorner().getX());
        assertEquals(0, mapBoundary1.leftBottomCorner().getX());
        assertEquals(0, mapBoundary1.leftBottomCorner().getY());
        assertEquals(9, mapBoundary1.rightTopCorner().getY());

        var mapBoundary2 = map2.getCurrentBounds();
        assertEquals(13, mapBoundary2.rightTopCorner().getX());
        assertEquals(0, mapBoundary2.leftBottomCorner().getX());
        assertEquals(0, mapBoundary2.leftBottomCorner().getY());
        assertEquals(4, mapBoundary2.rightTopCorner().getY());
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
    void animalStepOnPlantShouldIncreaseEnergy() {
        // given
        var earth = new Earth(10, 10, 10, 0, 5, PlantVariant.FORESTED_EQUATORS, breeder);
        var plant = new Plant(new Vector2d(2, 2), 5);
        var animal = Animal.builder()
                .position(new Vector2d(2, 2))
                .genome(new Genome(List.of(new Gen(4))))
                .energy(10)
                .orientation(MapDirection.NORTH)
                .build();

        try {
            earth.place(animal);
            earth.placePlant(plant);
        } catch (IncorrectPositionException e) {
            fail("Placing element should not throw exception");
        }

        //when
        earth.handleDayEnds(2);

        //then
        assertEquals(new Vector2d(2, 2), animal.getPosition());
        assertEquals(15, animal.getEnergy());
        assertFalse(earth.isPlantAtPosition(new Vector2d(2, 2)));
    }

    @Test
    void handleDayEndsShouldGrowNewPlants() {
        // given
        var earth = new Earth(10, 10, 10, 0, 5, PlantVariant.FORESTED_EQUATORS, breeder);

        // when
        earth.handleDayEnds(1);

        //then
        assertEquals(10, earth.getElements().size());
    }


    @Test
    void animalIntegrationStepOnEachOtherShouldCopulate() {
        // given
        var animalConfiguration = AnimalConfiguration
                .builder()
                .wellFedEnergy(15)
                .genomeLength(3)
                .lossCopulateEnergy(5)
                .startEnergy(50)
                .build();

        var factory = new AnimalFactory(animalConfiguration);
        AnimalBreeder breeder = (animal1, animal2) -> factory.birthAnimal(animal1, animal2, 1);

        var parent1 = factory.createAnimal(new Vector2d(0, 3), 1);
        var parent2 = factory.createAnimal(new Vector2d(0, 3), 1);
        var earth = new Earth(10, 10, 10, 0, 5, PlantVariant.FORESTED_EQUATORS, breeder);

        try {
            earth.place(parent1);
            earth.place(parent2);
        } catch (IncorrectPositionException e) {
            fail("Placing element should not throw exception");
        }

        //when
        earth.handleDayEnds(1);

        //then
        var animalsAtPosition = earth.getAnimalsAtPosition(new Vector2d(0, 3));
        var bornAnimalOptional = animalsAtPosition.stream()
                .filter(animal -> animal != parent1 && animal != parent2)
                .findAny();

        assertTrue(bornAnimalOptional.isPresent());
        var bornAnimal = bornAnimalOptional.get();

        assertEquals(45, parent1.getEnergy());
        assertEquals(45, parent2.getEnergy());
        assertEquals(10, bornAnimal.getEnergy());
        assertTrue(animalsAtPosition.contains(parent1));
        assertTrue(animalsAtPosition.contains(parent2));
        assertTrue(animalsAtPosition.contains(bornAnimal));
        assertTrue(parent1.getChildren().contains(bornAnimal));
        assertTrue(parent2.getChildren().contains(bornAnimal));
        assertTrue(bornAnimal.getParents().contains(parent1));
        assertTrue(bornAnimal.getParents().contains(parent2));
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