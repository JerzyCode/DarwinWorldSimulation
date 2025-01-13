package agh.ics.oop.factory;

import agh.ics.oop.TestAnimalBuilder;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.AnimalConfigurationBuilder;
import agh.ics.oop.model.configuration.MutationVariant;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Gen;
import agh.ics.oop.model.elements.Genome;
import agh.ics.oop.model.exceptions.AnimalNotBirthException;
import agh.ics.oop.model.exceptions.InvalidCountException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AnimalFactoryTest {

    @Test
    void shouldCreateAnimal() {
        //given
        var configuration = AnimalConfigurationBuilder.create()
                .startEnergy(10)
                .minimumMutationCount(0)
                .maximumMutationCount(0)
                .mutationVariant(MutationVariant.FULL_RANDOM)
                .genomeLength(3)
                .build();

        var factory = new AnimalFactory(configuration);

        //when
        var animal = factory.createAnimal(new Vector2d(1, 1));

        //then
        assertEquals(new Vector2d(1, 1), animal.getPosition());
        assertEquals(configuration.getStartEnergy(), animal.getEnergy());
    }


    @Test
    void shouldBirthAnimalFirstParentDominatingTakeLeftPartNoMutation() {
        //given
        var configuration = AnimalConfigurationBuilder.create()
                .startEnergy(10)
                .minimumMutationCount(0)
                .maximumMutationCount(0)
                .mutationVariant(MutationVariant.FULL_RANDOM)
                .genomeLength(3)
                .build();

        var parent1Genome = new Genome(List.of(new Gen(0), new Gen(1), new Gen(2)));
        var parent2Genome = new Genome(List.of(new Gen(5), new Gen(6), new Gen(7)));
        var leftParent = TestAnimalBuilder.create()
                .position(new Vector2d(0, 0))
                .energy(40)
                .genome(parent1Genome)
                .build();

        var rightParent = TestAnimalBuilder.create()
                .position(new Vector2d(0, 0))
                .energy(20)
                .genome(parent2Genome)
                .build();

        var random = Mockito.mock(RandomWrapper.class);
        when(random.nextInt(2)).thenReturn(1);

        var factory = new AnimalFactory(configuration, random);

        //when
        Animal newAnimalLeftDominating = null;
        Animal newAnimalRightDominating = null;
        try {
            newAnimalLeftDominating = factory.birthAnimal(leftParent, rightParent, 20);
            newAnimalRightDominating = factory.birthAnimal(rightParent, leftParent, 20);
        } catch (AnimalNotBirthException e) {
            fail("Test shouldBirthAnimalFirstParentDominating should not fail: e=" + e.getMessage());
        }

        //then
        assertNotNull(newAnimalLeftDominating);
        assertEquals(20, newAnimalLeftDominating.getEnergy());
        assertEquals(new Vector2d(0, 0), newAnimalLeftDominating.getPosition());

        assertNotNull(newAnimalRightDominating);
        assertEquals(20, newAnimalRightDominating.getEnergy());
        assertEquals(new Vector2d(0, 0), newAnimalRightDominating.getPosition());

        try {
            var newAnimalLeftGens = newAnimalLeftDominating.getPartOfGens(3, true);
            assertEquals(3, newAnimalLeftGens.size());
            assertEquals(List.of(new Gen(0), new Gen(1), new Gen(7)), newAnimalLeftGens);

            var newAnimalRightGens = newAnimalRightDominating.getPartOfGens(3, true);
            assertEquals(3, newAnimalRightGens.size());
            assertEquals(List.of(new Gen(0), new Gen(1), new Gen(7)), newAnimalRightGens);

        } catch (InvalidCountException e) {
            fail("Test shouldBirthAnimalFirstParentDominating should not fail: e=" + e.getMessage());
        }

    }

    //TODO more tests
}