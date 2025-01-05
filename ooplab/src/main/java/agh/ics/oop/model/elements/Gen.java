package agh.ics.oop.model.elements;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.configuration.MutationVariant;

import java.util.Random;

public class Gen {
    private int index;

    public Gen(int index) {
        if (index < 0 || index >= MapDirection.values().length) {
            throw new IllegalArgumentException("Gen index is out of bound!");
        }
        this.index = index;
    }

    MapDirection rotate(MapDirection direction) {
        return direction.rotate(index * 45);
    }

    void mutate(MutationVariant variant) {
        switch (variant) {
            case MutationVariant.FULL_RANDOM -> mutateFullRandom();
            case MutationVariant.LITTLE_CORRECTION -> mutateLittleCorrection();
        }
    }

    int getIndex() {
        return index;
    }

    private void mutateFullRandom() {
        var random = new Random();
        var prev = index;
        while (prev == index) {
            index = random.nextInt(0, MapDirection.values().length);
        }
    }

    private void mutateLittleCorrection() {
        var random = new Random();

        if (index == 0) {
            index = random.nextBoolean() ? 1 : 7;
        } else {
            int direction = random.nextBoolean() ? 1 : -1;
            index = (index + direction) % MapDirection.values().length;
        }
    }
}
