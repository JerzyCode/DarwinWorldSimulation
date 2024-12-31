package agh.ics.oop.model.move;


import agh.ics.oop.model.map.elements.Animal;

public interface PositionAdjuster {

    MoveAdjustment adjustMove(Animal animal);

}
