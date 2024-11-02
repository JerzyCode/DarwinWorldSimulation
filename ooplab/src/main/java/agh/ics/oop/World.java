package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;

import java.util.List;

public class World {
  private static final String GO_FORWARD_MESSAGE = "Zwierzak idzie do przodu";
  private static final String GO_BACKWARD_MESSAGE = "Zwierzak idzie do tyłu";
  private static final String GO_LEFT_MESSAGE = "Zwierzak idzie w lewo";
  private static final String GO_RIGHT_MESSAGE = "Zwierzak idzie w prawo";

  public static void main(String[] args) {
    System.out.println("Start");
    run(OptionsParser.parse(args));
    System.out.println("Stop");

    Vector2d position1 = new Vector2d(1, 2);
    System.out.println(position1);
    Vector2d position2 = new Vector2d(-2, 1);
    System.out.println(position2);
    System.out.println(position1.add(position2));
    System.out.println(position1.subtract(position2));

    MapDirection mapDirection = MapDirection.NORTH;
    System.out.println(mapDirection.next());
    System.out.println(mapDirection.previous());
    System.out.println(mapDirection.toUnitVector());

    var animal = new Animal();
    System.out.println(animal);
  }

  private static void run(List<MoveDirection> directions) {
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
