package agh.ics.oop.model;

public class RectangularMap extends AbstractWorldMap {

  public RectangularMap(int width, int height) {
    super();
    super.rightTopCorner = new Vector2d(width - 1, height - 1);
  }

  //for testing purposes
  Vector2d getRightTopCorner() {
    return rightTopCorner;
  }
}
