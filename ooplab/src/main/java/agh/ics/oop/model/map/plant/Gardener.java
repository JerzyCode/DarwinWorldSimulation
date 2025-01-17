package agh.ics.oop.model.map.plant;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.PlantVariant;
import agh.ics.oop.model.elements.Plant;

import java.util.HashSet;
import java.util.Set;

//TODO otestować
class Gardener {
    private final PlantVariant plantVariant;
    private final int plantGrowth;
    private final int plantEnergyGain;

    Gardener(PlantVariant plantVariant, int plantGrowth, int energyGain) {
        this.plantVariant = plantVariant;
        this.plantGrowth = plantGrowth;
        this.plantEnergyGain = energyGain;
    }

    Set<Plant> createPlantsDaily(int currentPlantsCount, int mapCellsCount, Boundary boundary) {
        return createPlants(currentPlantsCount, mapCellsCount, boundary, plantGrowth);
    }

    Set<Plant> createPlants(int currentPlantsCount, int mapCellsCount, Boundary boundary, int plantCount) {
        var plants = new HashSet<Plant>();
        var countOfAvailablePlacesForPlants = mapCellsCount - currentPlantsCount;
        plantCount = Math.min(plantCount, countOfAvailablePlacesForPlants);

        int placedPlantsCount = 0;

        while (placedPlantsCount < plantCount) {
            var plant = createPlant(boundary);
            plants.add(plant);
            placedPlantsCount++;
        }

        return plants;
    }


    private Plant createPlant(Boundary boundary) {
        return switch (plantVariant) {
            case PlantVariant.FORESTED_EQUATORS -> createPlantForestedEquators(boundary);
            case PlantVariant.NONE -> null;
        };
    }


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
        Vector2d position = new Vector2d(col, row);
        return new Plant(position, plantEnergyGain);
    }
}
