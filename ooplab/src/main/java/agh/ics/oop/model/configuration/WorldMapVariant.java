package agh.ics.oop.model.configuration;

public enum WorldMapVariant {
    EARTH("Earth"),
    FIRE("Fire Earth");

    private final String displayText;

    WorldMapVariant(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}
