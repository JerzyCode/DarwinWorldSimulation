package agh.ics.oop.model.map;

import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.elements.Animal;
import agh.ics.oop.model.map.elements.Grass;
import agh.ics.oop.model.map.elements.WorldElement;
import agh.ics.oop.model.move.MoveAdjustment;
import agh.ics.oop.model.move.MoveDirection;
import agh.ics.oop.model.move.PositionAdjuster;

import java.util.*;

public class Earth extends AbstractWorldMap implements PositionAdjuster {
    private final Boundary boundary;
    private final Map<Vector2d, Grass> grasses;

    public Earth(int width, int height) {
        super();
        boundary = new Boundary(new Vector2d(0, 0), new Vector2d(width - 1, height - 1));
        grasses = new HashMap<>();
    }

    // TODO: remove unused constructor
    public Earth() {
        this(1, 1);
    }

    @Override
    public Boundary getCurrentBounds() {
        return boundary;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }

    @Override
    public Collection<WorldElement> getElements() {
        var elements = new ArrayList<WorldElement>();
        elements.addAll(grasses.values());
        elements.addAll(super.getElements());

        return Collections.unmodifiableCollection(elements);
    }

    @Override
    public MoveAdjustment adjustMove(Animal animal) {
        var position = animal.getPosition();
        var orientation = animal.getOrientation();

        if (position.getY() < boundary.leftBottomCorner().getY()) {
            position = new Vector2d(position.getX(), boundary.leftBottomCorner().getY());
            orientation = orientation.next().next();
        } else if (position.getY() > boundary.rightTopCorner().getY()) {
            position = new Vector2d(position.getX(), boundary.rightTopCorner().getY());
            orientation = orientation.next().next();
        } else if (position.getX() < boundary.leftBottomCorner().getX()) {
            position = new Vector2d(boundary.rightTopCorner().getX(), position.getY());
        } else if (position.getX() > boundary.rightTopCorner().getX()) {
            position = new Vector2d(boundary.leftBottomCorner().getX(), position.getY());
        }
        return new MoveAdjustment(position, orientation);
    }

    // TODO: różni się tylko jedną linijką ale trzeba zrobić override bo trzeba jakoś przekazać 3 argument do move
    @Override
    public void move(Animal animal, MoveDirection direction) {
        if (animals.containsValue(animal)) {
            animals.remove(animal.getPosition());
            animal.move(direction, this, this);
            animals.put(animal.getPosition(), animal);
            notifyListeners("Animal moved to position: " + animal.getPosition());
        }
    }

    public void placePlant(Grass plant) throws IncorrectPositionException {
        if (grasses.containsKey(plant.getPosition())) {
            throw new IncorrectPositionException(plant.getPosition());
        } else {
            grasses.put(plant.getPosition(), plant);
        }
    }

    public boolean isGrassAtPosition(Vector2d position) {
        return grasses.containsKey(position);
    }

    public void removeGrass(Vector2d position) {
        grasses.remove(position);
    }

    public int getMapSize() {
        return (boundary.rightTopCorner().getX() + 1) * (boundary.rightTopCorner().getY() + 1);
    }
}

