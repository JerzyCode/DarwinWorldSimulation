package agh.ics.oop.model.configuration;

public record AnimalConfiguration(
    int startEnergy,
    int minimumMutationCount,
    int maximumMutationCount,
    MutationVariant mutationVariant,
    int genomeLength) {
}
