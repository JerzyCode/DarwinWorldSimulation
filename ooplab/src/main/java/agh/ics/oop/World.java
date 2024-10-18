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
    StringBuilder message = new StringBuilder();
    for (MoveDirection direction : directions) {
      switch (direction) {
        case FORWARD -> message.append(GO_FORWARD_MESSAGE).append(",\n");
        case BACKWARD -> message.append(GO_BACKWARD_MESSAGE).append(",\n");
        case RIGHT -> message.append(GO_RIGHT_MESSAGE).append(",\n");
        case LEFT -> message.append(GO_LEFT_MESSAGE).append(",\n");
      }
    }
    message.deleteCharAt(message.length() - 2);
    System.out.println(message);
  }
}
