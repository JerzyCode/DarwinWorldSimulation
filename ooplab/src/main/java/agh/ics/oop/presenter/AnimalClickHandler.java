package agh.ics.oop.presenter;

import agh.ics.oop.model.Vector2d;

@FunctionalInterface
public interface AnimalClickHandler {
    void onAnimalClick(Vector2d position);
}
