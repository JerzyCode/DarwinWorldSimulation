package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {
  private int eventsCount;

  public ConsoleMapDisplay() {
    this.eventsCount = 0;
  }

  @Override
  public void mapChanged(WorldMap worldMap, MapChangeEventData data) {
    synchronized (System.out) {
      eventsCount += 1;
      System.out.println(data.getMessage());
      System.out.println("Map ID: " + worldMap.getId());
      System.out.print(worldMap);
      System.out.println("Events count: " + eventsCount);
    }
  }
}
