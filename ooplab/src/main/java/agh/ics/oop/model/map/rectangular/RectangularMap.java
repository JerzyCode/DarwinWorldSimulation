package agh.ics.oop.model.map.rectangular;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.map.AbstractWorldMap;

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
