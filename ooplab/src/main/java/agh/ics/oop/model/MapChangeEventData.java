package agh.ics.oop.model;

public class MapChangeEventData {
  private final MapChangeEventType type;
  private final String message;

  public MapChangeEventData(MapChangeEventType type, String message) {
    this.type = type;
    this.message = message;
  }

  public MapChangeEventType getType() {
    return type;
  }

  public String getMessage() {
    return message;
  }
}
