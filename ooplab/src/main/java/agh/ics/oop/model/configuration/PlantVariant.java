package agh.ics.oop.model.configuration;

import lombok.Getter;

@Getter
public enum PlantVariant {
    FULL_RANDOM("Full random"),
    FORESTED_EQUATORS("Forest Equators");

    private final String displayText;

    PlantVariant(String displayText) {
        this.displayText = displayText;
    }
}
