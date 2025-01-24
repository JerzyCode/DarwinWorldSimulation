package agh.ics.oop.model.repository;

import agh.ics.oop.model.elements.Gen;
import agh.ics.oop.model.statistics.Statistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CsvStatisticsRepositoryAdapterTest {
    private static final String TEST_PATH = "src/test/resources/statistics";

    private CsvStatisticsRepositoryAdapter sut;
    private File directory;
    private final Statistics statistics = Statistics.builder()
            .currentDay(1)
            .animalCount(12)
            .averageChildren(23.2)
            .averageEnergy(54.3)
            .mostPopularGenotype(List.of(new Gen(1), new Gen(2), new Gen(3)))
            .plantCount(12)
            .averageLifespan(23.3)
            .freeFieldsCount(100)
            .build();

    @BeforeEach
    void setUp() {
        directory = new File(TEST_PATH);
        sut = new CsvStatisticsRepositoryAdapter(TEST_PATH);
    }

    @AfterEach
    void tearDown() {
        try {
            sut.close();
            FileHelper.deleteTestDirectory(TEST_PATH);
        } catch (IOException e) {
            fail("Cannot close test writer, e=" + e.getMessage());
            throw new RuntimeException();
        }
    }

    @Test
    void shouldCreateDirectoryIfNoExists() {
        File file = new File(TEST_PATH);
        assertTrue(file.exists());
    }

    @Test
    void shouldCreateCsvFileWithHeaders() {
        // given
        var mapUuid = UUID.randomUUID().toString();

        // when
        sut.save(statistics, mapUuid);

        // then
        var directoryFiles = directory.listFiles();
        assertTrue(isFileSaved(mapUuid, directoryFiles));

        var savedFilePath = String.format("%s/%s.csv", TEST_PATH, mapUuid);
        assertEquals(2, countLinesInFile(savedFilePath));
    }


    @Test
    void shouldAppendLineToExistingFile() {
        // given
        var mapUuid = UUID.randomUUID().toString();
        sut.save(statistics, mapUuid);

        // when
        sut.save(statistics, mapUuid);

        //then
        var directoryFiles = directory.listFiles();
        assertTrue(isFileSaved(mapUuid, directoryFiles));

        var savedFilePath = String.format("%s/%s.csv", TEST_PATH, mapUuid);
        assertEquals(3, countLinesInFile(savedFilePath));
    }


    private boolean isFileSaved(String filename, File[] directoryFiles) {
        return Arrays.stream(directoryFiles).anyMatch(file -> file.getName().equals(String.format("%s.csv", filename)));
    }

    private long countLinesInFile(String path) {
        Path filePath = Paths.get(path);

        try (var lines = Files.lines(filePath)) {
            return lines.count();
        } catch (IOException e) {
            fail("Failed to read the file: " + e.getMessage());
        }

        return -1;
    }

}