package agh.ics.oop.factory;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.PlantVariant;
import agh.ics.oop.model.elements.Plant;

public class PlantFactory {
    private final PlantVariant variant;

    public PlantFactory(PlantVariant variant) {
        this.variant = variant;
    }

    public Plant createPlant(Boundary boundary) {
        return switch (variant) {
            case PlantVariant.FORESTED_EQUATORS -> createPlantForestedEquators(boundary);
            case PlantVariant.NONE -> null;
        };
    }

    //TODO: zapytać o to
    private Plant createPlantForestedEquators(Boundary boundary) {
        var countOfRows = boundary.rightTopCorner().getY() - boundary.leftBottomCorner().getY() + 1;
        var countOfColumns = boundary.rightTopCorner().getX() - boundary.leftBottomCorner().getX() + 1;

        int countOfPreferableRows = Math.max(1, (int) (countOfRows * 0.2));
        int notPreferableRows = countOfRows - countOfPreferableRows;
        int minIndexOfPreferableRow = notPreferableRows / 2;

        int row;
        if (Math.random() < 0.8) {
            row = minIndexOfPreferableRow + (int) (Math.random() * countOfPreferableRows);
        } else {
            row = (int) (Math.random() * notPreferableRows);
            if (row >= minIndexOfPreferableRow) {
                row += countOfPreferableRows;
            }
        }
        int col = (int) (Math.random() * countOfColumns);
        // TODO: z jakiejś przyczny tutaj col i row musi być tutaj odwrotnie żeby działało
        Vector2d position = new Vector2d(col, row);
        return new Plant(position);
    }

}
