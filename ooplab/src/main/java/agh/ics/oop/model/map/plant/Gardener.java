package agh.ics.oop.model.map.plant;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.PlantVariant;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.HashSet;
import java.util.Set;

//TODO otestować
class Gardener {
    private final int plantGrowth;
    private final int plantEnergyGain;
    private final PlantVariant plantVariant;

    Gardener(PlantVariant plantVariant, int plantGrowth, int energyGain) {
        this.plantGrowth = plantGrowth;
        this.plantEnergyGain = energyGain;
        this.plantVariant = plantVariant;
    }

    public Set<Plant> createPlantsDaily(int currentPlantsCount, int mapCellsCount, Boundary boundary, Set<Vector2d> unavailablePositions) {
            return createPlants(currentPlantsCount, mapCellsCount, boundary, plantGrowth, unavailablePositions);

    }

    private Set<Plant> createPlants(int currentPlantsCount, int mapCellsCount, Boundary boundary, int plantCount, Set<Vector2d> unavailablePositions) {
        var plants = new HashSet<Plant>();
        var countOfAvailablePlacesForPlants = mapCellsCount - currentPlantsCount;
        plantCount = Math.min(plantCount, countOfAvailablePlacesForPlants);

        RandomPositionGenerator randomizer = switch (plantVariant) {
            case FORESTED_EQUATORS -> new RandomPositionGenerator(plantCount, boundary.rightTopCorner().getX(),
                    boundary.rightTopCorner().getY(), unavailablePositions , getPreferableArea(boundary));
            case FULL_RANDOM -> new RandomPositionGenerator(plantCount, boundary.rightTopCorner().getX(),
                    boundary.rightTopCorner().getY(), unavailablePositions);
        };

        for (Vector2d position: randomizer) {
            plants.add(new Plant(position, plantEnergyGain));
        }

        return plants;
    }

    private Boundary getPreferableArea(Boundary boundary) {
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
