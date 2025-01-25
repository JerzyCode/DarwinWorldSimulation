package agh.ics.oop.model.util;

import agh.ics.oop.model.elements.Animal;

import java.util.Comparator;
import java.util.Random;

public class AnimalComparator implements Comparator<Animal> {
    private final Random random = new Random();

    @Override
    public int compare(Animal a1, Animal a2) {
        int energyComparison = Integer.compare(a1.getEnergy(), a2.getEnergy());
        if (energyComparison != 0) {
            return energyComparison;
        }

        int ageComparison = Integer.compare(a1.getStartDay(), a2.getStartDay());
        if (ageComparison != 0) {
            return -ageComparison;
        }

        int childrenComparison = Integer.compare(a1.getChildren().size(), a2.getChildren().size());
        if (childrenComparison != 0) {
            return childrenComparison;
        }

        return random.nextBoolean() ? 1 : -1;
    }
}
