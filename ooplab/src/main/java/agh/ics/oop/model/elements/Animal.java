package agh.ics.oop.model.elements;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.exceptions.AnimalStillAliveException;
import agh.ics.oop.model.exceptions.InvalidCountException;
import agh.ics.oop.model.move.Move;
import agh.ics.oop.model.move.MoveAdjuster;
import agh.ics.oop.model.move.MoveDirection;
import agh.ics.oop.model.move.MoveValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
@Getter
@AllArgsConstructor
public class Animal implements WorldElement {
    private int energy;
    private Vector2d position;
    private MapDirection orientation;
    @Builder.Default
    private final Set<Animal> parents = new HashSet<>();
    @Builder.Default
    private final Set<Animal> children = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private int countOfEatenPlants;
    @Setter
    private int endDay;
    private final int wellFedEnergy;
    private final int startDay;
    private final Genome genome;

    public Animal(Vector2d position, Genome genome) {
        this(25, position, MapDirection.NORTH, new HashSet<>(), new HashSet<>(), 0, 0, 15, 0, genome);
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void decreaseEnergy(int amount) {
        this.energy -= amount;
    }

    public void eat(Eatable food) {
        this.energy += food.getEnergyGain();
        this.countOfEatenPlants++;
    }

    public boolean isDead() {
        return energy <= 0;
    }

    public void kill() {
        energy = 0;
    }

    public boolean canMakeChild() {
        return energy >= wellFedEnergy;
    }

    public void addChild(Animal child) {
        children.add(child);
    }

    public Set<Animal> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    public Set<Animal> getParents() {
        return Collections.unmodifiableSet(parents); // a to by w ogóle mogło być niemodyfikowalne - raczej się nie zmienia rodziców w czasie życia
    }

    public Set<Animal> getDescendants() {
        return children.stream()
                .flatMap(child -> Stream.concat(Stream.of(child), child.getDescendants().stream()))
                .collect(Collectors.toUnmodifiableSet());
    }

    public List<Gen> getPartOfGens(int count, boolean left) throws InvalidCountException { // czemu nie IllegalArgumentException? i czemu checked?
        if (count < 0) {
            throw new InvalidCountException();
        }
        return genome.getPartOfGenome(count, left); // po co takie pośrednictwo?
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

    public void move(MoveDirection moveDirection, MoveValidator validator, MoveAdjuster adjuster) {
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
        return String.format("Animal - Energy: %s", energy);
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public Gen getActivatedGen() {
        return genome.getActivatedGen();
    }

    public void birthAnimal(Animal child, int lossCopulateEnergy) {
        children.add(child);
        energy -= lossCopulateEnergy;
    }

    public int getCountOfChildren() {
        return children.size();
    }

    public int getCountOfDescendants() {
        return getDescendants().size();
    }

    public int getEndDay() throws AnimalStillAliveException {
        if (!isDead()) {
            throw new AnimalStillAliveException(this);
        }
        return endDay;
    }

}
