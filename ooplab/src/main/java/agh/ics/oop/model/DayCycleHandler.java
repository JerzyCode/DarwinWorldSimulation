package agh.ics.oop.model;

@FunctionalInterface
public interface DayCycleHandler {
    void handleDayEnds(int currentDay);
}
