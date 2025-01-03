package agh.ics.oop.model.elements;

import agh.ics.oop.model.configuration.MutationVariant;

import java.util.List;

public class Genome {
    private final List<Gen> gens;
    private int currentIndex = 0;
    private MutationVariant mutation;

    public Genome(List<Gen> gens) {
        this(gens, MutationVariant.FULL_RANDOM);
    }

    public Genome(List<Gen> gens, MutationVariant mutation) {
        this.gens = gens;
        this.mutation = mutation;
    }

    public void mutate(int count) {
        if (count > gens.size()) {
            count = gens.size();
        }
        //TODO impl and tests
        switch (mutation) {

        }
    }

    public List<Gen> getPartOfGenome(int count, boolean left) {
        if (left) {
            return gens.subList(0, count);
        }
        return gens.subList(count, gens.size());
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
