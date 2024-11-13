package agh.ics.oop.model;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap<T, P> extends MoveValidator<P> {

  boolean place(T t);

  /**
   * Moves an object t (if it is present on the map) according to specified direction.
   * If the move is not possible, this method has no effect.
   */
  void move(T t, MoveDirection direction);

  /**
   * Return true if given position on the map is occupied. Should not be
   * confused with canMove since there might be empty positions where the object t
   * cannot move.
   *
   * @param p
   *     Position to check.
   * @return True if the position is occupied.
   */
  boolean isOccupied(P p);

  /**
   * Return an object t at a given position.
   *
   * @param p
   *     The position of the object t.
   * @return object t or null if the position is not occupied.
   */
  T objectAt(P p);
}