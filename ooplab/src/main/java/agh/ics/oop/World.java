package agh.ics.oop;

import agh.ics.oop.mappers.OptionsParser;
import agh.ics.oop.model.MoveDirection;

public class World {
  private static final String GO_FORWARD_MESSAGE = "Zwierzak idzie do przodu";
  private static final String GO_BACKWARD_MESSAGE = "Zwierzak idzie do tyłu";
  private static final String GO_LEFT_MESSAGE = "Zwierzak idzie w lewo";
  private static final String GO_RIGHT_MESSAGE = "Zwierzak idzie w prawo";

  public static void main(String[] args) {
    System.out.println("Start");
    run(OptionsParser.parse(args));
    System.out.println("Stop");

  }

  public static void run(MoveDirection[] directions) {
    for (MoveDirection direction : directions) {
      switch (direction) {
        case FORWARD -> System.out.println(GO_FORWARD_MESSAGE);
        case BACKWARD -> System.out.println(GO_BACKWARD_MESSAGE);
        case RIGHT -> System.out.println(GO_RIGHT_MESSAGE);
        case LEFT -> System.out.println(GO_LEFT_MESSAGE);
      }
    }
  }
}
