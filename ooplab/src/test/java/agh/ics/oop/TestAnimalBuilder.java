package agh.ics.oop;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Gen;
import agh.ics.oop.model.elements.Genome;

import java.util.List;

public class TestAnimalBuilder {
    private int energy = 10;
    private Vector2d position;
    private MapDirection orientation = MapDirection.NORTH;
    private Genome genome = new Genome(List.of(new Gen(1)));

    private TestAnimalBuilder() {
    }

    public static TestAnimalBuilder create() {
        return new TestAnimalBuilder();
    }

    public TestAnimalBuilder energy(int energy) {
        this.energy = energy;
        return this;
    }

    public TestAnimalBuilder position(Vector2d position) {
        this.position = position;
        return this;
    }

    public TestAnimalBuilder orientation(MapDirection orientation) {
        this.orientation = orientation;
        return this;
    }

    public TestAnimalBuilder genome(Genome genome) {
        this.genome = genome;
        return this;
    }

    public Animal build() {
        return new Animal(energy, position, orientation, genome);
    }
}
