package agh.ics.oop.model.util;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;

import java.util.*;

public class RandomPositionGenerator implements Iterable<Vector2d>, Iterator<Vector2d> {
    private final LinkedList<Vector2d> preferredPositions = new LinkedList<>();
    private final LinkedList<Vector2d> otherPositions = new LinkedList<>();
    Random random = new Random();
    private final Boundary preferredArea;
    private int elementsCount;

    public RandomPositionGenerator(int elementsCount, int maxWidth, int maxHeight, Set<Vector2d> unavailablePositions) {
        this(elementsCount, maxWidth, maxHeight, unavailablePositions, new Boundary(new Vector2d(0, 0), new Vector2d(maxWidth, maxHeight)));
    }

    public RandomPositionGenerator(int elementsCount, int maxWidth, int maxHeight, Set<Vector2d> unavailablePositions, Boundary preferredArea) {
        this.elementsCount = elementsCount;
        this.preferredArea = preferredArea;
        setAvailablePositions(maxWidth, maxHeight, unavailablePositions);
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return !(otherPositions.isEmpty() && preferredPositions.isEmpty()) && elementsCount > 0;
    }

    @Override
    public Vector2d next() {
        elementsCount--;
        if (otherPositions.isEmpty()) {
            return preferredPositions.poll();
        }

        if (preferredPositions.isEmpty()) {
            return otherPositions.poll();
        }

        if(random.nextDouble() < 0.8) {
            return preferredPositions.poll();
        }
        else {
            return otherPositions.poll();
        }
    }

    private void setAvailablePositions(int maxWidth, int maxHeight, Set<Vector2d> unavailablePositions) {
        for (int i = 0; i <= maxWidth; i++) {
            for (int j = 0; j <= maxHeight; j++) {
                var position = new Vector2d(i, j);
                if (!unavailablePositions.contains(position)) {
                    if (isPreferredPosition(position)) {
                        preferredPositions.add(position);
                    }
                    else {
                        otherPositions.add(position);
                    }
                }
            }
        }

        Collections.shuffle(preferredPositions);
        Collections.shuffle(otherPositions);
    }

    private boolean isPreferredPosition(Vector2d position) {
        return position.follows(preferredArea.leftBottomCorner()) && position.precedes(preferredArea.rightTopCorner());
    }
}
