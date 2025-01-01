package agh.ics.oop;

public class Simulation implements Runnable {
  private final int daysCount;
  private final SimulationContext simulationContext;

  public Simulation(SimulationContext simulationContext, int daysCount) {
    this.simulationContext = simulationContext;
    this.daysCount = daysCount;
  }

  @Override
  public void run() {
    for (int i = 0; i < daysCount; i++) {
      try {
        simulationContext.handleDayEnds();
        Thread.sleep(250);
      }
      catch (InterruptedException e) {
        System.out.println("Simulation was interrupted!!");
        return;
      }
    }
  }

}
