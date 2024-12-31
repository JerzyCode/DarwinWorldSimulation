package agh.ics.oop.model;


import agh.ics.oop.model.map.elements.Animal;

public interface PositionAdjuster {

    MoveAdjustment adjustMove(Animal animal);

}
