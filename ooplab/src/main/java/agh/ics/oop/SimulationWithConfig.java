package agh.ics.oop;

public class SimulationWithConfig implements Runnable {
  private final int daysCount;
  private final SimulationContext simulationContext;

  public SimulationWithConfig(SimulationContext simulationContext, int daysCount) {
    this.simulationContext = simulationContext;
    this.daysCount = daysCount;
  }

  @Override
  public void run() {
    for (int i = 0; i < daysCount; i++) {
      try {
        simulationContext.handleDayEnds();
        Thread.sleep(500);
      }
      catch (InterruptedException e) {
        System.out.println("Simulation was interrupted!!");
        return;
      }
    }
  }

}
