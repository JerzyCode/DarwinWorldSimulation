package agh.ics.oop.model.configuration;

public record SimulationConfiguration(
    int energyGain,
    int plantGrowth,
    PlantVariant plantVariant,
    int wellFedEnergy,
    int lossCopulateEnergy,
    int fireFrequency
) {
}
