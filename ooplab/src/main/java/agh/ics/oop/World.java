package agh.ics.oop;

import agh.ics.oop.model.ConsoleMapDisplay;
import agh.ics.oop.model.GrassField;
import agh.ics.oop.model.Vector2d;

import java.util.List;

public class World {

  public static void main(String[] args) {
    System.out.println("Start");

    // f f r f f r r f f f f r f f f f
    var directions = OptionsParser.parse(args);
    var positions = List.of(new Vector2d(2, 2), new Vector2d(2, 3));
    var worldMap = new GrassField(10);

    var consoleMapDisplay = new ConsoleMapDisplay();
    worldMap.addListener(consoleMapDisplay);

    var simulation = new Simulation(positions, directions, worldMap);
    simulation.run();
    System.out.println("Stop");
  }

}
