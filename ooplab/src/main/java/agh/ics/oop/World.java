package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.RectangularMap;
import agh.ics.oop.model.Vector2d;

import java.util.List;

public class World {
  private static final String GO_FORWARD_MESSAGE = "Zwierzak idzie do przodu";
  private static final String GO_BACKWARD_MESSAGE = "Zwierzak idzie do tyłu";
  private static final String GO_LEFT_MESSAGE = "Zwierzak idzie w lewo";
  private static final String GO_RIGHT_MESSAGE = "Zwierzak idzie w prawo";

  public static void main(String[] args) {
    System.out.println("Start");

    // f f r f f r r f f f f r f f f f
    var directions = OptionsParser.parse(args);
    var positions = List.of(new Vector2d(2, 2), new Vector2d(0, 0));
    var worldMap = new RectangularMap(4, 4);

    var simulation = new Simulation(positions, directions, worldMap);
    simulation.run();
    System.out.println("Stop");
  }

  private static void printDirections(MoveDirection[] directions) {
    StringBuilder message = new StringBuilder();
    for (MoveDirection direction : directions) {
      switch (direction) {
        case FORWARD -> message.append(GO_FORWARD_MESSAGE).append(", ");
        case BACKWARD -> message.append(GO_BACKWARD_MESSAGE).append(", ");
        case RIGHT -> message.append(GO_RIGHT_MESSAGE).append(", ");
        case LEFT -> message.append(GO_LEFT_MESSAGE).append(", ");
      }
    }
    message.deleteCharAt(message.length() - 2);
    System.out.println(message);
  }

}
