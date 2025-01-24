package agh.ics.oop.model.repository;

import agh.ics.oop.TestConfigurationHelper;
import agh.ics.oop.model.exceptions.DeleteSaveException;
import agh.ics.oop.model.exceptions.LoadConfigurationException;
import agh.ics.oop.model.exceptions.SaveFailedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class JsonConfigurationRepositoryAdapterTest {
    private static final String TEST_PATH = "src/test/resources/configurations";

    private JsonConfigurationRepositoryAdapter sut; // system under control

    @BeforeEach
    void setUp() {
        sut = new JsonConfigurationRepositoryAdapter(TEST_PATH);
    }

    @AfterEach
    void tearDown() {
        deleteTestDirectory();
    }

    @Test
    void shouldCreateDirectoryIfNotExists() {
        File file = new File(TEST_PATH);
        assertTrue(file.exists());
        assertTrue(sut.getSaveNames().isEmpty());
    }

    @Test
    void shouldSaveFile() {
        // given
        var earthConfiguration = TestConfigurationHelper.createEarthConfigurationNoAnimals();
        var fireConfiguration = TestConfigurationHelper.createFireConfiguration();

        // when
        try {
            sut.save(earthConfiguration, "earthConfig");
            sut.save(fireConfiguration, "fireConfig");
        } catch (SaveFailedException e) {
            fail("Saving configurations should not fail: e=" + e.getMessage());
        }

        //then
        var saves = sut.getSaveNames();
        var directoryFiles = new File(TEST_PATH).listFiles();
        assertEquals(2, saves.size());
        assertEquals("earthConfig", saves.getFirst());
        assertEquals("fireConfig", saves.getLast());
        assertTrue(isFileSaved("earthConfig", directoryFiles));
        assertTrue(isFileSaved("fireConfig", directoryFiles));
    }

    @Test
    void saveFileShouldThrowExceptionSaveNameExists() {
        // given
        var configuration = TestConfigurationHelper.createEarthConfigurationNoAnimals();
        var otherConfiguration = TestConfigurationHelper.createEarthSimulationNoPlantsAndNoCopulation();
        var saveName = "configuration";
        try {
            sut.save(configuration, saveName);
        } catch (SaveFailedException e) {
            fail("Saving configurations should not fail: e=" + e.getMessage());
        }

        //when & then
        assertThrows(SaveFailedException.class, () -> sut.save(otherConfiguration, saveName));
    }

    @Test
    void loadSaveThatNoExistShouldThrowException() {
        // when & then
        assertThrows(LoadConfigurationException.class, () -> sut.loadConfiguration("NoExist"));
    }

    @Test
    void loadSaveShouldSucceed() {
        // given
        var configuration = TestConfigurationHelper.createEarthConfigurationNoAnimals();
        var expectedMapConfiguration = configuration.getWorldMapConfiguration();
        var expectedSimulationConfiguration = configuration.getSimulationConfiguration();
        var expectedAnimalConfiguration = configuration.getAnimalConfiguration();

        var saveName = "configuration";
        try {
            sut.save(configuration, saveName);
        } catch (SaveFailedException e) {
            fail("Saving configurations should not fail: e=" + e.getMessage());
        }

        // when & then
        try {
            var result = sut.loadConfiguration(saveName);
            assertNotNull(result);
            var resultMapConfiguration = result.getWorldMapConfiguration();
            var resultSimulationConfiguration = result.getSimulationConfiguration();
            var resultAnimalConfiguration = result.getAnimalConfiguration();

            assertNotNull(resultMapConfiguration);
            assertNotNull(resultSimulationConfiguration);
            assertNotNull(resultAnimalConfiguration);
            assertEquals(expectedMapConfiguration, resultMapConfiguration);
            assertEquals(expectedSimulationConfiguration, resultSimulationConfiguration);
            assertEquals(expectedAnimalConfiguration, resultAnimalConfiguration);
        } catch (LoadConfigurationException e) {
            fail("Test should not fail loading configuraton!, e=" + e.getMessage());
        }
    }

    @Test
    void deleteSaveShouldSucceed() {
        // given
        var configuration = TestConfigurationHelper.createFireConfiguration();
        var saveName = "saveToDelete";
        var saveFile = new File(TEST_PATH, saveName + ".json");


        try {
            sut.save(configuration, saveName);
        } catch (SaveFailedException e) {
            fail("Saving configurations should not fail: e=" + e.getMessage());
        }

        assertTrue(saveFile.exists(), "Save file should exist before deletion");
        assertTrue(sut.getSaveNames().contains(saveName));

        // when
        try {
            sut.delete(saveName);
        } catch (DeleteSaveException e) {
            fail("Deleting the save should not fail: e=" + e.getMessage());
        }

        // then
        assertFalse(saveFile.exists());
        assertFalse(sut.getSaveNames().contains(saveName));
    }

    @Test
    void deleteSaveThatNoExistsShouldThrowException() {
        // when & then
        assertThrows(DeleteSaveException.class, () -> sut.delete("no exist"));
    }


    private void deleteTestDirectory() {
        File directory = new File(TEST_PATH);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.delete()) {
                        System.out.println("Couldn't delete file: " + file.getAbsolutePath());
                    }
                }
            }

            if (!directory.delete()) {
                System.out.println("Couldn't delete directory: " + directory.getAbsolutePath());
            }
        }
        assertFalse(directory.exists(), "The directory should be deleted.");
    }


    private boolean isFileSaved(String saveName, File[] directoryFiles) {
        return Arrays.stream(directoryFiles).anyMatch(file -> file.getName().equals(String.format("%s.json", saveName)));
    }

}