package agh.ics.oop.model.configuration;

import lombok.Getter;

@Getter
public enum WorldMapVariant {
    EARTH("Earth"),
    FIRE("Fire Earth");

    private final String displayText;

    WorldMapVariant(String displayText) {
        this.displayText = displayText;
    }
}
