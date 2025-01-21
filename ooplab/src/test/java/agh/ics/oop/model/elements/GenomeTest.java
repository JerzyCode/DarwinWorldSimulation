package agh.ics.oop.model.elements;

import agh.ics.oop.model.configuration.MutationVariant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GenomeTest {

    @Test
    void nextGenShouldCycleThroughAllGens() {
        // given
        var gens = List.of(new Gen(0), new Gen(1), new Gen(2));
        var genome = new Genome(gens);

        // when & then
        assertEquals(gens.get(0), genome.nextGen());
        assertEquals(gens.get(1), genome.nextGen());
        assertEquals(gens.get(2), genome.nextGen());
        assertEquals(gens.get(0), genome.nextGen());
    }

    @Test
    void getActivatedGenShouldReturnCurrentGenWithoutAdvancing() {
        // given
        var gens = List.of(new Gen(0), new Gen(1), new Gen(2));
        var genome = new Genome(gens);

        // when & then
        assertEquals(gens.getFirst(), genome.getActivatedGen());
        assertEquals(gens.getFirst(), genome.getActivatedGen());

        genome.nextGen(); // Move to the next gen
        assertEquals(gens.get(1), genome.getActivatedGen());

        genome.nextGen(); // Move to the next gen
        assertEquals(gens.get(2), genome.getActivatedGen());
    }

    @ParameterizedTest
    @EnumSource(MutationVariant.class)
    void mutateGenomeShouldChangeGenParams(MutationVariant mutationVariant) {
        // given
        var gen1 = mock(Gen.class);
        var gen2 = mock(Gen.class);
        var gen3 = mock(Gen.class);
        var gens = List.of(gen1, gen2, gen3);
        var genome = new Genome(gens, mutationVariant);

        //when
        genome.mutate(2);

        //then
        var mutatedCount = 0;
        for (var gen : gens) {
            mutatedCount += wasMockGenMutated(gen, mutationVariant) ? 1 : 0;
        }

        assertEquals(2, mutatedCount);
    }


    @Test
    void mutateGenomeShouldMutateAllGensWhenCountIsHigherThanGensSize() {
        // given
        var mutationVariant = MutationVariant.FULL_RANDOM;
        var gen1 = mock(Gen.class);
        var gens = List.of(gen1);
        var genome = new Genome(gens, mutationVariant);

        //when
        genome.mutate(4);

        //then
        verify(gens.getFirst(), times(1)).mutate(mutationVariant);
    }


    @Test
    void mutateGenomeCountLessThanZeroShouldDoNothing() {
        // given
        var mutationVariant = MutationVariant.FULL_RANDOM;
        var gen1 = mock(Gen.class);
        var gens = List.of(gen1);
        var genome = new Genome(gens, mutationVariant);

        //when
        genome.mutate(-1);

        //then
        verify(gens.getFirst(), times(0)).mutate(mutationVariant);
    }

    @Test
    void getPartOfGenomeShouldStartFromLeft() {
        // given
        var gens = List.of(new Gen(0), new Gen(1), new Gen(2));
        var genome = new Genome(gens);

        //when
        var partOfGenome = genome.getPartOfGenome(2, true);

        //then
        assertEquals(2, partOfGenome.size());
        assertEquals(gens.getFirst(), partOfGenome.getFirst());
        assertEquals(gens.get(1), partOfGenome.getLast());
    }


    @Test
    void getPartOfGenomeShouldStartFromRight() {
        // given
        var gens = List.of(new Gen(0), new Gen(1), new Gen(2));
        var genome = new Genome(gens);

        //when
        var partOfGenome = genome.getPartOfGenome(2, false);

        //then
        assertEquals(2, partOfGenome.size());
        assertEquals(gens.get(1), partOfGenome.getFirst());
        assertEquals(gens.getLast(), partOfGenome.getLast());
    }

    @Test
    void getPartOfGenomeShouldReturnEmptyListWhenCountIsBelowZero() {
        // given
        var gens = List.of(new Gen(0));
        var genome = new Genome(gens);

        //when
        var partOfGenome = genome.getPartOfGenome(-2, false);

        //then
        assertTrue(partOfGenome.isEmpty());
    }

    @Test
    void getPartOfGenomeShouldCountGreaterThanGensSizeShouldReturnAllGens() {
        // given
        var gens = List.of(new Gen(0), new Gen(1), new Gen(2));
        var genome = new Genome(gens);

        //when
        var partOfGenomeLeft = genome.getPartOfGenome(12, true);
        var partOfGenomeRight = genome.getPartOfGenome(12, false);

        //then
        assertEquals(3, partOfGenomeLeft.size());
        assertEquals(3, partOfGenomeRight.size());

        assertEquals(gens.getFirst(), partOfGenomeLeft.getFirst());
        assertEquals(gens.get(1), partOfGenomeLeft.get(1));
        assertEquals(gens.getLast(), partOfGenomeLeft.getLast());

        assertEquals(gens.getFirst(), partOfGenomeRight.getFirst());
        assertEquals(gens.get(1), partOfGenomeRight.get(1));
        assertEquals(gens.getLast(), partOfGenomeRight.getLast());
    }

    private boolean wasMockGenMutated(Gen gen, MutationVariant mutationVariant) {
        try {
            verify(gen, times(1)).mutate(mutationVariant);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }
}
