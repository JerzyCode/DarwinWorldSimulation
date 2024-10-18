package agh.ics.oop.mappers;

import agh.ics.oop.model.MoveDirection;

import java.util.Arrays;
import java.util.Objects;

public class OptionsParser {

  public static MoveDirection[] parse(String[] directions) {
    return Arrays.stream(directions)
        .map(OptionsParser::parse)
        .filter(Objects::nonNull)
        .toArray(MoveDirection[]::new);
  }

  private static MoveDirection parse(String direction) {
    return switch (direction) {
      case "l" -> MoveDirection.LEFT;
      case "r" -> MoveDirection.RIGHT;
      case "f" -> MoveDirection.FORWARD;
      case "b" -> MoveDirection.BACKWARD;
      default -> null;
    };
  }
}
