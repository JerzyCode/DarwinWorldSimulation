package agh.ics.oop.model.map;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Fire;

public interface FireWorldMap extends WorldMap {

    boolean isFireAtPosition(Vector2d position);

    void decreaseFireRemainingLifetime();

    void removeFire(Fire fire);

    void spreadFire(int fireDuration);

    void removeBurnedFires();

    void createFire(int fireDuration);

    void handleDayEnds(); //TODO przenieść do innego interfejsu

}
