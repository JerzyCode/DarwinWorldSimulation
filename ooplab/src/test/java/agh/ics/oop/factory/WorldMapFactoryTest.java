package agh.ics.oop.factory;

import agh.ics.oop.model.configuration.WorldMapConfigurationBuilder;
import agh.ics.oop.model.configuration.WorldMapVariant;
import agh.ics.oop.model.map.fire.FireEarth;
import agh.ics.oop.model.map.plant.Earth;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class WorldMapFactoryTest {

    @Test
    void should_create_earth() {
        //given
        var configuration = WorldMapConfigurationBuilder.create()
                .height(100)
                .width(100)
                .mapVariant(WorldMapVariant.EARTH)
                .build();

        var factory = new WorldMapFactory(configuration);

        //when
        var map = factory.createWorldMap();

        //then
        assertInstanceOf(Earth.class, map);
        assertEquals(100 * 100, map.getSize());
    }

    @Test
    void should_create_fire_earth() {
        //given
        var configuration = WorldMapConfigurationBuilder.create()
                .height(100)
                .width(100)
                .mapVariant(WorldMapVariant.FIRE)
                .build();

        var factory = new WorldMapFactory(configuration);

        //when
        var map = factory.createWorldMap();

        //then
        assertInstanceOf(FireEarth.class, map);
        assertEquals(100 * 100, map.getSize());
    }

}