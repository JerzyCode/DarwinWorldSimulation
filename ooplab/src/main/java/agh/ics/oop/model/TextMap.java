package agh.ics.oop.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TextMap implements WorldNumberPositionMap<String, Integer> {
  private final Map<Integer, String> objects;

  public TextMap() {
    objects = new HashMap<>();
  }

  @Override
  public boolean place(String s) {
    var count = objects.size();
    objects.put(count, s);
    return true;
  }

  @Override
  public void move(String s, MoveDirection direction) {
    if (!objects.containsValue(s)) {
      return;
    }

    var move = switch (direction) {
      case LEFT, BACKWARD -> -1;
      case RIGHT, FORWARD -> 1;
    };

    var position = findKeyByValue(s);
    if (position != null) {
      var newPosition = position + move;
      if (canMoveTo(newPosition)) {
        switchPositions(position, newPosition);
      }
    }
  }

  @Override
  public boolean isOccupied(Integer position) {
    return objects.get(position) != null;
  }

  @Override
  public String objectAt(Integer position) {
    return objects.get(position);
  }

  @Override
  public boolean canMoveTo(Integer position) {
    return position < objects.size() && position > 0;
  }

  @Override
  public String toString() {
    return Arrays.deepToString(objects.values().toArray());
  }

  private Integer findKeyByValue(String value) {
    for (Map.Entry<Integer, String> entry : objects.entrySet()) {
      if (entry.getValue().equals(value)) {
        return entry.getKey();
      }
    }
    return null;
  }

  private void switchPositions(Integer position1, Integer position2) {
    String temp = objects.get(position1);
    objects.replace(position1, objects.get(position2));
    objects.replace(position2, temp);
  }
}
