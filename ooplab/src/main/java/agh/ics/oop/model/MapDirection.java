package agh.ics.oop.model;

public enum MapDirection {
  NORTH(new Vector2d(0, 1)),
  SOUTH(new Vector2d(0, -1)),
  WEST(new Vector2d(-1, 0)),
  EAST(new Vector2d(1, 0));

  private final Vector2d vector;

  MapDirection(Vector2d vector) {
    this.vector = vector;
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

}
