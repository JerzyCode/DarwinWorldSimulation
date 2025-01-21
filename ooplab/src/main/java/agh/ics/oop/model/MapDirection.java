package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.InvalidAngleException;

public enum MapDirection {
    NORTH(new Vector2d(0, 1), "N"),
    NORTH_EAST(new Vector2d(1, 1), "NE"),
    EAST(new Vector2d(1, 0), "E"),
    SOUTH_EAST(new Vector2d(1, -1), "SE"),
    SOUTH(new Vector2d(0, -1), "S"),
    SOUTH_WEST(new Vector2d(-1, -1), "SW"),
    WEST(new Vector2d(-1, 0), "W"),
    NORTH_WEST(new Vector2d(-1, 1), "NW");

    private final Vector2d vector;
    private final String symbol;

    MapDirection(Vector2d vector, String symbol) {
        this.vector = vector;
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "Północ";
            case NORTH_EAST -> "Północny Wschód";
            case EAST -> "Wschód";
            case SOUTH_EAST -> "Południowy Wschód";
            case SOUTH -> "Południe";
            case SOUTH_WEST -> "Południowy Zachód";
            case WEST -> "Zachód";
            case NORTH_WEST -> "Północny Zachód";
        };
    }

    public MapDirection rotateRightAngleClockwise() {
        return MapDirection.values()[(this.ordinal() + 2) % 8];
    }

    public MapDirection rotateRightAngleCounterClockwise() {
        return MapDirection.values()[(this.ordinal() + 6) % 8];
    }

    public MapDirection rotate(int angle) {
        if (angle % 45 != 0) {
            throw new InvalidAngleException();
        }

        var index = (this.ordinal() + angle / 45) % 8;
        return MapDirection.values()[index];
    }

    public Vector2d toUnitVector() {
        return vector;
    }

    public String getSymbol() {
        return symbol;
    }
}
