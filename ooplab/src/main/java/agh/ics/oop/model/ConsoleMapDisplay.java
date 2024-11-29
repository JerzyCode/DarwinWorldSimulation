package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {
  private int eventsCount;

  public ConsoleMapDisplay() {
    this.eventsCount = 0;
  }

  @Override
  public void mapChanged(WorldMap worldMap, String message) {
    eventsCount += 1;
    System.out.println(message);
    System.out.println(worldMap);
    System.out.println("Events count: " + eventsCount);
  }
}
