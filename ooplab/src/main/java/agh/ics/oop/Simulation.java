package agh.ics.oop;

public class Simulation implements Runnable {
    private final int daysCount;
    private final SimulationContext simulationContext;
    private volatile boolean paused = false;
    private volatile boolean stopped = false;


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
                        // TODO: Nie wiem czy to oznacza wątek jako przerwany czy robi głupią rzecz czyli przerywa samego siebie
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                if (stopped) {
                    return;
                }
            }

            simulationContext.handleDayEnds();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // TODO: Nie wiem czy to oznacza wątek jako przerwany czy robi głupią rzecz czyli przerywa samego siebie
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

    public synchronized void stop() {
        stopped = true;
        paused = false;
        notifyAll();
    }

}
