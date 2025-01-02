package agh.ics.oop.model.elements;

import agh.ics.oop.model.MapDirection;

public class Gen {
  private final int index;

  public Gen(int index) {
    if (index < 0 || index >= MapDirection.values().length) {
      throw new IllegalArgumentException("Gen index is out of bound!");
    }
    this.index = index;
  }

  MapDirection rotate(MapDirection direction) {
    return direction.rotate(index * 45);
  }
}
