package agh.ics.oop.model.configuration;

public class AnimalConfigurationBuilder {
    private int startEnergy;
    private int minimumMutationCount;
    private int maximumMutationCount;
    private MutationVariant mutationVariant;
    private int genomeLength;

    private AnimalConfigurationBuilder() {
    }

    public static AnimalConfigurationBuilder create() {
        return new AnimalConfigurationBuilder();
    }

    public AnimalConfigurationBuilder startEnergy(int startEnergy) {
        this.startEnergy = startEnergy;
        return this;
    }

    public AnimalConfigurationBuilder minimumMutationCount(int minimumMutationCount) {
        this.minimumMutationCount = minimumMutationCount;
        return this;
    }

    public AnimalConfigurationBuilder maximumMutationCount(int maximumMutationCount) {
        this.maximumMutationCount = maximumMutationCount;
        return this;
    }

    public AnimalConfigurationBuilder mutationVariant(MutationVariant mutationVariant) {
        this.mutationVariant = mutationVariant;
        return this;
    }

    public AnimalConfigurationBuilder genomeLength(int genomeLength) {
        this.genomeLength = genomeLength;
        return this;
    }

    public AnimalConfiguration build() {
        return new AnimalConfiguration(startEnergy, minimumMutationCount, maximumMutationCount, mutationVariant, genomeLength);
    }


}
