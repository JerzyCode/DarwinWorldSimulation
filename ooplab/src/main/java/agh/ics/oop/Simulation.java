package agh.ics.oop;

import agh.ics.oop.model.repository.CsvStatisticsRepositoryAdapter;
import agh.ics.oop.model.repository.StatisticsRepositoryPort;
import agh.ics.oop.model.statistics.Statistics;

import java.io.IOException;

public class Simulation implements Runnable {
    private final int daysCount;
    private final SimulationContext simulationContext;
    private StatisticsRepositoryPort statisticsRepositoryPort;
    private final boolean statisticsSaveEnabled;


    public Simulation(SimulationContext simulationContext, int daysCount, boolean statisticsSaveEnabled) {
        this.simulationContext = simulationContext;
        this.daysCount = daysCount;
        this.statisticsSaveEnabled = statisticsSaveEnabled;

        if (statisticsSaveEnabled) {
            this.statisticsRepositoryPort = new CsvStatisticsRepositoryAdapter();
        }

    }

    @Override
    public void run() {
        for (int i = 0; i < daysCount; i++) {
            try {
                simulationContext.handleDayEnds();

                if (statisticsSaveEnabled) {
                    Statistics statistics = simulationContext.getStatistics();
                    String worldMapUuid = simulationContext.getWorldMapUuid();
                    statisticsRepositoryPort.save(statistics, worldMapUuid);
                }

                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.out.println("Simulation was interrupted!!");
                break;
            }
        }

        if (statisticsSaveEnabled) {
            try {
                statisticsRepositoryPort.close();
            } catch (IOException e) {
                System.out.println("Can not close statistics repository");
            }
        }
    }

}
