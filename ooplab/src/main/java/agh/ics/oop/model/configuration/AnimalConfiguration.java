package agh.ics.oop.model.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@AllArgsConstructor
@ToString
public class AnimalConfiguration {
    private final int startEnergy;
    private final int minimumMutationCount;
    private final int maximumMutationCount;
    private final int wellFedEnergy;
    private final int lossCopulateEnergy;
    private final MutationVariant mutationVariant;
    private final int genomeLength;
}
