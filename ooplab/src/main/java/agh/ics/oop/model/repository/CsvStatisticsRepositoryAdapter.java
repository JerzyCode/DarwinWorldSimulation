package agh.ics.oop.model.repository;

import agh.ics.oop.model.statistics.Statistics;

import java.io.*;

public class CsvStatisticsRepositoryAdapter implements StatisticsRepositoryPort, Closeable {
    private static final String DELIMITER = ";";
    private final File directory;
    private BufferedWriter writer;

    CsvStatisticsRepositoryAdapter(String path) { // modyfikator dostępu?
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
            initializeWriter(file);
            var csvData = createRow(statistics);
            writer.write(csvData);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("Failed to save statistics: e=" + e.getMessage());
        }
    }


    @Override
    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }

    private void initializeWriter(File file) throws IOException {
        if (writer == null) {
            writer = new BufferedWriter(new FileWriter(file, true));
            if (file.exists() && file.length() == 0) {
                writer.write(getHeaders());
                writer.newLine();
            }
        }
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
