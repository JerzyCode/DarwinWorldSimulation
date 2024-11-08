package agh.ics.oop;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class AnimalMovementIntegrationTest {

  @Test
  void shouldChangeAnimalsOrientationToNorth() {
    //given
    String[] args = new String[] { "l", "r", "l", "r", "l", "r", "l", "r" };
    var animalPositions = List.of(new Vector2d(2, 2), new Vector2d(2, 2));
    var moveDirections = OptionsParser.parse(args);

    //when
    var simulation = new Simulation(animalPositions, moveDirections);
    simulation.run();

    //then
    Assertions.assertEquals(8, moveDirections.size());
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(0));
    Assertions.assertEquals(MoveDirection.RIGHT, moveDirections.get(1));
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(2));
    Assertions.assertEquals(MoveDirection.RIGHT, moveDirections.get(3));
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(4));
    Assertions.assertEquals(MoveDirection.RIGHT, moveDirections.get(5));
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(6));
    Assertions.assertEquals(MoveDirection.RIGHT, moveDirections.get(7));

    var firstAnimal = simulation.getAnimals().getFirst();
    Assertions.assertEquals(MapDirection.NORTH, firstAnimal.getOrientation());

    var secondAnimal = simulation.getAnimals().getLast();
    Assertions.assertEquals(MapDirection.NORTH, secondAnimal.getOrientation());
  }

  @Test
  void shouldChangeAnimalsOrientationToSouth() {
    //given
    String[] args = new String[] { "l", "r", "l", "r" };
    var animalPositions = List.of(new Vector2d(2, 2), new Vector2d(2, 2));
    var moveDirections = OptionsParser.parse(args);

    //when
    var simulation = new Simulation(animalPositions, moveDirections);
    simulation.run();

    //then
    Assertions.assertEquals(4, moveDirections.size());
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(0));
    Assertions.assertEquals(MoveDirection.RIGHT, moveDirections.get(1));
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(2));
    Assertions.assertEquals(MoveDirection.RIGHT, moveDirections.get(3));

    var firstAnimal = simulation.getAnimals().getFirst();
    Assertions.assertEquals(MapDirection.SOUTH, firstAnimal.getOrientation());

    var secondAnimal = simulation.getAnimals().getLast();
    Assertions.assertEquals(MapDirection.SOUTH, secondAnimal.getOrientation());
  }

  @Test
  void shouldChangeAnimalsOrientationToWest() {
    //given
    String[] args = new String[] { "l", "r", "f", "r", "f", "r" };
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
    Assertions.assertEquals(MoveDirection.RIGHT, moveDirections.get(3));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(4));
    Assertions.assertEquals(MoveDirection.RIGHT, moveDirections.get(5));

    var firstAnimal = simulation.getAnimals().getFirst();
    Assertions.assertEquals(MapDirection.WEST, firstAnimal.getOrientation());

    var secondAnimal = simulation.getAnimals().getLast();
    Assertions.assertEquals(MapDirection.WEST, secondAnimal.getOrientation());
  }

  @Test
  void shouldChangeAnimalsOrientationToEast() {
    //given
    String[] args = new String[] { "r", "l", "f", "l", "f", "l" };
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
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(3));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(4));
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(5));

    var firstAnimal = simulation.getAnimals().getFirst();
    Assertions.assertEquals(MapDirection.EAST, firstAnimal.getOrientation());

    var secondAnimal = simulation.getAnimals().getLast();
    Assertions.assertEquals(MapDirection.EAST, secondAnimal.getOrientation());
  }

  @Test
  void shouldNotChangeXWhenTryingMoveOutsideMap() {
    //given
    String[] args = new String[] { "l", "r", "f", "f", "f", "f" };
    var animalPositions = List.of(new Vector2d(0, 0), new Vector2d(4, 4));
    var moveDirections = OptionsParser.parse(args);

    //when
    var simulation = new Simulation(animalPositions, moveDirections);
    simulation.run();

    //then
    Assertions.assertEquals(6, moveDirections.size());
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(0));
    Assertions.assertEquals(MoveDirection.RIGHT, moveDirections.get(1));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(2));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(3));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(4));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(5));

    var firstAnimal = simulation.getAnimals().getFirst();
    Assertions.assertEquals(MapDirection.WEST, firstAnimal.getOrientation());
    Assertions.assertTrue(firstAnimal.isAt(new Vector2d(0, 0)));

    var secondAnimal = simulation.getAnimals().getLast();
    Assertions.assertEquals(MapDirection.EAST, secondAnimal.getOrientation());
    Assertions.assertTrue(secondAnimal.isAt(new Vector2d(4, 4)));
  }

  @Test
  void shouldNotChangeYWhenTryingMoveOutsideMap() {
    //given
    String[] args = new String[] { "f", "b", "f", "b" };
    var animalPositions = List.of(new Vector2d(4, 4), new Vector2d(0, 0));
    var moveDirections = OptionsParser.parse(args);

    //when
    var simulation = new Simulation(animalPositions, moveDirections);
    simulation.run();

    //then
    Assertions.assertEquals(4, moveDirections.size());
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(0));
    Assertions.assertEquals(MoveDirection.BACKWARD, moveDirections.get(1));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(2));
    Assertions.assertEquals(MoveDirection.BACKWARD, moveDirections.get(3));

    var firstAnimal = simulation.getAnimals().getFirst();
    Assertions.assertEquals(MapDirection.NORTH, firstAnimal.getOrientation());
    Assertions.assertTrue(firstAnimal.isAt(new Vector2d(4, 4)));

    var secondAnimal = simulation.getAnimals().getLast();
    Assertions.assertEquals(MapDirection.NORTH, secondAnimal.getOrientation());
    Assertions.assertTrue(secondAnimal.isAt(new Vector2d(0, 0)));
  }

  @Test
  void shouldDecreaseAnimalsX() {
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
    Assertions.assertTrue(firstAnimal.isAt(new Vector2d(0, 2)));

    var secondAnimal = simulation.getAnimals().getLast();
    Assertions.assertEquals(MapDirection.EAST, secondAnimal.getOrientation());
    Assertions.assertTrue(secondAnimal.isAt(new Vector2d(0, 2)));
  }

  @Test
  void shouldIncreaseAnimalsX() {
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
    Assertions.assertTrue(firstAnimal.isAt(new Vector2d(4, 2)));

    var secondAnimal = simulation.getAnimals().getLast();
    Assertions.assertEquals(MapDirection.WEST, secondAnimal.getOrientation());
    Assertions.assertTrue(secondAnimal.isAt(new Vector2d(4, 2)));
  }

  @Test
  void shouldIncreaseAnimalsY() {
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
    Assertions.assertTrue(firstAnimal.isAt(new Vector2d(2, 4)));

    var secondAnimal = simulation.getAnimals().getLast();
    Assertions.assertEquals(MapDirection.SOUTH, secondAnimal.getOrientation());
    Assertions.assertTrue(secondAnimal.isAt(new Vector2d(2, 3)));
  }

  @Test
  void shouldDecreaseAnimalsY() {
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
    Assertions.assertTrue(firstAnimal.isAt(new Vector2d(2, 0)));

    var secondAnimal = simulation.getAnimals().getLast();
    Assertions.assertEquals(MapDirection.SOUTH, secondAnimal.getOrientation());
    Assertions.assertTrue(secondAnimal.isAt(new Vector2d(2, 1)));
  }

  @Test
  void shouldReturnToTheSamePosition() {
    //given
    String[] args = new String[] { "f", "r", "f", "l", "f", "l", "f", "f", "l", "f", "f", "f", "l", "f", "l", "f" };
    var animalPositions = List.of(new Vector2d(2, 2));
    var moveDirections = OptionsParser.parse(args);

    //when
    var simulation = new Simulation(animalPositions, moveDirections);
    simulation.run();

    //then
    Assertions.assertEquals(16, moveDirections.size());
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(0));
    Assertions.assertEquals(MoveDirection.RIGHT, moveDirections.get(1));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(2));
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(3));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(4));
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(5));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(6));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(7));
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(8));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(9));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(10));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(11));
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(12));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(13));
    Assertions.assertEquals(MoveDirection.LEFT, moveDirections.get(14));
    Assertions.assertEquals(MoveDirection.FORWARD, moveDirections.get(15));

    var animal = simulation.getAnimals().getFirst();
    Assertions.assertEquals(MapDirection.NORTH, animal.getOrientation());
    Assertions.assertTrue(animal.isAt(new Vector2d(2, 2)));
  }

}