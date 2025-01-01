package agh.ics.oop.model.elements;

import agh.ics.oop.model.Vector2d;

public class Fire implements WorldElement {
    private final Vector2d position;
    private int remainingLifetime;

    public Fire(Vector2d position, int lifeTime) {
        this.position = position;
        this.remainingLifetime = lifeTime;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public void decreaseRemainingLifetime() {
        remainingLifetime--;
    }

    public boolean isBurned() {
        return remainingLifetime <= 0;
    }


}
