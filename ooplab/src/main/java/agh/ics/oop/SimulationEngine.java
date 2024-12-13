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
    this.threads = createThreads(simulations);
    this.executor = Executors.newFixedThreadPool(4);
  }

  public void runSync() {
    simulations.forEach(Simulation::run);
  }

  public void runAsync() {
    threads.forEach(Thread::start);
  }

  public void runAsyncInThreadPool() {
    executor.submit(this::runAsync);
  }

  public void awaitSimulationEnds() {
    if (!executor.isShutdown()) {
      shutDownThreadPool();
    }
    joinAllThreads();
  }

  private void shutDownThreadPool() {
    executor.shutdown(); //TODO poprawić to zamykanie
    try {
      if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
        System.out.println("Executor did not terminate in time, force shut down");
        executor.shutdownNow();
      }
    }
    catch (InterruptedException e) {
      System.out.println("Shutting down ThreadPool interrupted, Force closing");
      executor.shutdownNow();
    }
  }

  private void joinAllThreads() {
    try {
      for (Thread thread : threads) {
        thread.join();
      }
    }
    catch (InterruptedException e) {
      System.out.println("Thread was interrupted");
    }
  }

  private List<Thread> createThreads(List<Simulation> simulations) {
    List<Thread> threads = new ArrayList<>();
    simulations.forEach(simulation -> {
      Thread thread = new Thread(simulation);
      threads.add(thread);
    });

    return threads;
  }
}
