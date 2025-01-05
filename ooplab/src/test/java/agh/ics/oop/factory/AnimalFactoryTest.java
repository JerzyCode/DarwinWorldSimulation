package agh.ics.oop.factory;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.AnimalConfiguration;
import agh.ics.oop.model.configuration.MutationVariant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnimalFactoryTest {

    @Test
    void should_create_animal() {
        //given
        var configuration = new AnimalConfiguration(
                10,
                10,
                10,
                MutationVariant.FULL_RANDOM
                , 10
        );

        var factory = new AnimalFactory(configuration);

        //when
        var animal = factory.createAnimal(new Vector2d(1, 1));

        //then
        assertEquals(new Vector2d(1, 1), animal.getPosition());
        assertEquals(configuration.getStartEnergy(), animal.getEnergy());
    }

    //TODO przypadki testowe po implementacji dodatkowych pól
}