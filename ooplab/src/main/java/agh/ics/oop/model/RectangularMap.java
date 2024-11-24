package agh.ics.oop.model;

public class RectangularMap extends AbstractWorldMap {

  private final Vector2d leftBotCorner;
  private final Vector2d rightTopCorner;

  public RectangularMap(int width, int height) {
    super();
    rightTopCorner = new Vector2d(width - 1, height - 1);
    leftBotCorner = new Vector2d(0, 0);
  }

  //for testing purposes
  Vector2d getRightTopCorner() {
    return rightTopCorner;
  }

  @Override
  public boolean canMoveTo(Vector2d position) {
    return super.canMoveTo(position) && position.follows(leftBotCorner) && position.precedes(rightTopCorner);
  }

  @Override
  public Boundary getCurrentBounds() {
    return new Boundary(leftBotCorner, rightTopCorner);
  }
}
