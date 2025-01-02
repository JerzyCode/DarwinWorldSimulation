package agh.ics.oop.model.elements;

import java.util.List;

public class Genome {
    private final List<Gen> gens;
    private int currentIndex = 0;

    public Genome(List<Gen> gens) {
        this.gens = gens;
    }

    public Gen nextGen() {
        var index = currentIndex % gens.size();
        currentIndex += 1;
        return gens.get(index);
    }

    public Gen getActivatedGen() {
        return gens.get(currentIndex);
    }

}
