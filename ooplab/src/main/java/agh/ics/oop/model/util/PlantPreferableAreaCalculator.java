package agh.ics.oop.model.util;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;

public class PlantPreferableAreaCalculator {

    private PlantPreferableAreaCalculator() {

    }

    public static Boundary getPreferableArea(Boundary boundary) {
        var countOfRows = boundary.rightTopCorner().getY() - boundary.leftBottomCorner().getY() + 1;

        int countOfPreferableRows = Math.max(1, (int) (countOfRows * 0.2));
        int notPreferableRows = countOfRows - countOfPreferableRows;
        int minIndexOfPreferableRow = notPreferableRows / 2;
        int maxIndexOfPreferableRow = minIndexOfPreferableRow + countOfPreferableRows - 1;

        Vector2d leftBottomCorner = new Vector2d(boundary.leftBottomCorner().getX(), minIndexOfPreferableRow);
        Vector2d rightTopCorner = new Vector2d(boundary.rightTopCorner().getX(), maxIndexOfPreferableRow);

        return new Boundary(leftBottomCorner, rightTopCorner);
    }
}
