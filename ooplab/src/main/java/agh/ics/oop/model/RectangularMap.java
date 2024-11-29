package agh.ics.oop.model;

public class RectangularMap extends AbstractWorldMap {

  private final Boundary boundary;

  public RectangularMap(int width, int height) {
    super();
    boundary = new Boundary(new Vector2d(0, 0), new Vector2d(width - 1, height - 1));
  }

  //for testing purposes
  Vector2d getRightTopCorner() {
    return boundary.rightTopCorner();
  }

  @Override
  public boolean canMoveTo(Vector2d position) {
    return super.canMoveTo(position) && position.follows(boundary.leftBottomCorner()) && position.precedes(boundary.rightTopCorner());
  }

  @Override
  public Boundary getCurrentBounds() {
    return boundary;
  }
}
