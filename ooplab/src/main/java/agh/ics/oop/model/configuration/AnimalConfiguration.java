package agh.ics.oop.model.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class AnimalConfiguration {
    private final int startEnergy;
    private final int minimumMutationCount;
    private final int maximumMutationCount;
    private final int wellFedEnergy;
    private final int lossCopulateEnergy; // nazwa
    private final MutationVariant mutationVariant;
    private final int genomeLength;

    @JsonCreator
    public AnimalConfiguration(
            @JsonProperty("startEnergy") int startEnergy,
            @JsonProperty("minimumMutationCount") int minimumMutationCount,
            @JsonProperty("maximumMutationCount") int maximumMutationCount,
            @JsonProperty("wellFedEnergy") int wellFedEnergy,
            @JsonProperty("lossCopulateEnergy") int lossCopulateEnergy,
            @JsonProperty("mutationVariant") MutationVariant mutationVariant,
            @JsonProperty("genomeLength") int genomeLength) {
        this.startEnergy = startEnergy;
        this.minimumMutationCount = minimumMutationCount;
        this.maximumMutationCount = maximumMutationCount;
        this.wellFedEnergy = wellFedEnergy;
        this.lossCopulateEnergy = lossCopulateEnergy;
        this.mutationVariant = mutationVariant;
        this.genomeLength = genomeLength;
    }
}
