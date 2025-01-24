package agh.ics.oop.model.configuration;

import agh.ics.oop.model.exceptions.WrongConfigurationParameterException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigurationValidatorTest {

    private final ConfigurationValidator sut = new ConfigurationValidator();


    @Test
    void validateConfigurationMaxGenomeLengthTooHighShouldThrow() {
        // given
        var animalConfiguration = AnimalConfiguration.builder()
                .genomeLength(ConfigurationValidator.MAX_GENOME_LENGTH + 1)
                .build();
        var configuration = Configuration.builder()
                .simulationConfiguration(SimulationConfiguration.builder().build())
                .worldMapConfiguration(WorldMapConfiguration.builder().build())
                .animalConfiguration(animalConfiguration)
                .build();

        // when & then
        assertThrows(WrongConfigurationParameterException.class, () -> sut.validate(configuration));
    }


    @Test
    void validateConfigurationMaxStartAnimalCountTooHighShouldThrow() {
        // given
        var simulationConfiguration = SimulationConfiguration.builder()
                .startAnimalCount(ConfigurationValidator.MAX_START_ANIMAL_COUNT + 1)
                .build();
        var configuration = Configuration.builder()
                .simulationConfiguration(simulationConfiguration)
                .worldMapConfiguration(WorldMapConfiguration.builder().build())
                .animalConfiguration(AnimalConfiguration.builder().build())
                .build();

        // when & then
        assertThrows(WrongConfigurationParameterException.class, () -> sut.validate(configuration));
    }


    @Test
    void validateConfigurationMaxMapWidthTooHighShouldThrow() {
        // given
        var worldMapConfiguration = WorldMapConfiguration.builder()
                .width(ConfigurationValidator.MAX_WIDTH + 1)
                .build();
        var configuration = Configuration.builder()
                .simulationConfiguration(SimulationConfiguration.builder().build())
                .worldMapConfiguration(worldMapConfiguration)
                .animalConfiguration(AnimalConfiguration.builder().build())
                .build();

        // when & then
        assertThrows(WrongConfigurationParameterException.class, () -> sut.validate(configuration));
    }

    @Test
    void validateConfigurationMinMapWidthTooLowShouldThrow() {
        // given
        var worldMapConfiguration = WorldMapConfiguration.builder()
                .width(ConfigurationValidator.MIN_WIDTH - 1)
                .build();
        var configuration = Configuration.builder()
                .simulationConfiguration(SimulationConfiguration.builder().build())
                .worldMapConfiguration(worldMapConfiguration)
                .animalConfiguration(AnimalConfiguration.builder().build())
                .build();

        // when & then
        assertThrows(WrongConfigurationParameterException.class, () -> sut.validate(configuration));
    }

    @Test
    void validateConfigurationMaxMapHeightTooHighShouldThrow() {
        // given
        var worldMapConfiguration = WorldMapConfiguration.builder()
                .height(ConfigurationValidator.MAX_HEIGHT + 1)
                .build();
        var configuration = Configuration.builder()
                .simulationConfiguration(SimulationConfiguration.builder().build())
                .worldMapConfiguration(worldMapConfiguration)
                .animalConfiguration(AnimalConfiguration.builder().build())
                .build();

        // when & then
        assertThrows(WrongConfigurationParameterException.class, () -> sut.validate(configuration));
    }

    @Test
    void validateConfigurationMinMapHeightTooLowShouldThrow() {
        // given
        var worldMapConfiguration = WorldMapConfiguration.builder()
                .height(ConfigurationValidator.MIN_HEIGHT - 1)
                .build();
        var configuration = Configuration.builder()
                .simulationConfiguration(SimulationConfiguration.builder().build())
                .worldMapConfiguration(worldMapConfiguration)
                .animalConfiguration(AnimalConfiguration.builder().build())
                .build();

        // when & then
        assertThrows(WrongConfigurationParameterException.class, () -> sut.validate(configuration));
    }

    @Test
    void validateConfigurationMaxPlantGrowthTooHighShouldThrow() {
        // given
        var worldMapConfiguration = WorldMapConfiguration.builder()
                .plantGrowth(ConfigurationValidator.MAX_PLANT_GROWTH + 1)
                .build();
        var configuration = Configuration.builder()
                .simulationConfiguration(SimulationConfiguration.builder().build())
                .worldMapConfiguration(worldMapConfiguration)
                .animalConfiguration(AnimalConfiguration.builder().build())
                .build();

        // when & then
        assertThrows(WrongConfigurationParameterException.class, () -> sut.validate(configuration));
    }

}