package agh.ics.oop.factory;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.PlantVariant;
import agh.ics.oop.model.configuration.WorldMapConfiguration;
import agh.ics.oop.model.configuration.WorldMapVariant;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.map.fire.FireEarth;
import agh.ics.oop.model.map.plant.Earth;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class WorldMapFactoryTest {

    @Test
    void should_create_earth() {
        //given
        var configuration = WorldMapConfiguration.builder()
                .height(100)
                .width(100)
                .startPlantCount(10)
                .mapVariant(WorldMapVariant.EARTH)
                .plantVariant(PlantVariant.FORESTED_EQUATORS)
                .build();

        var factory = new WorldMapFactory(configuration, null);

        //when
        var map = factory.createWorldMap();

        //then
        var mapBoundary = map.getCurrentBounds();

        assertInstanceOf(Earth.class, map);
        assertEquals(99, mapBoundary.rightTopCorner().getX());
        assertEquals(0, mapBoundary.leftBottomCorner().getX());
        assertEquals(0, mapBoundary.leftBottomCorner().getY());
        assertEquals(99, mapBoundary.rightTopCorner().getY());
        assertEquals(new Boundary(new Vector2d(0, 0), new Vector2d(99, 99)), map.getCurrentBounds());
        assertEquals(10, calculatePlantCount(map));
    }

    @Test
    void should_create_fire_earth() {
        //given
        var configuration = WorldMapConfiguration.builder()
                .height(500)
                .width(500)
                .startPlantCount(10)
                .mapVariant(WorldMapVariant.FIRE)
                .plantVariant(PlantVariant.FORESTED_EQUATORS)
                .build();

        var factory = new WorldMapFactory(configuration, null);

        //when
        var map = factory.createWorldMap();

        //then
        var mapBoundary = map.getCurrentBounds();

        assertInstanceOf(FireEarth.class, map);
        assertEquals(499, mapBoundary.rightTopCorner().getX());
        assertEquals(0, mapBoundary.leftBottomCorner().getX());
        assertEquals(0, mapBoundary.leftBottomCorner().getY());
        assertEquals(499, mapBoundary.rightTopCorner().getY());
        assertEquals(new Boundary(new Vector2d(0, 0), new Vector2d(499, 499)), map.getCurrentBounds());
        assertEquals(10, calculatePlantCount(map));
    }


    private int calculatePlantCount(WorldMap worldMap) {
        return (int) worldMap.getElements().stream()
                .filter(element -> element instanceof Plant)
                .count();
    }

}