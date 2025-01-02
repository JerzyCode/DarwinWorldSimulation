package agh.ics.oop.model.elements;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.move.Move;
import agh.ics.oop.model.move.MoveAdjuster;
import agh.ics.oop.model.move.MoveDirection;
import agh.ics.oop.model.move.MoveValidator;

public class Animal implements WorldElement {
    private final Genome genome;
    private int energy;
    private Vector2d position;
    private MapDirection orientation;

    public Animal(int startEnergy, Vector2d position, Genome genome) {
        this(startEnergy, position, MapDirection.NORTH, genome);
    }


    public Animal(int energy, Vector2d position, MapDirection orientation, Genome genome) {
        this.energy = energy;
        this.orientation = orientation;
        this.position = position;
        this.genome = genome;
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void decreaseEnergy(int amount) {
        this.energy -= amount;
    }

    public void increaseEnergy(int amount) {
        this.energy += amount;
    }

    public boolean isDead() {
        return energy <= 0;
    }

    public void kill() {
        energy = 0;
    }

    public boolean canMakeChild(int wellFedEnergy) {
        return energy >= wellFedEnergy;
    }

    public void move(MoveValidator moveValidator) {
        move(moveValidator, null);
    }

    public void move(MoveValidator moveValidator, MoveAdjuster moveAdjuster) {
        orientation = genome.nextGen().rotate(orientation);
        move(MoveDirection.FORWARD, moveValidator, moveAdjuster);
    }

    public void move(MoveDirection moveDirection, MoveValidator validator) {
        switch (moveDirection) {
            case FORWARD -> updatePosition(position.add(orientation.toUnitVector()), validator);
            case BACKWARD -> updatePosition(position.subtract(orientation.toUnitVector()), validator);
            case LEFT -> orientation = orientation.rotateRightAngleCounterClockwise();
            case RIGHT -> orientation = orientation.rotateRightAngleClockwise();
        }
    }

    void move(MoveDirection moveDirection, MoveValidator validator, MoveAdjuster adjuster) {
        move(moveDirection, validator);

        if (adjuster != null) {
            var adjustedMove = adjuster.adjustMove(new Move(position, orientation));
            position = adjustedMove.getToPosition();
            orientation = adjustedMove.getOrientation();
        }
    }

    private void updatePosition(Vector2d newPosition, MoveValidator validator) {
        if (validator.canMoveTo(newPosition)) {
            position = newPosition;
        }
    }

    @Override
    public String toString() {
        return orientation.getSymbol();
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

}
