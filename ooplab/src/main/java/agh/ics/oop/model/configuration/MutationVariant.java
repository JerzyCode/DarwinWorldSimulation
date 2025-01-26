package agh.ics.oop.model.configuration;

import lombok.Getter;

@Getter
public enum MutationVariant {
    FULL_RANDOM("Full Random"),
    LITTLE_CORRECTION("Little Correction");

    private final String displayText;

    MutationVariant(String displayText) {
        this.displayText = displayText;
    }
}
