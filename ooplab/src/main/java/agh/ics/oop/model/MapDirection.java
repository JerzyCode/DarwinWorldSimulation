package agh.ics.oop.model;

public enum MapDirection {
  NORTH(new Vector2d(0, 1), "N"),
  SOUTH(new Vector2d(0, -1), "S"),
  WEST(new Vector2d(-1, 0), "W"),
  EAST(new Vector2d(1, 0), "E");

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
      case SOUTH -> "Południe";
      case WEST -> "Zachód";
      case EAST -> "Wschód";
    };
  }

  public MapDirection next() {
    return switch (this) {
      case NORTH -> EAST;
      case SOUTH -> WEST;
      case WEST -> NORTH;
      case EAST -> SOUTH;
    };
  }

  public MapDirection previous() {
    return switch (this) {
      case NORTH -> WEST;
      case SOUTH -> EAST;
      case WEST -> SOUTH;
      case EAST -> NORTH;
    };
  }

  public Vector2d toUnitVector() {
    return vector;
  }

  public String getSymbol() {
    return symbol;
  }

}
