package agh.ics.oop.model.statistics;

import agh.ics.oop.SimulationContext;
import agh.ics.oop.TestConfigurationHelper;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Gen;
import agh.ics.oop.model.elements.Genome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SimulationStatisticsTest {

    SimulationContext simulationWithAnimals;
    SimulationContext simulationWithoutAnimals;
    SimulationContext simulationWithoutPlantsAndFires;

    SimulationStatistics simulationWithAnimalsStatistics;
    SimulationStatistics simulationWithoutAnimalsStatistics;
    SimulationStatistics simulationWithoutPlantsAndFiresStatistics;

    @BeforeEach
    void setUp() {
        var configuration = TestConfigurationHelper.createFireConfiguration();
        var configuration2 = TestConfigurationHelper.createEarthConfigurationNoAnimals();
        var configuration3 = TestConfigurationHelper.createFireConfigurationNoPlantsNoFire();

        this.simulationWithAnimals = new SimulationContext(configuration);
        this.simulationWithoutAnimals = new SimulationContext(configuration2);
        this.simulationWithoutPlantsAndFires = new SimulationContext(configuration3);

        this.simulationWithAnimalsStatistics = new SimulationStatistics(simulationWithAnimals);
        this.simulationWithoutAnimalsStatistics = new SimulationStatistics(simulationWithoutAnimals);
        this.simulationWithoutPlantsAndFiresStatistics = new SimulationStatistics(simulationWithoutPlantsAndFires);
    }


    @Test
    void getAnimalInitCount() {
        // given & when
        var noAnimalsCount = simulationWithoutAnimalsStatistics.getAnimalCount();
        var defaultAnimalsCount = simulationWithAnimalsStatistics.getAnimalCount();
        var animalsOnMapWithoutPlantsCount = simulationWithoutPlantsAndFiresStatistics.getAnimalCount();

        // then
        assertEquals(0, noAnimalsCount);
        assertEquals(25, defaultAnimalsCount);
        assertEquals(10, animalsOnMapWithoutPlantsCount);
    }

    @Test
    void getAnimalCountShouldReturnProperCountOfAnimalsEveryday() {
        for(int i=0; i < 16; i++){
            assertEquals(10 , simulationWithoutPlantsAndFiresStatistics.getAnimalCount());
            simulationWithoutPlantsAndFires.handleDayEnds();
        }
        assertEquals(0 , simulationWithoutPlantsAndFiresStatistics.getAnimalCount());
    }

    @Test
    void getAverageAnimalEnergy() {
        for(int i=0; i < 15; i++){
            assertTrue(simulationWithoutPlantsAndFiresStatistics.getAverageAnimalEnergy().isPresent());
            assertEquals(15 - i, simulationWithoutPlantsAndFiresStatistics.getAverageAnimalEnergy().getAsDouble(), 0.01);
            simulationWithoutPlantsAndFires.handleDayEnds();
        }

        assertTrue(simulationWithoutAnimalsStatistics.getAverageAnimalEnergy().isEmpty());
    }

    @Test
    void getAverageDeadAnimalTimeLife() {

        // given & when
        for(int i=0; i < 16; i++){
            simulationWithoutPlantsAndFires.handleDayEnds();
        }
        var averageTimeLifeOnMapWithOnlyAnimals = simulationWithoutPlantsAndFiresStatistics.getAverageDeadAnimalTimeLife();

        // then
        assertTrue(averageTimeLifeOnMapWithOnlyAnimals.isPresent());
        assertEquals(15, averageTimeLifeOnMapWithOnlyAnimals.getAsDouble(), 0.01);
        simulationWithoutPlantsAndFires.handleDayEnds();
        averageTimeLifeOnMapWithOnlyAnimals = simulationWithoutPlantsAndFiresStatistics.getAverageDeadAnimalTimeLife();
        assertTrue(averageTimeLifeOnMapWithOnlyAnimals.isPresent());
        assertEquals(15, averageTimeLifeOnMapWithOnlyAnimals.getAsDouble(), 0.01);
        assertTrue(simulationWithoutAnimalsStatistics.getAverageDeadAnimalTimeLife().isEmpty());
        assertTrue(simulationWithAnimalsStatistics.getAverageDeadAnimalTimeLife().isEmpty());

    }

    @Test
    void getAverageAnimalCountOfChildren() {
        // Given
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
        var animal5 = Animal.builder()
                .energy(0)
                .position(position)
                .orientation(MapDirection.WEST)
                .genome(genome)
                .parents(Set.of(animal1, animal2))
                .build();
        animal1.addChild(animal3);
        animal1.addChild(animal4);
        animal2.addChild(animal3);
        animal2.addChild(animal4);

        SimulationContext mockSimulationContext = Mockito.mock(SimulationContext.class);
        when(mockSimulationContext.getAliveAnimals()).thenReturn(Set.of(animal1, animal2, animal3, animal4));
        when(mockSimulationContext.getDeadAnimals()).thenReturn(Set.of(animal5));
        var simulationStatistics = new SimulationStatistics(mockSimulationContext);

        SimulationContext mockSimulationContext2 = Mockito.mock(SimulationContext.class);
        when(mockSimulationContext2.getAliveAnimals()).thenReturn(Set.of());
        when(mockSimulationContext2.getDeadAnimals()).thenReturn(Set.of(animal5));
        var simulationStatistics2 = new SimulationStatistics(mockSimulationContext2);

        // when
        var average = simulationStatistics.getAverageAnimalCountOfChildren();
        var average2 = simulationStatistics2.getAverageAnimalCountOfChildren();

        // then
        assertTrue(average.isPresent());
        assertEquals(1, average.getAsDouble(), 0.001);
        assertTrue(average2.isEmpty());
    }

    @Test
    void getMostPopularGenotype() {
        // Given
        var mostPopularGenotype = List.of(new Gen(0), new Gen(1), new Gen(2));
        var popularGenome = new Genome(mostPopularGenotype);
        var genome = new Genome(List.of(new Gen(1), new Gen(1), new Gen(1)));
        var genome2 = new Genome(List.of(new Gen(1), new Gen(1), new Gen(6)));
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
                .genome(popularGenome)
                .build();

        var animal3 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.EAST)
                .genome(popularGenome)
                .parents(Set.of(animal1, animal2))
                .build();

        var animal4 = Animal.builder()
                .energy(10)
                .position(position)
                .orientation(MapDirection.WEST)
                .genome(genome2)
                .parents(Set.of(animal1, animal2))
                .build();
        var animal5 = Animal.builder()
                .energy(0)
                .position(position)
                .orientation(MapDirection.WEST)
                .genome(genome)
                .parents(Set.of(animal1, animal2))
                .build();
        var animal6 = Animal.builder()
                .energy(0)
                .position(position)
                .orientation(MapDirection.WEST)
                .genome(genome)
                .parents(Set.of(animal1, animal2))
                .build();
        animal1.addChild(animal3);
        animal1.addChild(animal4);
        animal2.addChild(animal3);
        animal2.addChild(animal4);

        SimulationContext mockSimulationContext = Mockito.mock(SimulationContext.class);
        when(mockSimulationContext.getAliveAnimals()).thenReturn(Set.of(animal1, animal2, animal3, animal4));
        when(mockSimulationContext.getDeadAnimals()).thenReturn(Set.of(animal5, animal6));
        var simulationStatistics = new SimulationStatistics(mockSimulationContext);

        SimulationContext mockSimulationContext2 = Mockito.mock(SimulationContext.class);
        when(mockSimulationContext2.getAliveAnimals()).thenReturn(Set.of());
        when(mockSimulationContext2.getDeadAnimals()).thenReturn(Set.of(animal5));
        var simulationStatistics2 = new SimulationStatistics(mockSimulationContext2);

        // when
        var mostPopularGenotypeSimulation1 = simulationStatistics.getMostPopularGenotype();
        var mostPopularGenotypeSimulation2 = simulationStatistics2.getMostPopularGenotype();

        // then
        assertTrue(mostPopularGenotypeSimulation1.isPresent());
        assertTrue(mostPopularGenotypeSimulation2.isEmpty());
        assertEquals(mostPopularGenotypeSimulation1.get(), mostPopularGenotypeSimulation1.get());
    }
}