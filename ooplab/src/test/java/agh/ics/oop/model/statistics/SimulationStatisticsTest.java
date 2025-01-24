package agh.ics.oop.model.statistics;

import agh.ics.oop.SimulationContext;
import agh.ics.oop.TestConfigurationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    }

    @Test
    void getMostPopularGenotype() {
    }
}