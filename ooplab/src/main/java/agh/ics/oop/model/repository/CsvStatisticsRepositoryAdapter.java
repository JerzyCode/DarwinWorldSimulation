package agh.ics.oop.model.repository;

import agh.ics.oop.model.statistics.Statistics;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CsvStatisticsRepositoryAdapter implements StatisticsRepositoryPort, Closeable {
    private static final String DELIMITER = ";";
    private final Map<String, BufferedWriter> writers = new HashMap<>();
    private final File directory;


    CsvStatisticsRepositoryAdapter(String path) {
        directory = DirectoryInitializer.getDirectory(path);
    }

    public CsvStatisticsRepositoryAdapter() {
        this("statistics");
    }

    @Override
    public void save(Statistics statistics, String worldMapUuid) {
        var fileName = String.format("%s.csv", worldMapUuid);
        var file = new File(directory, fileName);

        try {
            var writer = getOrInitializeWriter(fileName, file);
            var csvData = createRow(statistics);
            writer.write(csvData);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("Failed to save statistics: e=" + e.getMessage());
        }
    }


    @Override
    public synchronized void close() throws IOException {
        for (var writer : writers.values()) {
            writer.close();
        }
        writers.clear();
    }

    private BufferedWriter getOrInitializeWriter(String fileName, File file) throws IOException {
        if (!writers.containsKey(fileName)) {
            var writer = new BufferedWriter(new FileWriter(file, true));
            if (file.length() == 0) {
                writer.write(getHeaders());
                writer.newLine();
            }
            writers.put(fileName, writer);
        }
        return writers.get(fileName);
    }


    private String createRow(Statistics statistics) {
        return statistics.getCurrentDay() + DELIMITER +
                statistics.getAnimalCount() + DELIMITER +
                statistics.getPlantCount() + DELIMITER +
                statistics.getFreeFieldsCount() + DELIMITER +
                String.format("%f.2", statistics.getAverageEnergy()) + DELIMITER +
                statistics.getMostPopularGenotype().toString() + DELIMITER +
                String.format("%f.2", statistics.getAverageLifespan()) + DELIMITER +
                String.format("%f.2", statistics.getAverageChildren());
    }

    private String getHeaders() {
        return "CURRENT_DAY" + DELIMITER +
                "ANIMAL_COUNT" + DELIMITER +
                "PLANT_COUNT" + DELIMITER +
                "FREE_FIELDS_COUNT" + DELIMITER +
                "AVERAGE_ENERGY" + DELIMITER +
                "MOST_POPULAR_GENOTYPE" + DELIMITER +
                "AVERAGE_LIFESPAN" + DELIMITER +
                "AVERAGE_CHILDREN";
    }

}
