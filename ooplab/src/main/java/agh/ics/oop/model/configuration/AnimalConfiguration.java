package agh.ics.oop.model.configuration;

public class AnimalConfiguration {
    private int startEnergy;
    private int minimumMutationCount;
    private int maximumMutationCount;
    private MutationVariant mutationVariant;
    private int genomeLength;

    AnimalConfiguration() {
        this(20, 2, 5, MutationVariant.FULL_RANDOM, 10);
    }

    public AnimalConfiguration(int startEnergy, int minimumMutationCount, int maximumMutationCount, MutationVariant mutationVariant, int genomeLength) {
        this.startEnergy = startEnergy;
        this.minimumMutationCount = minimumMutationCount;
        this.maximumMutationCount = maximumMutationCount;
        this.mutationVariant = mutationVariant;
        this.genomeLength = genomeLength;
    }

    public int getStartEnergy() {
        return startEnergy;
    }

    public void setStartEnergy(int startEnergy) {
        this.startEnergy = startEnergy;
    }

    public int getMinimumMutationCount() {
        return minimumMutationCount;
    }

    public void setMinimumMutationCount(int minimumMutationCount) {
        this.minimumMutationCount = minimumMutationCount;
    }

    public int getMaximumMutationCount() {
        return maximumMutationCount;
    }

    public void setMaximumMutationCount(int maximumMutationCount) {
        this.maximumMutationCount = maximumMutationCount;
    }

    public MutationVariant getMutationVariant() {
        return mutationVariant;
    }

    public void setMutationVariant(MutationVariant mutationVariant) {
        this.mutationVariant = mutationVariant;
    }

    public int getGenomeLength() {
        return genomeLength;
    }

    public void setGenomeLength(int genomeLength) {
        this.genomeLength = genomeLength;
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
