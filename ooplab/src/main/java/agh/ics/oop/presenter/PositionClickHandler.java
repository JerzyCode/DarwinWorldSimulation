package agh.ics.oop.presenter;

import agh.ics.oop.model.Vector2d;

@FunctionalInterface
public interface PositionClickHandler {
    void onPositionClick(Vector2d position);
}
