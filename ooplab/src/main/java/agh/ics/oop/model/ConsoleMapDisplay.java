package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {
  private int eventsCount;

  public ConsoleMapDisplay() {
    this.eventsCount = 0;
  }

  @Override
  public synchronized void mapChanged(WorldMap worldMap, String message) {
    eventsCount += 1;
    System.out.println(message);
    System.out.println("Map ID: " + worldMap.getId());
    System.out.print(worldMap);
    System.out.println("Events count: " + eventsCount);
  }
}
