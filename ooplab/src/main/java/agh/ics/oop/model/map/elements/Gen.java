package agh.ics.oop.model.map.elements;

import agh.ics.oop.model.MapDirection;

public class Gen {
  private final int index;

  public Gen(int index) {
    this.index = index;
  }

  MapDirection rotate(MapDirection direction) {
    return direction.rotate(index * 45);
  }
}
