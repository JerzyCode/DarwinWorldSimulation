package agh.ics.oop;

import agh.ics.oop.factory.AnimalFactory;
import agh.ics.oop.factory.PlantFactory;
import agh.ics.oop.factory.WorldMapFactory;
import agh.ics.oop.model.DayCycleHandler;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.exceptions.AnimalNotBirthException;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.AbstractWorldMap;
import agh.ics.oop.model.map.FireWorldMap;
import agh.ics.oop.model.map.PlantMap;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.map.plant.Earth;
import agh.ics.oop.model.move.MoveDirection;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.HashSet;
import java.util.Set;

public class SimulationContext {
    private final Configuration configuration;
    private final AnimalFactory animalFactory;
    private final PlantFactory plantFactory;
    private final WorldMap worldMap;
    private final Set<Animal> animals;
    private final Set<Plant> plants; //TODO wywalić - mapa tym będzie zarządzać
    private int currentDay;

    public SimulationContext(Configuration configuration) {
        this.configuration = configuration;
        this.animalFactory = new AnimalFactory(configuration.getAnimalConfiguration());
        WorldMapFactory worldMapFactory = new WorldMapFactory(configuration.getWorldMapConfiguration());
        this.plantFactory = new PlantFactory(configuration.getWorldMapConfiguration().getPlantVariant());
        this.worldMap = worldMapFactory.createWorldMap();
        this.plants = new HashSet<>();
        this.animals = new HashSet<>();
        currentDay = 1;

        createPlants(configuration.getSimulationConfiguration().getStartPlantCount());
        createAnimals();
    }

    public void handleDayEnds() {
        System.out.println("Current day: " + currentDay + ", animalsCount=" + animals.size());
        clearDeadAnimals();
        handleAnimalsMove();
//        handlePlantEating();
        handleCopulate();
        handleAnimalLossEnergy();
        handlePlantGrowth();
        handleFirefightings();
        handleAnimalsOnFire();
        currentDay++;
    }


    private void handleAnimals() {

    }

    private void handleAnimalsMove() {
        animals.forEach(animal -> worldMap.move(animal, MoveDirection.FORWARD));
    }

    private void handlePlantGrowth() {
        createPlants(configuration.getSimulationConfiguration().getPlantGrowth());
    }

    private void handleCopulate() {
        if (worldMap instanceof Earth earth) {
            Set<Animal> newAnimals = new HashSet<>();

            animals.stream()
                    .filter(animal -> animal.canMakeChild(configuration.getSimulationConfiguration().getWellFedEnergy()))
                    .map(Animal::getPosition)
                    .distinct()
                    .forEach(pos -> {
                        var animalsAt = earth.getAnimalsAtPosition(pos);
                        if (animalsAt.size() >= 2) {

                            try {
                                var lossCopulateEnergy = configuration.getSimulationConfiguration().getLossCopulateEnergy();
                                var iterator = animalsAt.iterator();
                                var parent1 = iterator.next();
                                var parent2 = iterator.next();
                                var child = animalFactory.birthAnimal(parent1, parent2, 2 * lossCopulateEnergy);
                                newAnimals.add(child);

                                parent1.decreaseEnergy(lossCopulateEnergy);
                                parent2.decreaseEnergy(lossCopulateEnergy);
                                //TODO add parents to child, and child to parents

                                worldMap.place(child);

                            } catch (AnimalNotBirthException e) {
                                System.out.println("handleCopulate(), animal could not be born: message=" + e.getMessage());
                            } catch (IncorrectPositionException e) {
                                System.out.println("handleCopulate(), animal not placed: message=" + e.getMessage());
                            }
                        }
                    });

            animals.addAll(newAnimals);
        }
    }

    private void handleAnimalsOnFire() {
        if (worldMap instanceof FireWorldMap fireWorldMap) {
            animals.stream()
                    .filter(animal -> fireWorldMap.isFireAtPosition(animal.getPosition()))
                    .forEach(Animal::kill);
        }
    }

    private void clearDeadAnimals() {
        animals.removeIf(animal -> {
            if (animal.isDead()) {
                System.out.println("removing dead animal");
                worldMap.removeAnimal(animal);
                return true;
            }
            return false;
        });
    }

    private void handleAnimalLossEnergy() {
        animals.forEach(animal -> animal.decreaseEnergy(1));
    }


    private void createPlants(int plantCount) {
        int countOfPlantsBeforeCreating = plants.size();
        var countOfAvailablePlacesForPlants = worldMap.getSize() - countOfPlantsBeforeCreating;
        plantCount = Math.min(plantCount, countOfAvailablePlacesForPlants);

        int placedPlantsCount = 0;

        while (placedPlantsCount < plantCount) {
            try {
                var plant = plantFactory.createPlant(worldMap.getCurrentBounds());
                ((PlantMap) worldMap).placePlant(plant);
                plants.add(plant);
                placedPlantsCount++;
            } catch (IncorrectPositionException ignored) {
                //        System.out.println("Couldn't create plant: " + e.getMessage());
            }
        }
    }

    private void createAnimals() {
        var boundary = worldMap.getCurrentBounds();
        var randomizer = new RandomPositionGenerator(
                configuration.getSimulationConfiguration().getStartAnimalCount(),
                boundary.rightTopCorner().getX(),
                boundary.rightTopCorner().getY());

        for (Vector2d position : randomizer) {
            var animal = animalFactory.createAnimal(position);
            try {
                worldMap.place(animal);
                animals.add(animal);
            } catch (IncorrectPositionException e) {
                System.out.println("createAnimals(), animal not placed: message=" + e.getMessage());
            }
        }
    }

    private void handleFirefightings() {
        if (worldMap instanceof DayCycleHandler fireWorldMap) {
            fireWorldMap.handleDayEnds(currentDay);//TODO inaczej to zeby nie bylo instance of
        }
    }

    public void setMapChangeListener(MapChangeListener listener) {
        ((AbstractWorldMap) worldMap).addListener(listener);
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }
}
