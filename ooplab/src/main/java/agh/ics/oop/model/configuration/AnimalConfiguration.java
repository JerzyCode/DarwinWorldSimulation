package agh.ics.oop.model.configuration;

public class AnimalConfiguration {
    private final int startEnergy;
    private final int minimumMutationCount;
    private final int maximumMutationCount;
    private final MutationVariant mutationVariant;
    private final int genomeLength;

    AnimalConfiguration(int startEnergy, int minimumMutationCount, int maximumMutationCount, MutationVariant mutationVariant, int genomeLength) {
        this.startEnergy = startEnergy;
        this.minimumMutationCount = minimumMutationCount;
        this.maximumMutationCount = maximumMutationCount;
        this.mutationVariant = mutationVariant;
        this.genomeLength = genomeLength;
    }

    public int getStartEnergy() {
        return startEnergy;
    }


    public int getMinimumMutationCount() {
        return minimumMutationCount;
    }


    public int getMaximumMutationCount() {
        return maximumMutationCount;
    }


    public MutationVariant getMutationVariant() {
        return mutationVariant;
    }

    public int getGenomeLength() {
        return genomeLength;
    }


    @Override
    public String toString() {
        return "AnimalConfiguration{" +
                "startEnergy=" + startEnergy +
                ", minimumMutationCount=" + minimumMutationCount +
                ", maximumMutationCount=" + maximumMutationCount +
                ", mutationVariant=" + mutationVariant +
                ", genomeLength=" + genomeLength +
                '}';
    }
}
