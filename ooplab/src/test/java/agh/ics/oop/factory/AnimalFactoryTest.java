package agh.ics.oop.factory;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.AnimalConfiguration;
import agh.ics.oop.model.configuration.MutationVariant;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Gen;
import agh.ics.oop.model.elements.Genome;
import agh.ics.oop.model.exceptions.AnimalNotBirthException;
import agh.ics.oop.model.exceptions.InvalidCountException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnimalFactoryTest {

    @Test
    void shouldCreateAnimal() {
        //given
        var configuration = AnimalConfiguration.builder()
                .startEnergy(10)
                .minimumMutationCount(0)
                .maximumMutationCount(3)
                .wellFedEnergy(15)
                .mutationVariant(MutationVariant.FULL_RANDOM)
                .genomeLength(3)
                .build();

        var factory = new AnimalFactory(configuration);

        //when
        var animal = factory.createAnimal(new Vector2d(1, 1), 1);

        //then
        assertEquals(new Vector2d(1, 1), animal.getPosition());
        assertEquals(configuration.getStartEnergy(), animal.getEnergy());
        assertEquals(1, animal.getStartDay());
        assertTrue(animal.getParents().isEmpty());
        assertTrue(animal.getChildren().isEmpty());
        assertEquals(15, animal.getWellFedEnergy());
        assertEquals(1, animal.getStartDay());
    }


    @Test
    void shouldBirthAnimalFirstParentDominatingTakeLeftPartNoMutation() {
        //given
        var configuration = AnimalConfiguration.builder()
                .minimumMutationCount(0)
                .maximumMutationCount(0)
                .lossCopulateEnergy(20)
                .mutationVariant(MutationVariant.FULL_RANDOM)
                .genomeLength(3)
                .build();

        var parent1Genome = new Genome(List.of(new Gen(0), new Gen(1), new Gen(2)));
        var parent2Genome = new Genome(List.of(new Gen(5), new Gen(6), new Gen(7)));
        var leftParent = Animal.builder()
                .position(new Vector2d(0, 0))
                .energy(60)
                .genome(parent1Genome)
                .build();

        var rightParent = Animal.builder()
                .position(new Vector2d(0, 0))
                .energy(40)
                .genome(parent2Genome)
                .build();

        var random = mock(RandomWrapper.class);
        when(random.nextInt(2)).thenReturn(1);

        var factory = new AnimalFactory(configuration, random);

        //when
        Animal newAnimalLeftDominating = null;
        Animal newAnimalRightDominating = null;
        try {
            newAnimalLeftDominating = factory.birthAnimal(leftParent, rightParent, 1);
            newAnimalRightDominating = factory.birthAnimal(rightParent, leftParent, 1);
        } catch (AnimalNotBirthException e) {
            fail("Test shouldBirthAnimalFirstParentDominating should not fail: e=" + e.getMessage());
        }

        //then
        assertNotNull(newAnimalLeftDominating);
        assertEquals(40, newAnimalLeftDominating.getEnergy());
        assertEquals(new Vector2d(0, 0), newAnimalLeftDominating.getPosition());
        assertTrue(leftParent.getChildren().contains(newAnimalLeftDominating));
        assertTrue(rightParent.getChildren().contains(newAnimalLeftDominating));
        assertEquals(newAnimalLeftDominating.getParents(), Set.of(leftParent, rightParent));

        assertNotNull(newAnimalRightDominating);
        assertEquals(40, newAnimalRightDominating.getEnergy());
        assertEquals(new Vector2d(0, 0), newAnimalRightDominating.getPosition());
        assertTrue(leftParent.getChildren().contains(newAnimalRightDominating));
        assertTrue(rightParent.getChildren().contains(newAnimalRightDominating));
        assertEquals(newAnimalRightDominating.getParents(), Set.of(leftParent, rightParent));

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

    @Test
    void shouldBirthAnimalProperlyHandleParentsAndChildren() {
        // given
        var configuration = AnimalConfiguration.builder()
                .startEnergy(10)
                .minimumMutationCount(0)
                .maximumMutationCount(0)
                .wellFedEnergy(15)
                .mutationVariant(MutationVariant.FULL_RANDOM)
                .genomeLength(3)
                .build();

        var factory = new AnimalFactory(configuration);

        var genome = new Genome(List.of(new Gen(0), new Gen(1), new Gen(2)));
        var parent1 = Animal.builder()
                .position(new Vector2d(0, 0))
                .energy(60)
                .genome(genome)
                .build();
        var parent2 = Animal.builder()
                .position(new Vector2d(0, 0))
                .energy(60)
                .genome(genome)
                .build();

        // when
        var children1 = factory.birthAnimal(parent1, parent2, 3);
        var children2 = factory.birthAnimal(parent1, parent2, 3);
        var children3 = factory.birthAnimal(children1, children2, 10);

        // then
        assertEquals(Set.of(children1, children2), parent1.getChildren());
        assertEquals(Set.of(children1, children2), parent2.getChildren());
        assertEquals(Set.of(children3), children1.getChildren());
        assertEquals(Set.of(children3), children2.getChildren());
        assertTrue(children3.getChildren().isEmpty());
        assertEquals(Set.of(parent1, parent2), children1.getParents());
        assertEquals(Set.of(parent1, parent2), children2.getParents());
        assertEquals(Set.of(children1, children2), children3.getParents());

    }

    @Test
    void shouldBirthAnimalSetStartDayProperly() {
        // given
        var configuration = AnimalConfiguration.builder()
                .startEnergy(10)
                .minimumMutationCount(0)
                .maximumMutationCount(0)
                .wellFedEnergy(15)
                .mutationVariant(MutationVariant.FULL_RANDOM)
                .genomeLength(3)
                .build();

        var factory = new AnimalFactory(configuration);

        var genome = new Genome(List.of(new Gen(0), new Gen(1), new Gen(2)));
        var parent1 = Animal.builder()
                .position(new Vector2d(0, 0))
                .energy(60)
                .genome(genome)
                .build();
        var parent2 = Animal.builder()
                .position(new Vector2d(0, 0))
                .energy(60)
                .genome(genome)
                .build();

        // when
        var children1 = factory.birthAnimal(parent1, parent2, 10);
        var children2 = factory.birthAnimal(parent1, parent2, 15);

        // then
        assertEquals(10, children1.getStartDay());
        assertEquals(15, children2.getStartDay());
    }

    @Test
    void shouldCreateAnimalSetStartDayProperly() {
        // given
        var configuration = AnimalConfiguration.builder()
                .startEnergy(10)
                .minimumMutationCount(0)
                .maximumMutationCount(0)
                .wellFedEnergy(15)
                .mutationVariant(MutationVariant.FULL_RANDOM)
                .genomeLength(3)
                .build();

        var factory = new AnimalFactory(configuration);

        var position = new Vector2d(0, 0);

        // when
        var animal = factory.createAnimal(position, 1);
        var animal2 = factory.createAnimal(position, 25);


        // then
        assertEquals(1, animal.getStartDay());
        assertEquals(25, animal2.getStartDay());
    }


    @Test
    void animalBirthShouldThrowException() throws InvalidCountException {
        // given
        var configuration = AnimalConfiguration.builder()
                .startEnergy(10)
                .minimumMutationCount(0)
                .maximumMutationCount(0)
                .wellFedEnergy(15)
                .mutationVariant(MutationVariant.FULL_RANDOM)
                .genomeLength(3)
                .build();

        var parent1 = mock(Animal.class);
        when(parent1.getPartOfGens(anyInt(), anyBoolean())).thenThrow(InvalidCountException.class);
        var parent2 = mock(Animal.class);
        when(parent2.getPartOfGens(anyInt(), anyBoolean())).thenThrow(InvalidCountException.class);


        var factory = new AnimalFactory(configuration);

        //when & then
        assertThrows(AnimalNotBirthException.class, () -> factory.birthAnimal(parent1, parent2, 10));
    }

    @Test
    void bornAnimalShouldHaveActivatedRandomGen() {
        // given
        var randomWrapperMock = mock(RandomWrapper.class);
        var configuration = AnimalConfiguration.builder()
                .startEnergy(10)
                .minimumMutationCount(0)
                .maximumMutationCount(0)
                .wellFedEnergy(15)
                .mutationVariant(MutationVariant.FULL_RANDOM)
                .genomeLength(3)
                .build();

        when(randomWrapperMock.nextInt(2)).thenReturn(1);
        when(randomWrapperMock.nextInt(0, 3)).thenReturn(2);


        var factory = new AnimalFactory(configuration, randomWrapperMock);

        var genome = new Genome(List.of(new Gen(0), new Gen(1), new Gen(2)));
        var parent1 = Animal.builder()
                .position(new Vector2d(0, 0))
                .energy(60)
                .genome(genome)
                .build();
        var parent2 = Animal.builder()
                .position(new Vector2d(0, 0))
                .energy(60)
                .genome(genome)
                .build();

        // when
        var child = factory.birthAnimal(parent1, parent2, 10);

        // then
        var childGenome = child.getGenome();
        assertEquals(childGenome.getActivatedGen(), new Gen(2));
    }
}
