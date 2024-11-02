package agh.ics.oop;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class SimulationIntegrationTest {

  @Test
  void simulateAnimalsXDecreasing() {
    //given
    String[] args = new String[] { "l", "r", "f", "b", "f", "b" };
    var animalPositions = List.of(new Vector2d(2, 2), new Vector2d(2, 2));
    var moveDirections = OptionsParser.parse(args);

    //when
    var simulation = new Simulation(animalPositions, moveDirections);
    simulation.run();

    //then
    Assertions.assertEquals(6, moveDirections.size());
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(0));
    Assertions.assertEquals(MoveDirection.RIGHT, moveDirections.get(1));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(2));
    Assertions.assertEquals(MoveDirection.BACKWARD, moveDirections.get(3));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(4));
    Assertions.assertEquals(MoveDirection.BACKWARD, moveDirections.get(5));

    var firstAnimal = simulation.getAnimals().getFirst();
    Assertions.assertEquals(MapDirection.WEST, firstAnimal.getOrientation());
    Assertions.assertEquals(new Vector2d(0, 2), firstAnimal.getPosition());

    var secondAnimal = simulation.getAnimals().getLast();
    Assertions.assertEquals(MapDirection.EAST, secondAnimal.getOrientation());
    Assertions.assertEquals(new Vector2d(0, 2), secondAnimal.getPosition());
  }

  @Test
  void simulateAnimalsXIncreasing() {
    //given
    String[] args = new String[] { "r", "l", "f", "b", "f", "b" };
    var animalPositions = List.of(new Vector2d(2, 2), new Vector2d(2, 2));
    var moveDirections = OptionsParser.parse(args);

    //when
    var simulation = new Simulation(animalPositions, moveDirections);
    simulation.run();

    //then
    Assertions.assertEquals(6, moveDirections.size());
    Assertions.assertEquals(MoveDirection.RIGHT, moveDirections.get(0));
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(1));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(2));
    Assertions.assertEquals(MoveDirection.BACKWARD, moveDirections.get(3));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(4));
    Assertions.assertEquals(MoveDirection.BACKWARD, moveDirections.get(5));

    var firstAnimal = simulation.getAnimals().getFirst();
    Assertions.assertEquals(MapDirection.EAST, firstAnimal.getOrientation());
    Assertions.assertEquals(new Vector2d(4, 2), firstAnimal.getPosition());

    var secondAnimal = simulation.getAnimals().getLast();
    Assertions.assertEquals(MapDirection.WEST, secondAnimal.getOrientation());
    Assertions.assertEquals(new Vector2d(4, 2), secondAnimal.getPosition());
  }

  @Test
  void simulateAnimalsYIncreasing() {
    //given
    String[] args = new String[] { "f", "r", "f", "r", "f", "b" };
    var animalPositions = List.of(new Vector2d(2, 2), new Vector2d(2, 2));
    var moveDirections = OptionsParser.parse(args);

    //when
    var simulation = new Simulation(animalPositions, moveDirections);
    simulation.run();

    //then
    Assertions.assertEquals(6, moveDirections.size());
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(0));
    Assertions.assertEquals(MoveDirection.RIGHT, moveDirections.get(1));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(2));
    Assertions.assertEquals(MoveDirection.RIGHT, moveDirections.get(3));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(4));
    Assertions.assertEquals(MoveDirection.BACKWARD, moveDirections.get(5));

    var firstAnimal = simulation.getAnimals().getFirst();
    Assertions.assertEquals(MapDirection.NORTH, firstAnimal.getOrientation());
    Assertions.assertEquals(new Vector2d(2, 4), firstAnimal.getPosition());

    var secondAnimal = simulation.getAnimals().getLast();
    Assertions.assertEquals(MapDirection.SOUTH, secondAnimal.getOrientation());
    Assertions.assertEquals(new Vector2d(2, 3), secondAnimal.getPosition());
  }

  @Test
  void simulateAnimalsYDecreasing() {
    //given
    String[] args = new String[] { "b", "l", "b", "l", "b", "f" };
    var animalPositions = List.of(new Vector2d(2, 2), new Vector2d(2, 2));
    var moveDirections = OptionsParser.parse(args);

    //when
    var simulation = new Simulation(animalPositions, moveDirections);
    simulation.run();

    //then
    Assertions.assertEquals(6, moveDirections.size());
    Assertions.assertEquals(MoveDirection.BACKWARD, moveDirections.get(0));
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(1));
    Assertions.assertEquals(MoveDirection.BACKWARD, moveDirections.get(2));
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(3));
    Assertions.assertEquals(MoveDirection.BACKWARD, moveDirections.get(4));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(5));

    var firstAnimal = simulation.getAnimals().getFirst();
    Assertions.assertEquals(MapDirection.NORTH, firstAnimal.getOrientation());
    Assertions.assertEquals(new Vector2d(2, 0), firstAnimal.getPosition());

    var secondAnimal = simulation.getAnimals().getLast();
    Assertions.assertEquals(MapDirection.SOUTH, secondAnimal.getOrientation());
    Assertions.assertEquals(new Vector2d(2, 1), secondAnimal.getPosition());
  }

  @Test
  void simulateTryToGoOutMap() {
    //given
    String[] args = new String[] { "f", "f", "f", "f", "f", "f" };
    var animalPositions = List.of(new Vector2d(2, 2));
    var moveDirections = OptionsParser.parse(args);

    //when
    var simulation = new Simulation(animalPositions, moveDirections);
    simulation.run();

    //then
    Assertions.assertEquals(6, moveDirections.size());
    Assertions.assertEquals(MoveDirection.BACKWARD, moveDirections.get(0));
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(1));
    Assertions.assertEquals(MoveDirection.BACKWARD, moveDirections.get(2));
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(3));
    Assertions.assertEquals(MoveDirection.BACKWARD, moveDirections.get(4));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(5));

    var firstAnimal = simulation.getAnimals().getFirst();
    Assertions.assertEquals(MapDirection.NORTH, firstAnimal.getOrientation());
    Assertions.assertEquals(new Vector2d(2, 0), firstAnimal.getPosition());

    var secondAnimal = simulation.getAnimals().getLast();
    Assertions.assertEquals(MapDirection.SOUTH, secondAnimal.getOrientation());
    Assertions.assertEquals(new Vector2d(2, 1), secondAnimal.getPosition());
  }

}