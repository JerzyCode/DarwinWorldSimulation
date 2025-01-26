package agh.ics.oop.model.move;

@FunctionalInterface
public interface MoveAdjuster {
    Move adjustMove(Move move);
}
