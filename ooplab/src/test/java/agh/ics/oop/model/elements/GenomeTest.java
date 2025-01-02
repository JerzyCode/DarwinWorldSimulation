package agh.ics.oop.model.elements;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}