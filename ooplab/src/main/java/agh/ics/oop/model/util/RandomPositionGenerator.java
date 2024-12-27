package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class RandomPositionGenerator implements Iterable<Vector2d>, Iterator<Vector2d> {
  private final Queue<Vector2d> availablePositions;

  public RandomPositionGenerator(int elementsCount, int maxWidth, int maxHeight) {
    availablePositions = getAvailablePositionsPositive(elementsCount, maxWidth, maxHeight);
  }

  @Override
  public Iterator<Vector2d> iterator() {
    return this;
  }

  @Override
  public boolean hasNext() {
    return !availablePositions.isEmpty();
  }

  @Override
  public Vector2d next() {
    return availablePositions.poll();
  }

  private Queue<Vector2d> getAvailablePositions(int elementsCount, int maxWidth, int maxHeight) {
    LinkedList<Vector2d> availablePositions = new LinkedList<>();
    for (int i = -maxWidth; i <= maxWidth; i++) {
      for (int j = -maxHeight; j <= maxHeight; j++) {
        availablePositions.add(new Vector2d(i, j));
      }
    }

    Collections.shuffle(availablePositions);
    availablePositions = new LinkedList<>(availablePositions.subList(0, elementsCount));
    return availablePositions;
  }

  private Queue<Vector2d> getAvailablePositionsPositive(int elementsCount, int maxWidth, int maxHeight) {
    LinkedList<Vector2d> availablePositions = new LinkedList<>();
    for (int i = 0; i <= maxWidth; i++) {
      for (int j = 0; j <= maxHeight; j++) {
        availablePositions.add(new Vector2d(i, j));
      }
    }

    Collections.shuffle(availablePositions);
    availablePositions = new LinkedList<>(availablePositions.subList(0, Math.min(elementsCount, availablePositions.size())));
    return availablePositions;
  }
}
