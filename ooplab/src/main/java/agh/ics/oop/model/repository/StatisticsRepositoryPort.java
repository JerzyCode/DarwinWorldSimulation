package agh.ics.oop.model.repository;

import agh.ics.oop.model.statistics.Statistics;

import java.io.IOException;

public interface StatisticsRepositoryPort {
    void save(Statistics statistics, String worldMapUuid);

    void close() throws IOException;
}
