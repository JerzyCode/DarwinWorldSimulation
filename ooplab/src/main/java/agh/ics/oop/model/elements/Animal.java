package agh.ics.oop.model.elements;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.exceptions.AnimalStillAliveException;
import agh.ics.oop.model.exceptions.InvalidCountException;
import agh.ics.oop.model.move.Move;
import agh.ics.oop.model.move.MoveAdjuster;
import agh.ics.oop.model.move.MoveDirection;
import agh.ics.oop.model.move.MoveValidator;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Animal implements WorldElement {
    private final Genome genome;
    private int energy;
    private Vector2d position;
    private MapDirection orientation;
    private Set<Animal> parents;
    private final Set<Animal> children;
    private int countOfEatenPlants;
    private final int startDay;
    private int endDay;

    public Animal(int startEnergy, Vector2d position, Genome genome) {
        this(startEnergy, position, MapDirection.NORTH, genome, 1);
    }

    public Animal(int energy, Vector2d position, MapDirection orientation, Genome genome) {
        this(energy, position, orientation, genome, 1);
    }

    public Animal(int startEnergy, Vector2d position, Genome genome, int startDay) {
        this(startEnergy, position, MapDirection.NORTH, genome, startDay);
    }


    public Animal(int energy, Vector2d position, MapDirection orientation, Genome genome, int startDay) {
        this.energy = energy;
        this.orientation = orientation;
        this.position = position;
        this.genome = genome;
        this.children = new HashSet<>();
        this.countOfEatenPlants = 0;
        this.startDay = startDay;
        this.endDay = 0;
    }

    public Animal(int energy, Vector2d position, Genome genome, Animal parent1, Animal parent2, int startDay) {
        this(energy, position, genome, startDay);
        this.parents = Set.of(parent1, parent2);
    }

    public Animal(int energy, Vector2d position, MapDirection orientation, Genome genome, Animal parent1, Animal parent2, int startDay) {
        this(energy, position, orientation, genome, startDay);
        this.parents = Set.of(parent1, parent2);
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

    public boolean canMakeChild(int wellFedEnergy) {
        return energy >= wellFedEnergy;
    }

    public void addChild(Animal child) {
        children.add(child);
    }

    public Set<Animal> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    public Set<Animal> getParents() {
        return Collections.unmodifiableSet(parents);
    }

    public Set<Animal> getDescendants() {
        return children.stream()
                .flatMap(child -> Stream.concat(Stream.of(child), child.getDescendants().stream()))
                .collect(Collectors.toUnmodifiableSet());
    }

    public List<Gen> getPartOfGens(int count, boolean left) throws InvalidCountException {
        if (count < 0) {
            throw new InvalidCountException();
        }
        return genome.getPartOfGenome(count, left);
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
        return orientation.getSymbol();
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public Genome getGenome() {
        return genome;
    }

    public Gen getActivatedGen() {
        return genome.getActivatedGen();
    }

    public int getEnergy() {
        return energy;
    }

    public int getCountOfEatenPlants() {
        return countOfEatenPlants;
    }

    public int getCountOfChildren() {
        return children.size();
    }

    public int getCountOfDescendants() {
        return getDescendants().size();
    }

    public int getStartDay() {
        return startDay;
    }

    public int getEndDay() throws AnimalStillAliveException {
        if (endDay == 0) {
            throw new AnimalStillAliveException(this);
        }
        return endDay;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }
}
