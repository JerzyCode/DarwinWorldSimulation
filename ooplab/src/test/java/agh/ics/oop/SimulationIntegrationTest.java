package agh.ics.oop;

import agh.ics.oop.model.configuration.*;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Fire;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.map.fire.FireEarth;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class SimulationIntegrationTest {


    @ParameterizedTest
    @EnumSource(WorldMapVariant.class)
    void testInitEarthSimulationContext(WorldMapVariant mapVariant) {
        // given
        var mapConfiguration = WorldMapConfiguration.builder()
                .height(100)
                .width(50)
                .mapVariant(mapVariant)
                .plantVariant(PlantVariant.FULL_RANDOM)
                .plantGrowth(10)
                .startPlantCount(15)
                .build();

        var simulationConfiguration = SimulationConfiguration.builder()
                .startAnimalCount(25)
                .build();

        var configuration = Configuration.builder()
                .worldMapConfiguration(mapConfiguration)
                .simulationConfiguration(simulationConfiguration)
                .animalConfiguration(AnimalConfiguration.builder()
                        .startEnergy(15)
                        .genomeLength(2)
                        .build())
                .build();

        // when
        var simulationContext = new SimulationContext(configuration);

        //then
        var createdMap = simulationContext.getWorldMap();
        var elements = createdMap.getElements();
        var animalCount = elements.stream()
                .filter(element -> element instanceof Animal)
                .count();

        assertEquals(mapConfiguration.getStartPlantCount(), countElements(elements, Plant.class));
        assertEquals(simulationConfiguration.getStartAnimalCount(), countElements(elements, Animal.class));
        assertEquals(simulationConfiguration.getStartAnimalCount(), animalCount);
    }

    @Test
    void testWorldMapPlantGrowthDaily() {
        var fireConfiguration = TestConfigurationHelper.createFireConfigurationNoAnimalsNoFire();
        var earthConfiguration = TestConfigurationHelper.createEarthConfigurationNoAnimals();
        var earthSimulation = new SimulationContext(earthConfiguration);
        var fireSimulation = new SimulationContext(fireConfiguration);
        var earthMap = earthSimulation.getWorldMap();
        var fireMap = fireSimulation.getWorldMap();

        var earthStartPlantCount = earthConfiguration.getWorldMapConfiguration().getStartPlantCount();
        var fireStartPlantCount = fireConfiguration.getWorldMapConfiguration().getStartPlantCount();

        assertEquals(earthStartPlantCount, countElements(earthMap.getElements(), Plant.class));
        assertEquals(fireStartPlantCount, countElements(fireMap.getElements(), Plant.class));

        earthSimulation.handleDayEnds();
        fireSimulation.handleDayEnds();

        var earthExpectedPlantAfterOneDay = earthStartPlantCount + earthConfiguration.getWorldMapConfiguration().getPlantGrowth();
        var fireExpectedPlantAfterOneDay = fireStartPlantCount + fireConfiguration.getWorldMapConfiguration().getPlantGrowth();

        assertEquals(earthExpectedPlantAfterOneDay, countElements(earthMap.getElements(), Plant.class));
        assertEquals(fireExpectedPlantAfterOneDay, countElements(fireMap.getElements(), Plant.class));

        earthSimulation.handleDayEnds();
        fireSimulation.handleDayEnds();

        var earthExpectedPlantAfterTwoDays = earthExpectedPlantAfterOneDay + earthConfiguration.getWorldMapConfiguration().getPlantGrowth();
        var fireExpectedPlantAfterTwoDays = fireExpectedPlantAfterOneDay + fireConfiguration.getWorldMapConfiguration().getPlantGrowth();

        assertEquals(earthExpectedPlantAfterTwoDays, countElements(earthMap.getElements(), Plant.class));
        assertEquals(fireExpectedPlantAfterTwoDays, countElements(fireMap.getElements(), Plant.class));
    }


    @Test
    void testWorldMapAnimalsShouldDeadAfterSomeDays() {
        var earthConfiguration = TestConfigurationHelper.createEarthSimulationNoPlantsAndNoCopulation();
        var fireConfiguration = TestConfigurationHelper.createFireSimulationNoPlantsAndNoCopulation();
        var earthSimulation = new SimulationContext(earthConfiguration);
        var fireSimulation = new SimulationContext(fireConfiguration);
        var earthMap = earthSimulation.getWorldMap();
        var fireMap = fireSimulation.getWorldMap();

        assertEquals(0, countElements(earthMap.getElements(), Plant.class));
        assertEquals(0, countElements(fireMap.getElements(), Plant.class));
        assertEquals(earthConfiguration.getSimulationConfiguration().getStartAnimalCount(), countElements(earthMap.getElements(), Animal.class));
        assertEquals(fireConfiguration.getSimulationConfiguration().getStartAnimalCount(), countElements(fireMap.getElements(), Animal.class));

        for (int i = 0; i < earthConfiguration.getAnimalConfiguration().getStartEnergy() + 1; i++) {
            earthSimulation.handleDayEnds();
        }


        for (int i = 0; i < fireConfiguration.getAnimalConfiguration().getStartEnergy() + 1; i++) {
            fireSimulation.handleDayEnds();
        }

        assertEquals(0, countElements(earthMap.getElements(), Plant.class));
        assertEquals(0, countElements(fireMap.getElements(), Plant.class));
        assertEquals(0, countElements(earthMap.getElements(), Animal.class));
        assertEquals(0, countElements(fireMap.getElements(), Animal.class));
    }

    @Test
    void testFireShouldExistOnFireMap() {
        var fireConfiguration = TestConfigurationHelper.createFireConfiguration();
        var fireSimulation = new SimulationContext(fireConfiguration);
        var fireMap = (FireEarth) fireSimulation.getWorldMap();


        // starting fire
        for (int i = 0; i < fireConfiguration.getWorldMapConfiguration().getFireFrequency(); i++) {
            fireSimulation.handleDayEnds();
        }

        assertTrue(countElements(fireMap.getElements(), Fire.class) > 0);

        var fireOptional = fireMap.getElements().stream()
                .filter(element -> element instanceof Fire)
                .findAny();

        assertTrue(fireOptional.isPresent());
        for (int i = 0; i < fireConfiguration.getWorldMapConfiguration().getFireDuration(); i++) {
            assertTrue(fireMap.isFireAtPosition(fireOptional.get().getPosition()));
            fireSimulation.handleDayEnds();
        }

        assertFalse(fireMap.isFireAtPosition(fireOptional.get().getPosition()));
    }


    private <T extends WorldElement> int countElements(Collection<WorldElement> elements, Class<T> clazz) {
        return (int) elements.stream()
                .filter(clazz::isInstance)
                .count();
    }

}