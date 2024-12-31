package agh.ics.oop;

import agh.ics.oop.factory.AnimalFactory;
import agh.ics.oop.factory.PlantFactory;
import agh.ics.oop.model.move.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.configuration.SimulationConfiguration;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.Earth;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.map.elements.Animal;
import agh.ics.oop.model.map.elements.Plant;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

//TODO ogólny refactor tej klasy + testy integracyjne
public class SimulationWithConfig implements Runnable {
    private final SimulationConfiguration simulationConfiguration;
    private final Random random = new Random();
    private final AnimalFactory animalFactory;
    private final PlantFactory plantFactory;
    private final List<Animal> animals;
    private final List<Plant> plants = new LinkedList<>();
    private final WorldMap worldMap;

    public SimulationWithConfig(Configuration configuration, WorldMap worldMap) {
        this.simulationConfiguration = configuration.getSimulationConfiguration();
        this.animalFactory = new AnimalFactory(configuration.getAnimalConfiguration());
        this.plantFactory = new PlantFactory(configuration.getSimulationConfiguration().getPlantVariant());
        this.worldMap = worldMap;
        animals = createAnimals();
        createPlants(simulationConfiguration.getStartPlantCount());
    }

    @Override
    public void run() {
        for (int i = 0; i < simulationConfiguration.getDaysCount(); i++) {
            try {
                animals.forEach(animal -> worldMap.move(animal, getRandomMoveDirection()));
                handleDayEnds();
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Simulation was interrupted!!");
                return;
            }
        }
    }

    private void handleGrassEating() {
        animals.forEach(animal -> {
            if (((Earth) worldMap).isGrassAtPosition(animal.getPosition())) {
                ((Earth) worldMap).removeGrass(animal.getPosition());
                plants.removeIf(grass -> grass.getPosition() == animal.getPosition());
                //TODO: zwiekszyc energie zwierzaka
            }
        });
    }

    private MoveDirection getRandomMoveDirection() { //TODO do wydzielenia do genów
        return MoveDirection.values()[random.nextInt(4)];
    }


    //  private void createRandomPlants() { //TODO refactor
//    var boundary = worldMap.getCurrentBounds();
//
//    var randomizer = new RandomPositionGenerator(
//        simulationConfiguration.getPlantGrowth(),
//        boundary.rightTopCorner().getX(),
//        boundary.rightTopCorner().getY());
//
//    for (Vector2d pos : randomizer) {
//      var grass = new Grass(pos);
//      plants.add(grass);
//      addPlantOnMap(grass);
//    }
//
//  }
    private List<Animal> createAnimals() {
        var boundary = worldMap.getCurrentBounds();
        List<Animal> animals = new ArrayList<>();
        var randomizer = new RandomPositionGenerator(
                simulationConfiguration.getStartAnimalCount(),
                boundary.rightTopCorner().getX(),
                boundary.rightTopCorner().getY());

        for (Vector2d position : randomizer) {
            System.out.println("position=" + position);
            var animal = animalFactory.createAnimal(position);
            try {
                worldMap.place(animal);
                animals.add(animal);
            } catch (IncorrectPositionException e) {
                System.out.println("createAnimals(), animal not placed: message=" + e.getMessage());
            }
        }
        return animals;
    }

    private void createPlants(int plantCount) {

        int countOfPlantsBeforeCreating = plants.size();
        var countOfAvailablePlacesForPlants = ((Earth) worldMap).getMapSize() - countOfPlantsBeforeCreating;
        plantCount = Math.min(plantCount, countOfAvailablePlacesForPlants);

        int placedPlantsCount = 0;

        while (placedPlantsCount < plantCount) {
            try {
                var plant = plantFactory.createPlant(worldMap.getCurrentBounds());
                ((Earth) worldMap).placePlant(plant);
                plants.add(plant);
                placedPlantsCount++;
                System.out.println("Stowrzyłem roślinę");
            } catch (IncorrectPositionException ignored) {
                System.out.println("Nie stworzyłem rośliny");
            }
        }

    }

    private void handleDayEnds() {
        handleGrassEating();
        createPlants(simulationConfiguration.getPlantGrowth());
    }

//  private void addPlantOnMap(Grass grass) { //TODO do wywalenia
//    if (worldMap instanceof GrassField) {
//      ((GrassField)worldMap).placeGrass(grass);
//    }
//  }

}
