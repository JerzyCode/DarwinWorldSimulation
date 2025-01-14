package agh.ics.oop.model.elements;

import agh.ics.oop.model.Vector2d;

public class Plant implements WorldElement, Eatable {
    private final Vector2d position;
    private final int energyGain;

    public Plant(Vector2d position) {
        this(position, 0);
    }

    public Plant(Vector2d position, int energyGain) {
        this.position = position;
        this.energyGain = energyGain;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public int getEnergyGain() {
        return energyGain;
    }
}
