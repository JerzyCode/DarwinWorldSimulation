package agh.ics.oop.model.elements;

import agh.ics.oop.model.configuration.MutationVariant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.random.RandomGenerator;

public class Genome {
    private final List<Gen> gens;
    private int currentIndex;
    private final MutationVariant mutation;

    public Genome(List<Gen> gens) {
        this(gens, MutationVariant.FULL_RANDOM);
    }

    public Genome(List<Gen> gens, MutationVariant mutation) {
        this(gens, mutation, 0);
    }

    public Genome(List<Gen> gens, MutationVariant mutation, int index) {
        this.gens = gens;
        this.mutation = mutation;
        this.currentIndex = index;
    }

    public void mutate(int count) {
        count = Math.min(count, gens.size());

        var indices = new ArrayList<Integer>();
        for (int i = 0; i < gens.size(); i++) {
            indices.add(i);
        }

        Collections.shuffle(indices, RandomGenerator.getDefault());
        for (int i = 0; i < count; i++) {
            gens.get(indices.get(i)).mutate(mutation);
        }
    }

    public List<Gen> getPartOfGenome(int count, boolean shouldStartFromLeft) {
        if (count < 0) {
            return new ArrayList<>();
        }

        count = Math.min(count, gens.size());
        List<Gen> partOfGenome;
        if (shouldStartFromLeft) {
            partOfGenome = new ArrayList<>(gens.subList(0, count));
        } else {
            partOfGenome = new ArrayList<>(gens.subList(gens.size() - count, gens.size()));
        }

        return partOfGenome;
    }

    public Gen nextGen() {
        var index = currentIndex % gens.size();
        currentIndex += 1;
        return gens.get(index);
    }

    public Gen getActivatedGen() {
        return gens.get(currentIndex % gens.size());
    }

    public List<Gen> getGens() {
        return Collections.unmodifiableList(gens);
    }
}
