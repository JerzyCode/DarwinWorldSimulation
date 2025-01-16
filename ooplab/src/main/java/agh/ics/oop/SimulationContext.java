package agh.ics.oop;

import agh.ics.oop.factory.AnimalFactory;
import agh.ics.oop.factory.WorldMapFactory;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.SimulationWorldMap;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.exceptions.AnimalNotBirthException;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.AbstractWorldMap;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.map.plant.Earth;
import agh.ics.oop.model.move.MoveDirection;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.HashSet;
import java.util.OptionalDouble;
import java.util.Set;

public class SimulationContext {
    private final Configuration configuration;
    private final AnimalFactory animalFactory;
    private final SimulationWorldMap worldMap;
    private final Set<Animal> animals;
    private final Set<Animal> deadAnimals;
    private int currentDay;

    public SimulationContext(Configuration configuration) {
        this.configuration = configuration;
        this.animalFactory = new AnimalFactory(configuration.getAnimalConfiguration());
        WorldMapFactory worldMapFactory = new WorldMapFactory(configuration.getWorldMapConfiguration(), configuration.getSimulationConfiguration());
        this.worldMap = worldMapFactory.createWorldMap();
        this.animals = new HashSet<>();
        this.deadAnimals = new HashSet<>();
        currentDay = 1;

        createAnimals();
    }

    public void handleDayEnds() {
        System.out.println("Current day: " + currentDay + ", animalsCount=" + animals.size());
        clearDeadAnimals();
        handleAnimalsMove();
        handleCopulate();
        handleAnimalLossEnergy();

        worldMap.handleDayEnds(currentDay);
        currentDay++;
    }


    private void handleAnimalsMove() {
        animals.forEach(animal -> worldMap.move(animal, MoveDirection.FORWARD));
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
                                var child = animalFactory.birthAnimal(parent1, parent2, 2 * lossCopulateEnergy, currentDay);
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


    private void clearDeadAnimals() {
        animals.removeIf(animal -> {
            if (animal.isDead()) {
                System.out.println("removing dead animal");
                worldMap.removeAnimal(animal);
                deadAnimals.add(animal);
                animal.setEndDay(currentDay);
                return true;
            }
            return false;
        });
    }

    private void handleAnimalLossEnergy() {
        animals.forEach(animal -> animal.decreaseEnergy(1));
    }


    private void createAnimals() {
        var boundary = worldMap.getCurrentBounds();
        var randomizer = new RandomPositionGenerator(
                configuration.getSimulationConfiguration().getStartAnimalCount(),
                boundary.rightTopCorner().getX(),
                boundary.rightTopCorner().getY());

        for (Vector2d position : randomizer) {
            var animal = animalFactory.createAnimal(position, currentDay);
            try {
                worldMap.place(animal);
                animals.add(animal);
            } catch (IncorrectPositionException e) {
                System.out.println("createAnimals(), animal not placed: message=" + e.getMessage());
            }
        }
    }


    public void setMapChangeListener(MapChangeListener listener) {
        ((AbstractWorldMap) worldMap).addListener(listener);
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public int getAnimalCount() {
        return animals.size();
    }

    public OptionalDouble getAverageAnimalEnergy() {
        return animals.stream()
                .mapToDouble(Animal::getEnergy)
                .average();
    }

    public OptionalDouble getAverageDeadAnimalTimeLife() {
        return deadAnimals.stream()
                .mapToDouble(animal -> animal.getEndDay() - animal.getStartDay())
                .average();
    }

    public OptionalDouble getAverageAnimalCountOfChildren() {
        return animals.stream()
                .mapToInt(Animal::getCountOfChildren)
                .average();
    }

    //TODO: getMostPopularGenotype
}
