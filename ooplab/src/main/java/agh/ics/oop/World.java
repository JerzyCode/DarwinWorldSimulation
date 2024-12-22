package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.map.GrassField;
import agh.ics.oop.model.map.RectangularMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
  private static final List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(2, 3));
  private static final ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();
  private static final Random random = new Random();

  public static void main(String[] args) {
    System.out.println("Start");

    // f f r f b r r f f b f r f f f f
    try {
      var directions = OptionsParser.parse(args);
      var simulations = createSimulations(directions);
      var simulationEngine = new SimulationEngine(simulations);

      simulationEngine.runAsyncInThreadPool();
      simulationEngine.awaitSimulationEnds();
    }
    catch (IllegalArgumentException e) {
      System.out.println("Wrong arguments were passed!");
      e.printStackTrace();
    }
    catch (InterruptedException e) {
      System.out.println("Thread has been discontinued.");
      e.printStackTrace();
    }

    System.out.println("Stop");
  }

  private static List<Simulation> createSimulations(List<MoveDirection> directions) {
    var simulations = new ArrayList<Simulation>();
    for (int i = 0; i < 11; i++) {
      if (i % 2 == 0) {
        simulations.add(createGrassFieldSimulation(random.nextInt(4, 10), directions));
      }
      else {
        simulations.add(createRectangularMapSimulation(random.nextInt(4, 10), random.nextInt(4, 10), directions));
      }
    }
    return simulations;
  }

  private static Simulation createGrassFieldSimulation(int count, List<MoveDirection> directions) {
    var grassField = new GrassField(count);
    grassField.addListener(consoleMapDisplay);
    return new Simulation(positions, directions, grassField);
  }

  private static Simulation createRectangularMapSimulation(int width, int height, List<MoveDirection> directions) {
    var grassField = new RectangularMap(width, height);
    grassField.addListener(consoleMapDisplay);
    return new Simulation(positions, directions, grassField);
  }

}
