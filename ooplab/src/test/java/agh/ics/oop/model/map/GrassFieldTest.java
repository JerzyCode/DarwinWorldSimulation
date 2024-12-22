package agh.ics.oop.model.map;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.map.elements.Animal;
import agh.ics.oop.model.map.elements.Grass;
import agh.ics.oop.model.util.RandomPositionGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GrassFieldTest {

  @Test
  void shouldInitializeGrassField() {
    //given
    var position = new Vector2d(8, 10);
    var randomizer = spyRandomizerWithOnePosition(position);

    //when
    var result = new GrassField(randomizer);

    //then
    var bounds = result.getCurrentBounds();
    assertEquals(new Vector2d(8, 10), bounds.rightTopCorner());
    assertEquals(new Vector2d(8, 10), bounds.rightTopCorner());
    assertEquals(1, result.getElements().size());
  }

  @Test
  void shouldInitializeGrassFieldWith0GrassCount() {
    //given
    var grassCount = 0;

    //when
    var result = new GrassField(grassCount);

    //then
    assertEquals(0, result.getElements().size());
  }

  @Test
  void shouldPlaceAllGrasses() {
    //given
    var grassCount = 10;

    //when
    var grassField = new GrassField(grassCount);

    //then
    var elements = grassField.getElements();
    assertEquals(grassCount, elements.size());

    elements.forEach(element -> assertInstanceOf(Grass.class, element));
  }

  @Test
  void shouldPlaceAnimal() throws IncorrectPositionException {
    //given
    var grassCount = 0;
    var animal = new Animal(new Vector2d(0, 0));
    var grassField = new GrassField(grassCount);

    //when
    grassField.place(animal);

    //then
    var elements = grassField.getElements();
    assertEquals(1, elements.size());
  }

  @Test
  void shouldThrowExceptionWhenAnimalPositionOccupied() throws IncorrectPositionException {
    //given
    var grassCount = 0;
    var animal1 = new Animal(new Vector2d(0, 0));
    var animal2 = new Animal(new Vector2d(0, 0));
    var grassField = new GrassField(grassCount);
    grassField.place(animal1);

    //when && then
    assertThrows(IncorrectPositionException.class, () -> grassField.place(animal2));

    var elements = grassField.getElements();
    assertEquals(1, elements.size());
  }

  @Test
  void shouldPlaceAnimalOverGrass() throws IncorrectPositionException {
    //given
    var animal = new Animal(new Vector2d(1, 1));
    var randomizer = spyRandomizerWithOnePosition(new Vector2d(1, 1));
    var grassField = new GrassField(randomizer);

    //when
    grassField.place(animal);

    //then
    var elements = grassField.getElements();
    assertEquals(2, elements.size());
    var iterator = elements.iterator();
    assertInstanceOf(Grass.class, iterator.next());
    assertInstanceOf(Animal.class, iterator.next());
  }

  @Test
  void shouldMoveAnimalOverGrass() throws IncorrectPositionException {
    //given
    var animal = new Animal(new Vector2d(1, 0), MapDirection.NORTH);
    var randomizer = spyRandomizerWithOnePosition(new Vector2d(1, 1));
    var grassField = new GrassField(randomizer);
    grassField.place(animal);

    //when
    grassField.move(animal, MoveDirection.FORWARD);

    //then
    var elements = grassField.getElements();
    assertEquals(2, elements.size());
    var iterator = elements.iterator();

    assertNotEquals(animal, iterator.next());
    assertEquals(animal, iterator.next());
  }

  @Test
  void shouldIncreaseRightTopCornerWhenMovingAnimal() throws IncorrectPositionException {
    //given
    var animal = new Animal(new Vector2d(1, 1), MapDirection.NORTH);
    var randomizer = spyRandomizerWithOnePosition(new Vector2d(1, 1));

    var grassField = new GrassField(randomizer);
    grassField.place(animal);

    //when
    grassField.move(animal, MoveDirection.FORWARD);
    grassField.move(animal, MoveDirection.RIGHT);
    grassField.move(animal, MoveDirection.FORWARD);

    //then
    var bounds = grassField.getCurrentBounds();
    assertEquals(new Vector2d(2, 2), bounds.rightTopCorner());
  }

  @Test
  void shouldBeAnimalObjectAtPosition() throws IncorrectPositionException {
    //given
    var grassCount = 50;
    var animal = new Animal(new Vector2d(0, 0));

    var grassField = new GrassField(grassCount);
    grassField.place(animal);

    //when
    var result = grassField.objectAt(new Vector2d(0, 0));

    //then
    assertInstanceOf(Animal.class, result);
    assertEquals(result.getPosition(), new Vector2d(0, 0));
  }

  @Test
  void shouldBeGrassObjectAtPosition() {
    //given
    var randomizer = spyRandomizerWithOnePosition(new Vector2d(1, 1));

    var grassField = new GrassField(randomizer);

    //when
    var result = grassField.objectAt(new Vector2d(1, 1));

    //then
    assertInstanceOf(Grass.class, result);
    assertEquals(result.getPosition(), new Vector2d(1, 1));
  }

  @Test
  void shouldBeOccupiedByGrass() {
    //given
    var randomizer = spyRandomizerWithOnePosition(new Vector2d(1, 1));

    var grassField = new GrassField(randomizer);

    //when
    var result = grassField.isOccupied(new Vector2d(1, 1));

    //then
    assertTrue(result);
  }

  @Test
  void shouldBeOccupiedByAnimal() throws IncorrectPositionException {
    //given
    var grassCount = 100;
    var animal = new Animal((new Vector2d(0, 0)));

    var grassField = new GrassField(grassCount);
    grassField.place(animal);

    //when
    var result = grassField.isOccupied(new Vector2d(0, 0));

    //then
    assertTrue(result);
  }

  @Test
  void shouldBeAbleToMoveWhenGoOutsideMapWestSouth() {
    //given
    var grassCount = 100;
    var grassField = new GrassField(grassCount);

    //when && then
    assertTrue(grassField.canMoveTo(new Vector2d(-1, 0)));
    assertTrue(grassField.canMoveTo(new Vector2d(0, -1)));
  }

  @Test
  void shouldNotBeAbleToMoveWhenPositionIsOccupiedByAnimal() throws IncorrectPositionException {
    //given
    var grassCount = 100;
    var grassField = new GrassField(grassCount);
    var animal = new Animal((new Vector2d(0, 0)));
    grassField.place(animal);

    //when && then
    assertFalse(grassField.canMoveTo(new Vector2d(0, 0)));
  }

  @Test
  void shouldBeAbleToMove() {
    //given
    var grassCount = 100;
    var grassField = new GrassField(grassCount);

    //when && then
    assertTrue(grassField.canMoveTo(new Vector2d(0, 0)));
  }

  private RandomPositionGenerator spyRandomizerWithOnePosition(Vector2d position) {
    var sqrt = (int)Math.sqrt(10) + 1;
    var randomizer = Mockito.spy(new RandomPositionGenerator(1, sqrt, sqrt));
    Iterator<Vector2d> mockedIterator = Mockito.mock(Iterator.class);
    when(mockedIterator.hasNext()).thenReturn(true, false);
    when(mockedIterator.next()).thenReturn(position);
    when(randomizer.iterator()).thenReturn(mockedIterator);
    return randomizer;
  }
}