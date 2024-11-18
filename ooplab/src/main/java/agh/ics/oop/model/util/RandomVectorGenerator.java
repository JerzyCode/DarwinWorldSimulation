package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.Random;

public class RandomVectorGenerator {
  private final Random random;

  public RandomVectorGenerator() {
    random = new Random();
  }

  public Vector2d generateRandomVector(int minX, int minY, int maxX, int maxY) {
    var randomX = random.nextInt(minX, maxX);
    var randomY = random.nextInt(minY, maxY);
    return new Vector2d(randomX, randomY);
  }
}
