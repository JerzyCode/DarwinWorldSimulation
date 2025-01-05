package agh.ics.oop.model;

public enum MapDirection {
  NORTH(new Vector2d(0, 1), "N", "up.png"),
  SOUTH(new Vector2d(0, -1), "S", "down.png"),
  WEST(new Vector2d(-1, 0), "W", "left.png"),
  EAST(new Vector2d(1, 0), "E", "right.png");

  private final Vector2d vector;
  private final String symbol;
  private final String imageName;

  MapDirection(Vector2d vector, String symbol, String imageName) {
    this.vector = vector;
    this.symbol = symbol;
    this.imageName = imageName;
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
  public String getImageName() { return imageName; }

}
