package agh.ics.oop;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationEngine {
    private final ExecutorService executor;
    private final Map<UUID, Simulation> simulations;

    public SimulationEngine() {
        this.simulations = new HashMap<>();
        this.executor = Executors.newFixedThreadPool(4);
    }

    public UUID addSimulation(Simulation simulation) {
        var uuid = UUID.randomUUID();
        simulations.put(uuid, simulation);
        return uuid;
    }

    public void runAsyncInThreadPool(UUID simulationUUID) { // jest sens rozdzielać dodanie symulacji od uruchomienia?
        var simulation = simulations.get(simulationUUID);
        executor.submit(simulation);
    }

    public void closeThreadPool() {
        executor.shutdown();
    }

}
