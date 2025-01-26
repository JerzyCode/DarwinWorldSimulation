package agh.ics.oop;

import lombok.Getter;

public class Simulation implements Runnable {
    private final int daysCount;
    @Getter
    private final SimulationContext simulationContext;
    private volatile boolean paused = true;
    private volatile boolean ended = false;

    public Simulation(SimulationContext simulationContext, int daysCount) {
        this.simulationContext = simulationContext;
        this.daysCount = daysCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < daysCount; i++) {
            synchronized (this) {
                while (paused) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                if (ended) {
                    return;
                }
            }

            simulationContext.handleDayEnds();
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        simulationContext.notifySimulationFinished();
    }

    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notifyAll();
    }

    public synchronized void end() {
        ended = true;
        paused = false;
        notifyAll();
    }

}
