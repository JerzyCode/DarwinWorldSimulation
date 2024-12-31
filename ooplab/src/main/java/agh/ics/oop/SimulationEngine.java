package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
  private final List<Thread> threads;
  private final ExecutorService executor;
  private final List<Simulation> simulations;

  public SimulationEngine(List<Simulation> simulations) {
    this.simulations = simulations;
    this.threads = new ArrayList<>();
    this.executor = Executors.newFixedThreadPool(4);
  }

  // TODO: nie ma to żadnego sensu
  public SimulationEngine(Simulation simulation) {
    this.simulations = List.of(simulation);
    this.threads = new ArrayList<>();
    this.executor = Executors.newFixedThreadPool(4);
  }

  public void runSync() {
    simulations.forEach(Simulation::run);
  }

  public void runAsync() {
    simulations.forEach(simulation -> {
      var thread = new Thread(simulation);
      threads.add(thread);
      thread.start();
    });
  }

  public void runAsyncInThreadPool() {
    simulations.forEach(executor::submit);
    executor.shutdown();
  }

  public void awaitSimulationEnds() throws InterruptedException {
    for (Thread thread : threads) {
      thread.join();
    }

    if (executor.isShutdown() && !executor.awaitTermination(10, TimeUnit.SECONDS)) {
      System.err.println("Some simulations still work. I'm interrupting...");
      executor.shutdownNow();
    }
  }

}
