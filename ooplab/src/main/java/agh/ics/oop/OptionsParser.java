package agh.ics.oop;

import agh.ics.oop.model.move.MoveDirection;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OptionsParser {

    public static List<MoveDirection> parse(String[] directions) {
        return Arrays.stream(directions)
                .map(OptionsParser::parse)
                .filter(Objects::nonNull)
                .toList();
    }

    private static MoveDirection parse(String direction) {
        return switch (direction) {
            case "l" -> MoveDirection.LEFT;
            case "r" -> MoveDirection.RIGHT;
            case "f" -> MoveDirection.FORWARD;
            case "b" -> MoveDirection.BACKWARD;
            default -> throw new IllegalArgumentException(direction + " is not valid move specification.");
        };
    }
}
