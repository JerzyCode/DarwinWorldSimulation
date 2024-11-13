package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.RectangularMap;
import agh.ics.oop.model.TextMap;
import agh.ics.oop.model.Vector2d;

import java.util.List;

public class World {

  public static void main(String[] args) {
    System.out.println("Start");

    // f b r l f f r r f f f b b f f f
    stringMapSimulation(args);
    animalsMapSimulation(args);
  }

  private static void stringMapSimulation(String[] args) {
    var moves = OptionsParser.parse(args);
    var strings = List.of("el1", "el2", "el3", "el4", "el5", "el6");
    var worldMap = new TextMap();
    var simulation = new Simulation<>(strings, moves, worldMap);
    simulation.run();
  }

  private static void animalsMapSimulation(String[] args) {
    var directions = OptionsParser.parse(args);
    var animals = List.of(new Animal(new Vector2d(2, 2)), new Animal(new Vector2d(3, 4)));
    var worldMap = new RectangularMap(5, 5);
    var simulation = new Simulation<>(animals, directions, worldMap);
    simulation.run();
  }

}
